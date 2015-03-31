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
package org.jclouds.vcloud.director.v1_5.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;
import org.jclouds.vcloud.director.v1_5.domain.Vdc;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

/**
 * Transforms an {@link Vdc} to the jclouds portable model.
 */
@Singleton
public class VdcToLocation implements Function<Vdc, Location> {

   private final JustProvider justProvider;

   @Inject
   VdcToLocation(JustProvider justProvider) {
      this.justProvider = checkNotNull(justProvider, "justProvider cannot be null");
   }

   @Override
   public Location apply(Vdc input) {
      LocationBuilder builder = new LocationBuilder();
      builder.id(input.getHref().toASCIIString());
      builder.description(format("%s:%s", input.getName(), input.getType()));
      builder.scope(LocationScope.PROVIDER);
      //builder.parent(getOnlyElement(justProvider.get()));
      builder.iso3166Codes(ImmutableSet.<String> of());
      return builder.build();
   }
}
