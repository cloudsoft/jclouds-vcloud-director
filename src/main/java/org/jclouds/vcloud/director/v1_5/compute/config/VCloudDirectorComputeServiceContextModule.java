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
package org.jclouds.vcloud.director.v1_5.compute.config;

import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.functions.IdentityFunction;
import org.jclouds.location.Provider;
import org.jclouds.vcloud.director.v1_5.annotations.Login;
import org.jclouds.vcloud.director.v1_5.compute.functions.HardwareForVm;
import org.jclouds.vcloud.director.v1_5.compute.functions.ImageForVAppTemplate;
import org.jclouds.vcloud.director.v1_5.compute.functions.ImageStateForStatus;
import org.jclouds.vcloud.director.v1_5.compute.functions.NodemetadataStatusForStatus;
import org.jclouds.vcloud.director.v1_5.compute.functions.ValidateVAppTemplateAndReturnEnvelopeOrThrowIllegalArgumentException;
import org.jclouds.vcloud.director.v1_5.compute.functions.VdcToLocation;
import org.jclouds.vcloud.director.v1_5.compute.functions.VmToNodeMetadata;
import org.jclouds.vcloud.director.v1_5.compute.options.VCloudDirectorTemplateOptions;
import org.jclouds.vcloud.director.v1_5.compute.strategy.VCloudDirectorComputeServiceAdapter;
import org.jclouds.vcloud.director.v1_5.compute.suppliers.VirtualHardwareConfigSupplier;
import org.jclouds.vcloud.director.v1_5.domain.Entity;
import org.jclouds.vcloud.director.v1_5.domain.ResourceEntity;
import org.jclouds.vcloud.director.v1_5.domain.Session;
import org.jclouds.vcloud.director.v1_5.domain.SessionWithToken;
import org.jclouds.vcloud.director.v1_5.domain.Vdc;
import org.jclouds.vcloud.director.v1_5.domain.Vm;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.Envelope;
import org.jclouds.vcloud.director.v1_5.domain.query.QueryResultVAppTemplateRecord;
import org.jclouds.vcloud.director.v1_5.loaders.LoginUserInOrgWithPassword;
import org.jclouds.vcloud.director.v1_5.loaders.ResolveEntity;
import org.jclouds.vcloud.director.v1_5.loaders.ResolveEnvelope;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;

public class VCloudDirectorComputeServiceContextModule extends
        ComputeServiceAdapterContextModule<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc> {

   @SuppressWarnings("unchecked")
   @Override
   protected void configure() {
      super.configure();
      bind(new TypeLiteral<ComputeServiceAdapter<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc>>() {
      }).to(VCloudDirectorComputeServiceAdapter.class);
      bind(new TypeLiteral<Function<ResourceEntity.Status, NodeMetadata.Status>>() {
      }).to(NodemetadataStatusForStatus.class);
      bind(new TypeLiteral<Function<String, Image.Status>>() {
      }).to(ImageStateForStatus.class);
      bind(new TypeLiteral<Function<Vm, NodeMetadata>>() {
      }).to(VmToNodeMetadata.class);
      bind(new TypeLiteral<Function<QueryResultVAppTemplateRecord, org.jclouds.compute.domain.Image>>() {
      }).to(ImageForVAppTemplate.class);
      bind(new TypeLiteral<Function<URI, Envelope>>() {
      }).to(ValidateVAppTemplateAndReturnEnvelopeOrThrowIllegalArgumentException.class);
      bind(new TypeLiteral<Function<Hardware, Hardware>>() {
      }).to(Class.class.cast(IdentityFunction.class));
      bind(new TypeLiteral<Function<Vm, Hardware>>() {
      }).to(Class.class.cast(HardwareForVm.class));
      bind(new TypeLiteral<Function<Vdc, Location>>() {
      }).to(VdcToLocation.class);
      bind(TemplateOptions.class).to(VCloudDirectorTemplateOptions.class);
      bind(new TypeLiteral<Supplier<Set<Hardware>>>() {}).to(VirtualHardwareConfigSupplier.class);
      install(new LocationsFromComputeServiceAdapterModule<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc>() {
      });
   }

   @Provides
   @Login
   protected Supplier<URI> loginUrl(@Provider Supplier<URI> provider) {
      // TODO: technically, we should implement version api, but this will work
      return Suppliers.compose(new Function<URI, URI>() {

         @Override
         public URI apply(URI arg0) {
            return URI.create(arg0.toASCIIString() + "/sessions");
         }

      }, provider);
   }

   @Provides
   protected Supplier<Session> currentSession(Supplier<SessionWithToken> in) {
      return Suppliers.compose(new Function<SessionWithToken, Session>() {

         @Override
         public Session apply(SessionWithToken arg0) {
            return arg0.getSession();
         }

      }, in);

   }

   @Provides
   @Singleton
   @org.jclouds.vcloud.director.v1_5.annotations.Session
   protected Supplier<String> sessionToken(Supplier<SessionWithToken> in) {
      return Suppliers.compose(new Function<SessionWithToken, String>() {

         @Override
         public String apply(SessionWithToken arg0) {
            return arg0.getToken();
         }

      }, in);

   }

   @Provides
   @Singleton
   LoadingCache<String, Entity> resolveEntityCache(ResolveEntity loader, @Named(PROPERTY_SESSION_INTERVAL) int seconds) {
      return CacheBuilder.newBuilder().expireAfterWrite(seconds, TimeUnit.SECONDS).build(loader);
   }

   @Provides
   @Singleton
   LoadingCache<Credentials, SessionWithToken> provideSessionWithTokenCache(LoginUserInOrgWithPassword loader,
                                                                            @Named(PROPERTY_SESSION_INTERVAL) int seconds) {
      return CacheBuilder.newBuilder().expireAfterWrite(seconds, TimeUnit.SECONDS).build(loader);
   }

   // Temporary conversion of a cache to a supplier until there is a single-element cache
   // http://code.google.com/p/guava-libraries/issues/detail?id=872
   @Provides
   @Singleton
   protected Supplier<SessionWithToken> provideSessionWithTokenSupplier(
           final LoadingCache<Credentials, SessionWithToken> cache, @Provider final Supplier<Credentials> creds) {
      return new Supplier<SessionWithToken>() {
         @Override
         public SessionWithToken get() {
            return cache.getUnchecked(creds.get());
         }
      };
   }

   @Provides
   @Singleton
   LoadingCache<URI, Envelope> provideEnvelopeCache(ResolveEnvelope loader, @Named(PROPERTY_SESSION_INTERVAL) int seconds) {
      return CacheBuilder.newBuilder().expireAfterWrite(seconds, TimeUnit.SECONDS).build(loader);
   }

}
