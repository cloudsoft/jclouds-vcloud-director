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
package org.jclouds.vcloud.director.v1_5.domain.dmtf;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.vcloud.director.v1_5.domain.dmtf.DMTFConstants.OVF_NS;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorConstants.VCLOUD_1_5_NS;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.SectionType;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.VirtualHardwareSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem;
import org.jclouds.vcloud.director.v1_5.domain.section.GuestCustomizationSection;
import org.jclouds.vcloud.director.v1_5.domain.section.NetworkConnectionSection;
import org.jclouds.vcloud.director.v1_5.domain.section.OperatingSystemSection;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@XmlRootElement(name = "VirtualSystem", namespace = OVF_NS)
public class VirtualSystem extends SectionType {

   @XmlElement(name = "Name", namespace = OVF_NS)
   private String name;
   @XmlElement(name = "OperatingSystemSection", namespace = OVF_NS)
   private OperatingSystemSection operatingSystem;
   @XmlElement(name = "VirtualHardwareSection", namespace = OVF_NS)
   private Set<VirtualHardwareSection> virtualHardwareSections;
   @XmlElement(name = "GuestCustomizationSection", namespace = VCLOUD_1_5_NS)
   private Set<GuestCustomizationSection> guestCustomizationSections;
   @XmlElement(name = "NetworkConnectionSection", namespace = VCLOUD_1_5_NS)
   private Set<NetworkConnectionSection> networkConnectionSections;

   @XmlElement(name = "AnnotationSection", namespace = OVF_NS)
   private AnnotationSection annotationSection;

   private VirtualSystem(Builder<?> builder) {
      super(builder);
      this.name = checkNotNull(builder.name, "name");
      this.operatingSystem = checkNotNull(builder.operatingSystem, "operatingSystem");
      this.virtualHardwareSections = ImmutableSet.copyOf(checkNotNull(builder.virtualHardwareSections, "virtualHardwareSections"));
      this.guestCustomizationSections = ImmutableSet.copyOf(checkNotNull(builder.guestCustomizationSections, "guestCustomizationSections"));
      this.networkConnectionSections = ImmutableSet.copyOf(checkNotNull(builder.networkConnectionSections, "networkConnectionSections"));
   }
   
   private VirtualSystem() {
      // for JAXB
   }

   public OperatingSystemSection getOperatingSystemSection() {
      return operatingSystem;
   }

   /**
    * Each VirtualSystem element may contain one or more VirtualHardwareSection elements, each of
    * which describes the virtual virtualHardwareSections required by the virtual system.
    */
   public Set<? extends VirtualHardwareSection> getVirtualHardwareSections() {
      return virtualHardwareSections;
   }



   @Override
   public int hashCode() {
      return Objects.hashCode(super.hashCode(), operatingSystem, virtualHardwareSections);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;

      VirtualSystem other = (VirtualSystem) obj;
      return super.equals(other) 
            && equal(operatingSystem, other.operatingSystem)
            && equal(virtualHardwareSections, other.virtualHardwareSections);
   }

   @Override
   protected Objects.ToStringHelper string() {
      return super.string()
            .add("operatingSystem", operatingSystem)
            .add("virtualHardwareSections", virtualHardwareSections);
   }

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return builder().fromVirtualSystem(this);
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
   }

   public static class Builder<B extends Builder<B>> extends BaseVirtualSystem.Builder<B> {

      private String name;
      private OperatingSystemSection operatingSystem;
      private Set<VirtualHardwareSection> virtualHardwareSections = Sets.newLinkedHashSet();
      private Set<GuestCustomizationSection> guestCustomizationSections = Sets.newLinkedHashSet();
      private Set<NetworkConnectionSection> networkConnectionSections = Sets.newLinkedHashSet();

      public B operatingSystemSection(OperatingSystemSection operatingSystem) {
         this.operatingSystem = operatingSystem;
         return self();
      }

      public B virtualHardwareSection(VirtualHardwareSection virtualHardwareSection) {
         this.virtualHardwareSections.add(checkNotNull(virtualHardwareSection, "virtualHardwareSection"));
         return self();
      }

      public B virtualHardwareSections(Iterable<? extends VirtualHardwareSection> virtualHardwareSections) {
         this.virtualHardwareSections = Sets.newLinkedHashSet(checkNotNull(virtualHardwareSections, "virtualHardwareSections"));
         return self();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public VirtualSystem build() {
         return new VirtualSystem(this);
      }

      public B fromVirtualSystem(VirtualSystem in) {
         return this
                 .operatingSystemSection(in.getOperatingSystemSection())
                 .virtualHardwareSections(in.getVirtualHardwareSections());
      }
   }
}
