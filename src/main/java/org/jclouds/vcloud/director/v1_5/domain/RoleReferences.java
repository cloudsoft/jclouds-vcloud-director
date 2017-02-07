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

import javax.xml.bind.annotation.XmlRootElement;

import org.jclouds.vcloud.director.v1_5.domain.query.QueryResultReferences;


/**
 * Container for ReferenceType elements that reference RoleType objects.
 * 
 * <pre>
 * &lt;complexType name="RoleReferences"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.vmware.com/vcloud/v1.5}VCloudExtensibleType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RoleReference" type="{http://www.vmware.com/vcloud/v1.5}ReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlRootElement(name = "RoleReferences")
public class RoleReferences extends QueryResultReferences {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   @Override
   public Builder<?> toBuilder() {
      return builder().fromRoleReferences(this);
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
   }
   
   public static class Builder<B extends Builder<B>> extends QueryResultReferences.Builder<B> {

      @Override
      public RoleReferences build() {
         return new RoleReferences(this);
      }

      public B fromRoleReferences(RoleReferences in) {
         return fromQueryResultReferences(in);
      }
   }

   protected RoleReferences(Builder<?> builder) {
      super(builder);
   }

   protected RoleReferences() {
      // for JAXB
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      RoleReferences that = RoleReferences.class.cast(o);
      return super.equals(that);
   }
   
   // NOTE hashcode inherited from QueryResultReferences

}
