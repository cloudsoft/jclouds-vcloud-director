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

import static org.jclouds.vcloud.director.v1_5.domain.dmtf.DMTFConstants.OVF_NS;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorConstants.VCLOUD_1_5_NS;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Iterables;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.DiskSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.NetworkSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.ReferencesType;
import org.jclouds.vcloud.director.v1_5.domain.section.CustomizationSection;
import org.jclouds.vcloud.director.v1_5.domain.section.LeaseSettingsSection;
import org.jclouds.vcloud.director.v1_5.domain.section.NetworkConfigSection;

import com.google.common.collect.Sets;

@XmlRootElement(name = "Envelope", namespace = OVF_NS)
public class Envelope { //extends BaseEnvelope<VirtualSystem, Envelope> {

   @XmlElement(name = "References", namespace = OVF_NS)
   protected ReferencesType references;
   @XmlElement(name = "NetworkSection", namespace = OVF_NS)
   protected Set<NetworkSection> networkSections = Sets.newLinkedHashSet();
   @XmlElement(name = "CustomizationSection", namespace = VCLOUD_1_5_NS)
   protected Set<CustomizationSection> customizationSections = Sets.newLinkedHashSet();
   @XmlElement(name = "NetworkConfigSection", namespace = VCLOUD_1_5_NS)
   protected Set<NetworkConfigSection> networkConfigSections = Sets.newLinkedHashSet();
   @XmlElement(name = "LeaseSettingsSection", namespace = VCLOUD_1_5_NS)
   protected Set<LeaseSettingsSection> leaseSettingsSections = Sets.newLinkedHashSet();

   @XmlElement(name = "VirtualSystemCollection", namespace = OVF_NS)
   protected VirtualSystemCollection virtualSystemCollection;

   @XmlElement(name = "VirtualSystem", namespace = OVF_NS)
   protected VirtualSystem virtualSystem;

   @XmlElement(name = "DiskSection", namespace = OVF_NS)
   protected Set<DiskSection> diskSections = Sets.newLinkedHashSet();
   
   protected Envelope() {
      // For JaxB
   }

   public ReferencesType getReferences() {
      return references;
   }

   public Set<NetworkSection> getNetworkSections() {
      return networkSections;
   }

   public Set<CustomizationSection> getCustomizationSections() {
      return customizationSections;
   }

   public Set<DiskSection> getDiskSections() {
      return diskSections;
   }

   public Set<NetworkConfigSection> getNetworkConfigSections() {
      return networkConfigSections;
   }

   public Set<LeaseSettingsSection> getLeaseSettingsSections() {
      return leaseSettingsSections;
   }

   public VirtualSystem getVirtualSystem() {
      if (virtualSystem != null) {
         return virtualSystem;
      } else if (virtualSystemCollection == null || virtualSystemCollection.getVirtualSystems() == null) {
         return null;
      } else {
         return Iterables.getFirst(virtualSystemCollection.getVirtualSystems(), null);
      }
   }
}
