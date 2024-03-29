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
package org.jclouds.vcloud.director.v1_5.predicates;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.Link;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Predicates handy when working with Links
 */

public class LinkPredicates {
   
   /**
    * matches links of the given relation
    * 
    * @param rel from {@code context.getApi().getCurrentSession().get().getLinks()}
    * @return predicate that will match links of the given rel
    */
   public static Predicate<Link> relEquals(final String rel) {
      checkNotNull(rel, "rel must be defined");

      return relEquals(Link.Rel.fromValue(rel));
   }

   /** @see #relEquals(String) */
   public static Predicate<Link> relEquals(final Link.Rel rel) {
      return LINK_REL_SELECTORS.getUnchecked(checkNotNull(rel, "rel must be defined"));
   }
   
   private static final LoadingCache<Link.Rel, Predicate<Link>> LINK_REL_SELECTORS = CacheBuilder.newBuilder()
      .maximumSize(Link.Rel.ALL.size())
      .build(
         new CacheLoader<Link.Rel, Predicate<Link>>() {
            @Override
            public Predicate<Link> load(final Link.Rel rel) {
               return new Predicate<Link>() {
                  @Override
                  public boolean apply(Link link) {
                     return rel == link.getRel();
                  }
                  
                  @Override
                  public String toString() {
                     return "relEquals(" + rel.value() + ")";
                  }
               };
            }
         });

   /**
    * @see ReferenceTypePredicates#nameEquals
    */
   public static Predicate<Link> nameEquals(String name) {
      return MEDIA_NAME_SELECTORS.getUnchecked(name);
   }
   
   private static final LoadingCache<String, Predicate<Link>> MEDIA_NAME_SELECTORS = CacheBuilder.newBuilder()
      .maximumSize(VCloudDirectorMediaType.ALL.size())
      .build(
         new CacheLoader<String, Predicate<Link>>() {
            @Override
            public Predicate<Link> load(String key) {
               return ReferencePredicates.nameEquals(key);
            }
         });

   /**
    * @see ReferenceTypePredicates#typeEquals
    */
   public static Predicate<Link> typeEquals(String type) {
      return MEDIA_TYPE_SELECTORS.getUnchecked(type);
   }
   
   private static final LoadingCache<String, Predicate<Link>> MEDIA_TYPE_SELECTORS = CacheBuilder.newBuilder()
      .maximumSize(VCloudDirectorMediaType.ALL.size())
      .build(
         new CacheLoader<String, Predicate<Link>>() {
            @Override
            public Predicate<Link> load(String key) {
               return ReferencePredicates.typeEquals(key);
            }
         });
}
