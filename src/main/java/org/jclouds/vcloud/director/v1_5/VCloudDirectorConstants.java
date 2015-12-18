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
package org.jclouds.vcloud.director.v1_5;

/**
 * Constants used by VCloudDirector apis
 */
public final class VCloudDirectorConstants {

   /** The XML namespace used by the apis. */
   public static final String VCLOUD_1_5_NS = "http://www.vmware.com/vcloud/v1.5";

   public static final String VCLOUD_VMW_NS = "http://www.vmware.com/schema/ovf";

   /** The property used to configure the timeout for task completion. */
   public static final String PROPERTY_VCLOUD_DIRECTOR_TIMEOUT_TASK_COMPLETED = "jclouds.vcloud-director.timeout.task-complete";

   public static final String PROPERTY_VCLOUD_DIRECTOR_VERSION_SCHEMA = "jclouds.vcloud-director.version.schema";

   /** TODO javadoc */
   public static final String PROPERTY_VCLOUD_DIRECTOR_XML_NAMESPACE = "jclouds.vcloud-director.xml.ns";
   
   /** TODO javadoc */
   public static final String PROPERTY_VCLOUD_DIRECTOR_XML_SCHEMA = "jclouds.vcloud-director.xml.schema";

   // TODO put these somewhere else, maybe core?

   /** TODO javadoc */
   public static final String PROPERTY_DNS_NAME_LEN_MIN = "jclouds.dns_name_length_min";

   /** TODO javadoc */
   public static final String PROPERTY_NS_NAME_LEN_MAX = "jclouds.dns_name_length_max";

   /**
    * For synthesizing hardware profiles, the maximum number of CPUs.
    */
   public static final String PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MAX_CPU = "jclouds.vcloud-director.hardware-profiles.max-cpu";

   /**
    * For synthesizing hardware profiles, the minimum megabytes of RAM.
    */
   public static final String PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MIN_RAM = "jclouds.vcloud-director.hardware-profiles.min-ram";

   /**
    * For synthesizing hardware profiles, the maximum megabytes of RAM.
    */
   public static final String PROPERTY_VCLOUD_DIRECTOR_HARDWARE_MAX_RAM = "jclouds.vcloud-director.hardware-profiles.max-ram";

   /**
    * For synthesizing hardware profiles, the maximum number of CPUs.
    */
   public static final String PROPERTY_VCLOUD_DIRECTOR_PREDEFINED_HARDWARE_PROFILES = "jclouds.vcloud-director.hardware-profiles.predefined";

   /** TODO javadoc */
   /*
   public static final TypeToken<RestContext<SessionApi, SessionAsyncApi>> SESSION_CONTEXT_TYPE =
         new TypeToken<RestContext<SessionApi, SessionAsyncApi>>() {
		   };
*/
   private VCloudDirectorConstants() {
      throw new AssertionError("intentionally unimplemented");
   }
}
