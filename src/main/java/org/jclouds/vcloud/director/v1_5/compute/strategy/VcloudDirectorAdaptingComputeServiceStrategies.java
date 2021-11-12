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
package org.jclouds.vcloud.director.v1_5.compute.strategy;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.annotation.Resource;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule.AddDefaultCredentialsToImage;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.compute.strategy.PrioritizeCredentialsFromTemplate;
import org.jclouds.compute.strategy.impl.AdaptingComputeServiceStrategies;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.director.v1_5.domain.Vdc;
import org.jclouds.vcloud.director.v1_5.domain.Vm;
import org.jclouds.vcloud.director.v1_5.domain.query.QueryResultVAppTemplateRecord;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Identical to {@link AdaptingComputeServiceStrategies}, except listNodes will automatically 
 * filter out any null values returned by the nodeMetadataAdapter. The addLoginCredentials 
 * thus also guards against null input.
 */
public class VcloudDirectorAdaptingComputeServiceStrategies extends AdaptingComputeServiceStrategies<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc> {

   // TODO Longer term, fix this in core jclouds and delete when that is available in a stable release.
   
   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final Map<String, Credentials> credentialStore;
   private final ComputeServiceAdapter<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc> client;
   private final Function<Vm, NodeMetadata> nodeMetadataAdapter;

   @Inject
   public VcloudDirectorAdaptingComputeServiceStrategies(Map<String, Credentials> credentialStore,
            PrioritizeCredentialsFromTemplate prioritizeCredentialsFromTemplate,
            ComputeServiceAdapter<Vm, Hardware, QueryResultVAppTemplateRecord, Vdc> client, Function<Vm, NodeMetadata> nodeMetadataAdapter,
            Function<QueryResultVAppTemplateRecord, Image> imageAdapter,
            AddDefaultCredentialsToImage addDefaultCredentialsToImage) {
      super(credentialStore, prioritizeCredentialsFromTemplate, client, nodeMetadataAdapter, imageAdapter, addDefaultCredentialsToImage);
      this.credentialStore = checkNotNull(credentialStore, "credentialStore");
      this.client = client;
      this.nodeMetadataAdapter = Functions.compose(addLoginCredentials, checkNotNull(nodeMetadataAdapter,
               "nodeMetadataAdapter"));
   }
   
   private final Function<NodeMetadata, NodeMetadata> addLoginCredentials = new Function<NodeMetadata, NodeMetadata>() {

      @Override
      public NodeMetadata apply(NodeMetadata arg0) {
         if (arg0 == null) return null;
         return credentialStore.containsKey("node#" + arg0.getId()) ? NodeMetadataBuilder.fromNodeMetadata(arg0)
                  .credentials(LoginCredentials.fromCredentials(credentialStore.get("node#" + arg0.getId()))).build()
                  : arg0;
      }

      @Override
      public String toString() {
         return "addLoginCredentialsFromCredentialStore()";
      }
   };

   @Override
   public Iterable<? extends NodeMetadata> listDetailsOnNodesMatching(Predicate<? super NodeMetadata> filter) {
      return FluentIterable.from(client.listNodes())
               .transform(nodeMetadataAdapter)
               .filter(Predicates.notNull())
               .filter(filter);
   }
}
