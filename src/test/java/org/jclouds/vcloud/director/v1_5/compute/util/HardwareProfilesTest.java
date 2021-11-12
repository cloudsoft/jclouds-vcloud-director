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
package org.jclouds.vcloud.director.v1_5.compute.util;

import static org.testng.Assert.assertEquals;

import org.jclouds.compute.domain.Hardware;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;

public class HardwareProfilesTest {

   @Test
   public void testHardwareProfileFromName() throws Exception {
      Hardware hardware = HardwareProfiles.createHardwareProfile("2CPU_4GB_RAM");
      assertEquals(Iterables.getOnlyElement(hardware.getProcessors()).getCores(), 2.0D);
      assertEquals(hardware.getRam(), 4 * 1024);
      assertEquals(hardware.getHypervisor(), "esxi");
      assertEquals(hardware.getName(), "2CPU_4GB_RAM");
   }
   
   @Test
   public void testHardwareProfileFromNameWithRamFraction() throws Exception {
      Hardware hardware = HardwareProfiles.createHardwareProfile("2CPU_0.5GB_RAM");
      assertEquals(Iterables.getOnlyElement(hardware.getProcessors()).getCores(), 2.0D);
      assertEquals(hardware.getRam(), 1024 / 2);
      assertEquals(hardware.getHypervisor(), "esxi");
      assertEquals(hardware.getName(), "2CPU_0.5GB_RAM");
   }
   
   @Test
   public void testHardwareProfileFromParts() throws Exception {
      Hardware hardware = HardwareProfiles.createHardwareProfile(2, 4 * 1024);
      assertEquals(Iterables.getOnlyElement(hardware.getProcessors()).getCores(), 2.0D);
      assertEquals(hardware.getRam(), 4 * 1024);
      assertEquals(hardware.getHypervisor(), "esxi");
      assertEquals(hardware.getName(), "2CPU_4GB_RAM");
   }
   
   @Test
   public void testHardwareProfileFromPartsWithRamFraction() throws Exception {
      Hardware hardware = HardwareProfiles.createHardwareProfile(2, 1024 / 2);
      assertEquals(Iterables.getOnlyElement(hardware.getProcessors()).getCores(), 2.0D);
      assertEquals(hardware.getRam(), 1024 / 2);
      assertEquals(hardware.getHypervisor(), "esxi");
      assertEquals(hardware.getName(), "2CPU_0.5GB_RAM");
   }
}
