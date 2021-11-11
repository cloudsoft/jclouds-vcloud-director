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
import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import java.util.Spliterator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.RasdItem;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Represents a list of RASD items.
 *
 * <pre>
 * &lt;complexType name="RasdItemsList" /&gt;
 * </pre>
 */
@XmlRootElement(name = "RasdItemsList")
@XmlType(name = "RasdItemsList")
public class RasdItemsList extends ForwardingList<RasdItem> implements Set<RasdItem> {
   @XmlAttribute
   private URI href;
   @XmlAttribute
   private String type;
   @XmlElement(name = "Link")
   private Set<Link> links = Sets.newLinkedHashSet();

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromRasdItemsList(this);
   }


   public static class Builder {
      private List<RasdItem> items = Lists.newLinkedList();

      /**
       * @see RasdItemsList#getItems()
       */
      public Builder items(List<RasdItem> items) {
         this.items = checkNotNull(items, "items");
         return this;
      }

      /**
       * @see RasdItemsList#getItems()
       */
      public Builder item(RasdItem item) {
         this.items.add(checkNotNull(item, "item"));
         return this;
      }

      public Builder item(int index, RasdItem item) {
          this.items.add(index, item);
          return this;
      }

      public Builder fromRasdItemsList(RasdItemsList in) {
         return fromResource(in).items(in.getItems());
      }

      private URI href;
      private String type;
      private Set<Link> links;

      public Builder href(URI href) {
         this.href = href;
         return this;
      }

      public Builder type(String type) {
         this.type = type;
         return this;
      }

      public Builder links(Set<Link> links) {
         this.links = Sets.newLinkedHashSet(checkNotNull(links, "links"));
         return this;
      }

      public Builder link(Link link) {
         if (links == null)
            links = Sets.newLinkedHashSet();
         this.links.add(checkNotNull(link, "link"));
         return this;
      }

      public RasdItemsList build() {
         return new RasdItemsList(this);
      }

      protected Builder fromResource(RasdItemsList in) {
         return href(in.getHref()).type(in.getType()).links(Sets.newLinkedHashSet(in.getLinks()));
      }
  }
   protected RasdItemsList() {
      // For JAXB and B use
   }

   protected RasdItemsList(Builder builder) {
      this.href = builder.href;
      this.type = builder.type;
      this.links = builder.links == null ? ImmutableSet.<Link>of() : builder.links;
      this.items = builder.items;
   }

   @XmlElement(name = "Item")
   protected List<RasdItem> items = Lists.newLinkedList();

   /**
    * A RASD item content.
    */
   public List<RasdItem> getItems() {
      return items;
   }

   @Override
   protected List<RasdItem> delegate() {
      return getItems();
   }

   /**
    * Contains the URI to the entity.
    *
    * An object reference, expressed in URL format. Because this URL includes the object identifier
    * portion of the id attribute value, it uniquely identifies the object, persists for the life of
    * the object, and is never reused. The value of the href attribute is a reference to a view of
    * the object, and can be used to access a representation of the object that is valid in a
    * particular context. Although URLs have a well-known syntax and a well-understood
    * interpretation, a api should treat each href as an opaque string. The rules that govern how
    * the server constructs href strings might change in future releases.
    *
    * @return an opaque reference and should never be parsed
    */
   public URI getHref() {
      return href;
   }

   /**
    * Contains the type of the the entity.
    *
    * The object type, specified as a MIME content type, of the object that the link references.
    * This attribute is present only for links to objects. It is not present for links to actions.
    *
    * @return type definition, type, expressed as an HTTP Content-Type
    */
   public String getType() {
      return type;
   }

   /**
    * Set of optional links to an entity or operation associated with this object.
    */
   public Set<Link> getLinks() {
      return links == null ? ImmutableSet.<Link>of() : Collections.unmodifiableSet(links);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      RasdItemsList that = RasdItemsList.class.cast(o);
      return super.equals(that) && equal(this.items, that.items);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(super.hashCode(), items);
   }

   public ToStringHelper string() {
      return MoreObjects.toStringHelper("").add("href", href).add("links", links).add("type", type).add("items", items);
   }

    @Override
    public Spliterator<RasdItem> spliterator() {
        return super.spliterator();
    }
}
