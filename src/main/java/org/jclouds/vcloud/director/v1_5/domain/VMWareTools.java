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
package org.jclouds.vcloud.director.v1_5.domain;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Objects.equal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


import com.google.common.base.Objects;

@XmlRootElement(name = "VMWareTools")
@XmlType(name = "VMWareTools")
public class VMWareTools {

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromVMWareTools(this);
   }

   public static class Builder {

      private String version;

      /**
       * @see VMWareTools#getVersion()
       */
      public Builder version(String version) {
         this.version = version;
         return this;
      }

      public VMWareTools build() {
         VMWareTools vmWareTools = new VMWareTools(version);
         return vmWareTools;
      }

      public Builder fromVMWareTools(VMWareTools in) {
         return version(in.getVersion());
      }
   }

   @XmlAttribute(required = true)
   protected String version;

   protected VMWareTools() {
      // For JAXB and builder use
   }

   public VMWareTools(String version) {
      this.version = version;
   }

   /**
    * Gets the value of the version property.
    */
   public String getVersion() {
      return version;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      VMWareTools that = VMWareTools.class.cast(o);
      return equal(this.version, that.version);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(version);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper("").add("version", version).toString();
   }
}
