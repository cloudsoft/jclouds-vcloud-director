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
package org.jclouds.vcloud.director.v1_5.compute.suppliers;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jclouds.compute.domain.Hardware;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorConstants;
import org.jclouds.vcloud.director.v1_5.compute.util.HardwareProfiles;

import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;

public class VirtualHardwareConfigSupplier implements Supplier<Set<Hardware>> {

   private final ImmutableSet<Hardware> hardwareConfigs;

   @Inject
   public VirtualHardwareConfigSupplier(@Named(VCloudDirectorConstants.PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MAX_CPU) int maxCpuFromProperties,
                                        @Named(VCloudDirectorConstants.PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MIN_RAM) int minRamFromProperties,
                                        @Named(VCloudDirectorConstants.PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MAX_RAM) int maxRamFromProperties,
                                        @Named(VCloudDirectorConstants.PROPERTY_VCLOUD_DIRECTOR_PREDEFINED_HARDWARE_PROFILES) String predefinedProfiles) {
      // Synthetic profiles
      ImmutableSet.Builder<Hardware> builder = ImmutableSet.builder();
      for (int cpu = 1; cpu <= maxCpuFromProperties; cpu++) {
         for (int ram = minRamFromProperties; ram <= maxRamFromProperties; ram *= 2) {
            builder.add(HardwareProfiles.createHardwareProfile(cpu, ram));
         }
      }
      // Predefined profiles, supplied explicitly by the user
      if (predefinedProfiles != null) {
         for (String profile : Splitter.on(",").trimResults().omitEmptyStrings().split(predefinedProfiles)) {
            builder.add(HardwareProfiles.createHardwareProfile(profile));
         }
      }
      this.hardwareConfigs = builder.build();
   }

   @Override
   public Set<Hardware> get() {
      return hardwareConfigs;
   }
}
