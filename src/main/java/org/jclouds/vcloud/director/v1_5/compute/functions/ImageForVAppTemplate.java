/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.vcloud.director.v1_5.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import java.net.URI;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.Envelope;
import org.jclouds.vcloud.director.v1_5.domain.query.QueryResultVAppTemplateRecord;
import org.jclouds.vcloud.director.v1_5.domain.section.OperatingSystemSection;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

@Singleton
public class ImageForVAppTemplate implements Function<QueryResultVAppTemplateRecord, Image> {

   private static final String CENTOS = "centos";
   private static final String UBUNTU = "ubuntu";
   private static final String SUSE = "sles";
   private static final String WINDOWS = "windows";
   private static final String UNRECOGNIZED = "unrecognized";

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   public Logger logger = Logger.NULL;

   private final Function<String, Image.Status> toPortableImageStatus;
   private final Function<URI, Envelope> templateToEnvelope;
   private final FindLocationForResource findLocationForResourceInVDC;
   private final Supplier<Set<? extends Location>> locations;

   @Inject
   protected ImageForVAppTemplate(Function<String, Image.Status> toPortableImageStatus, Function<URI, Envelope> templateToEnvelope,
            FindLocationForResource findLocationForResource, @Memoized Supplier<Set<? extends Location>> locations) {
      this.toPortableImageStatus = checkNotNull(toPortableImageStatus, "toPortableImageStatus");
      this.templateToEnvelope = checkNotNull(templateToEnvelope, "templateToEnvelope");
      this.findLocationForResourceInVDC = checkNotNull(findLocationForResource, "findLocationForResourceInVDC");
      this.locations = checkNotNull(locations, "locations");
   }

   @Override
   public Image apply(QueryResultVAppTemplateRecord from) {
      checkNotNull(from, "VAppTemplate");
      Envelope ovf = templateToEnvelope.apply(from.getHref());

      ImageBuilder builder = new ImageBuilder();
      builder.ids(getVappId(from));
      builder.uri(from.getHref());
      builder.name(from.getName());
      builder.description(from.getName()+"_"+from.getCatalogName());
      OperatingSystem os;
      if (ovf.getVirtualSystem() != null) {
         os = setOsDetails(ovf.getVirtualSystem().getOperatingSystemSection());
      } else {
         os = OperatingSystem.builder().description(UNRECOGNIZED).build();
      }
      builder.operatingSystem(os);
      builder.status(toPortableImageStatus.apply(from.getStatus()));
      return builder.build();
   }

   private String getVappId(QueryResultVAppTemplateRecord from) {
      String vApp = from.getHref().getPath().substring(from.getHref().getPath().lastIndexOf("/"));
      return vApp.substring(vApp.indexOf("-") + 1);
   }

   private boolean is64bit(String osType) {
      if (osType != null) {
         return osType.contains("64");
      }
      return false;
   }

   private OperatingSystem setOsDetails(OperatingSystemSection operatingSystemSection) {
      OperatingSystem.Builder osBuilder = OperatingSystem.builder();
      if (operatingSystemSection != null) {
         String osType = operatingSystemSection.getOsType();
         String description = operatingSystemSection.getDescription();
         osBuilder.description(description);
         osBuilder.is64Bit(is64bit(osType));
         if (osType.startsWith(CENTOS)) {
            osBuilder.family(OsFamily.CENTOS).version(parseVersion(CENTOS, osType).apply(description));
         }
         else if (osType.startsWith(UBUNTU)) {
            osBuilder.family(OsFamily.UBUNTU).version(parseVersion(UBUNTU, osType).apply(description));
         }
         else if (osType.startsWith(WINDOWS)) {
            osBuilder.family(OsFamily.WINDOWS).version(parseVersion(WINDOWS, osType).apply(description));
         }
         else if (osType.startsWith(SUSE)) {
            osBuilder.family(OsFamily.SUSE).version(parseVersion(SUSE, osType).apply(description));
         }
      }
      return osBuilder.build();
   }

   private Function<String, String> parseVersion(final String osFamily, final String osType) {
      return new Function<String, String>() {

         @Override
         public String apply(final String description) {
            if (osType != null) {
               if (osType.contains("_")) {
                  return Iterables.get(Splitter.on("_").split(osType), 0).substring(osFamily.indexOf(osFamily) +
                          osFamily.length()).trim();
               }
            }
            if (description != null) {
               String stripped = description.contains(" (") ? description.substring(0,
                       description.indexOf(" (")) : description;
               if (stripped.toLowerCase().contains(osFamily)) {
                  return stripped.substring(stripped.toLowerCase().indexOf(osFamily) + osFamily.length()).trim();
               }
            }
            return null;
         }

      };
   }

}
