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

import javax.annotation.Resource;
import javax.inject.Inject;

import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.predicates.ImagePredicates;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.director.v1_5.domain.Vm;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.ResourceAllocationSettingData;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.SectionType;
import org.jclouds.vcloud.director.v1_5.domain.section.VirtualHardwareSection;
import org.jclouds.vcloud.director.v1_5.functions.VirtualHardwareSectionForVApp;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class HardwareForVm implements Function<Vm, Hardware> {

   @Resource
   protected Logger logger = Logger.NULL;

   //private final Function<Reference, Location> findLocationForResource;
   private final VCloudHardwareBuilderFromResourceAllocations rasdToHardwareBuilder;
   private final VirtualHardwareSectionForVApp findVirtualHardwareSectionForVm;

   @Inject
   protected HardwareForVm(
           //Function<Reference, Location> findLocationForResource,
            VCloudHardwareBuilderFromResourceAllocations rasdToHardwareBuilder,
            VirtualHardwareSectionForVApp findVirtualHardwareSectionForVm) {
      //this.findLocationForResource = checkNotNull(findLocationForResource, "findLocationForResource");
      this.rasdToHardwareBuilder = checkNotNull(rasdToHardwareBuilder, "rasdToHardwareBuilder");
      this.findVirtualHardwareSectionForVm = checkNotNull(findVirtualHardwareSectionForVm, "findVirtualHardwareSectionForVm");
   }

   @Override
   public Hardware apply(Vm from) {
      checkNotNull(from, "VApp");
      // TODO make this work with composite vApps
      if (from == null)
         return null;

      VirtualHardwareSection hardware = findVirtualHardwareSectionForVm.apply(from);
      HardwareBuilder builder = rasdToHardwareBuilder.apply(hardware.getItems());
      /*
      builder.location(findLocationForResource.apply(Iterables.find(checkNotNull(from, "from").getLinks(), 
            LinkPredicates.typeEquals(VCloudDirectorMediaType.VDC))));
            */
      builder.ids(from.getHref().toASCIIString()).name(from.getName()).supportsImage(
              ImagePredicates.idEquals(from.getHref().toASCIIString()));
      VirtualHardwareSection virtualHardwareSection = findVirtualHardwareSection(from);
      int ram = extractRamFrom(virtualHardwareSection);
      double cores = extractCoresFrom(virtualHardwareSection);
      builder.id(from.getId())
             .name(from.getName())
             .hypervisor("esxi")
             .providerId(from.getHref().toString())
             .ram(ram)
             .processors(ImmutableList.of(new Processor(cores, 1)));
      return builder.build();
   }

   private double extractCoresFrom(VirtualHardwareSection virtualHardwareSection) {
      ResourceAllocationSettingData resourceAllocationSettingData = Iterables.find(virtualHardwareSection.getItems(), new
              Predicate<ResourceAllocationSettingData>() {
                 @Override
                 public boolean apply(ResourceAllocationSettingData input) {
                    return input.getResourceType() == ResourceAllocationSettingData.ResourceType.PROCESSOR;
                 }
              });
      return resourceAllocationSettingData.getVirtualQuantity().doubleValue();   }

   private VirtualHardwareSection findVirtualHardwareSection(Vm from) {
      return (VirtualHardwareSection) Iterables.find(from.getSections(), new
              Predicate<SectionType>() {
                 @Override
                 public boolean apply(SectionType input) {
                    return input.getInfo().getValue().equals("Virtual hardware requirements");
                 }
              });
   }

   private int extractRamFrom(VirtualHardwareSection virtualHardwareSection) {
      ResourceAllocationSettingData resourceAllocationSettingData = Iterables.find(virtualHardwareSection.getItems(), new
              Predicate<ResourceAllocationSettingData>() {
                 @Override
                 public boolean apply(ResourceAllocationSettingData input) {
                    return input.getResourceType() == ResourceAllocationSettingData.ResourceType.MEMORY;
                 }
              });
      return resourceAllocationSettingData.getVirtualQuantity().intValue();
   }
}
