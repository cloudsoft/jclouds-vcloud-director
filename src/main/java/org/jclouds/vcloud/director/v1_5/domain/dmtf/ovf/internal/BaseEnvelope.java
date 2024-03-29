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
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.DiskSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.NetworkSection;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.SectionType;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public abstract class BaseEnvelope<V extends BaseVirtualSystem, E extends BaseEnvelope<V, E>> {
   
   public abstract Builder<?, V, E> toBuilder();

   public abstract static class Builder<B extends Builder<B, V, E>, V extends BaseVirtualSystem, E extends BaseEnvelope<V, E>> {

      protected Set<DiskSection> diskSections = Sets.newLinkedHashSet();
      protected Set<NetworkSection> networkSections = Sets.newLinkedHashSet();
      protected Set<SectionType> additionalSections = Sets.newLinkedHashSet();
      protected V virtualSystem;

      @SuppressWarnings("unchecked")
      protected B self() {
         return (B) this;
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getDiskSections
       */
      public B diskSection(DiskSection diskSection) {
         this.diskSections.add(checkNotNull(diskSection, "diskSection"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getDiskSections
       */
      public B diskSections(Iterable<? extends DiskSection> diskSections) {
         this.diskSections = ImmutableSet.<DiskSection> copyOf(checkNotNull(diskSections, "diskSections"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getNetworkSections
       */
      public B networkSection(NetworkSection networkSection) {
         this.networkSections.add(checkNotNull(networkSection, "networkSection"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getNetworkSections
       */
      public B networkSections(Iterable<? extends NetworkSection> networkSections) {
         this.networkSections = ImmutableSet.<NetworkSection> copyOf(checkNotNull(networkSections, "networkSections"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getAdditionalSections
       */
      public B additionalSection(SectionType additionalSection) {
         this.additionalSections.add(checkNotNull(additionalSection, "additionalSection"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getAdditionalSections
       */
      public B additionalSections(Iterable<? extends SectionType> additionalSections) {
         this.additionalSections = ImmutableSet.<SectionType> copyOf(checkNotNull(additionalSections, "additionalSections"));
         return self();
      }

      /**
       * @see org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf.internal.BaseEnvelope#getVirtualSystem
       */
      public B virtualSystem(V virtualSystem) {
         this.virtualSystem = virtualSystem;
         return self();
      }

      public abstract E build();

      public B fromEnvelope(BaseEnvelope<V, E> in) {
         return virtualSystem(in.getVirtualSystem())
               .diskSections(in.getDiskSections())
               .networkSections(networkSections)
               .additionalSections(in.getAdditionalSections());
      }

   }

   private Set<DiskSection> diskSections;
   private Set<NetworkSection> networkSections;
   private Set<SectionType> additionalSections;
   private V virtualSystem;

   protected BaseEnvelope(Builder<?, V, E> builder) {
      this.diskSections = ImmutableSet.copyOf(checkNotNull(builder.diskSections, "diskSections"));
      this.networkSections = ImmutableSet.copyOf(checkNotNull(builder.networkSections, "networkSections"));
      this.additionalSections = ImmutableSet.copyOf(checkNotNull(builder.additionalSections, "additionalSections"));
      this.virtualSystem = checkNotNull(builder.virtualSystem, "virtualSystem");
   }
   
   protected BaseEnvelope() {
      // for JAXB
   }

   public V getVirtualSystem() {
      return virtualSystem;
   }

   public Set<DiskSection> getDiskSections() {
      return diskSections;
   }
   
   public Set<NetworkSection> getNetworkSections() {
      return networkSections;
   }

   public Set<SectionType> getAdditionalSections() {
      return additionalSections;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(additionalSections, diskSections, networkSections, virtualSystem);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;

      BaseEnvelope<?, ?> other = (BaseEnvelope<?, ?>) obj;
      return Objects.equal(additionalSections, other.additionalSections)
            && Objects.equal(diskSections, other.diskSections)
            && Objects.equal(networkSections, other.networkSections)
            && Objects.equal(virtualSystem, other.virtualSystem);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   protected MoreObjects.ToStringHelper string() {
      return MoreObjects.toStringHelper("")
            .add("diskSections", diskSections)
            .add("networkSections", networkSections)
            .add("additionalSections", additionalSections)
            .add("virtualSystem", virtualSystem);
   }
}
