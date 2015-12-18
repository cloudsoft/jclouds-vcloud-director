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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.Processor;

public class HardwareProfiles {

   public static final Pattern SHORT_NAME_PATTERN = Pattern.compile("([0-9]+)CPU_([0-9\\.]+)GB_RAM");

   public static Hardware createHardwareProfile(int numCpu, int ramAllocatedMB) {

      String shortName;
      if ((ramAllocatedMB & 0x3ff) == 0) {
         // Exact multiple of 1024
         int ramAllocatedGB = ramAllocatedMB >> 10;
         shortName = String.format("%dCPU_%dGB_RAM", numCpu, ramAllocatedGB);
      } else {
         // Inexact multiple of 1024
         double ramAllocatedGB = ((double) ramAllocatedMB) / 1024.0;
         shortName = String.format("%dCPU_%sGB_RAM", numCpu, Double.toString(ramAllocatedGB));
      }

      return new HardwareBuilder().ids(shortName).hypervisor("esxi").name(shortName).processor(new Processor(numCpu, 1)).ram(ramAllocatedMB).build();
   }
   
   public static Hardware createHardwareProfile(String shortName) {
      Matcher matcher = SHORT_NAME_PATTERN.matcher(shortName);
      if (!matcher.matches()) {
          throw new IllegalArgumentException("Invalid hardware profile '" + shortName + "'");
      }
      String cpus = matcher.group(1);
      String ram = matcher.group(2);
      int numCpus = Integer.parseInt(cpus);
      int ramAllocatedMB = (int) (Double.parseDouble(ram) * 1024);

      return new HardwareBuilder().ids(shortName).hypervisor("esxi").name(shortName).processor(new Processor(numCpus, 1)).ram(ramAllocatedMB).build();
   }
}
