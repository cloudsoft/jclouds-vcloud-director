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
package org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.vcloud.director.v1_5.domain.dmtf.DMTFConstants.OVF_NS;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;

import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.ProductSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.SectionType;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public abstract class BaseVirtualSystem extends SectionType {

   public abstract static class Builder<B extends Builder<B>> extends SectionType.Builder<B> {

      private String id;
      private String name;
      private String info;
      private Set<ProductSection> productSections = Sets.newLinkedHashSet();
      private Set<SectionType> additionalSections = Sets.newLinkedHashSet();

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getName()
       */
      public B name(String name) {
         this.name = name;
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getInfo()
       */
      public B info(String info) {
         this.info = info;
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getId()
       */
      public B id(String id) {
         this.id = id;
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getProductSections()
       */
      public B productSection(ProductSection productSection) {
         this.productSections.add(checkNotNull(productSection, "productSection"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getProductSections()
       */
      public B productSections(Iterable<ProductSection> productSections) {
         this.productSections = Sets.newLinkedHashSet(checkNotNull(productSections, "productSections"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getAdditionalSections()
       */
      public B additionalSection(SectionType additionalSection) {
         this.additionalSections.add(checkNotNull(additionalSection, "additionalSection"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseVirtualSystem#getAdditionalSections()
       */
      public B additionalSections(Iterable<? extends SectionType> additionalSections) {
         this.additionalSections = Sets.<SectionType>newLinkedHashSet(checkNotNull(additionalSections, "additionalSections"));
         return self();
      }

      public B fromBaseVirtualSystem(BaseVirtualSystem in) {
         return fromSectionType(in)
               .id(in.getId())
               .name(in.getName())
               .productSections(in.getProductSections())
               .additionalSections(in.getAdditionalSections());
      }
   }

   @XmlAttribute(namespace = OVF_NS)
   private String id;
   @XmlElement(name = "Name")
   private String name;
   @XmlElement(name = "Info")
   private String info;
   @XmlElement(name = "ProductSection")
   private Set<ProductSection> productSections;
   @XmlElementRef
   private Set<SectionType> additionalSections;

   protected BaseVirtualSystem(Builder<?> builder) {
      super(builder);
      this.id = builder.id;
      this.name = builder.name;
      this.info = builder.info;
      this.productSections = ImmutableSet.copyOf(checkNotNull(builder.productSections, "productSections"));
      this.additionalSections = ImmutableSet.copyOf(checkNotNull(builder.additionalSections, "additionalSections"));
   }

   protected BaseVirtualSystem() {
      // For JAXB      
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   /**
    * Specifies product-information for a package, such as product name and version, along with a
    * set of properties that can be configured
    */
   public Set<ProductSection> getProductSections() {
      return productSections;
   }

   public Set<SectionType> getAdditionalSections() {
      return additionalSections;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(super.hashCode(), id, name, productSections, additionalSections);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;

      BaseVirtualSystem other = (BaseVirtualSystem) obj;
      return super.equals(other) 
            && equal(id, other.id)
            && equal(name, other.name)
            && equal(productSections, other.productSections)
            && equal(additionalSections, other.additionalSections);
   }

   @Override
   protected MoreObjects.ToStringHelper string() {
      return super.string()
            .add("id", id)
            .add("name", name)
            .add("info", info)
            .add("productSections", productSections)
            .add("additionalSections", additionalSections);
   }
}
