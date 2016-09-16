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
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static org.jclouds.vcloud.director.v1_5.compute.util.VCloudDirectorComputeUtils.getCredentialsFrom;
import static org.jclouds.vcloud.director.v1_5.compute.util.VCloudDirectorComputeUtils.getIpsFromVm;
import static org.jclouds.vcloud.director.v1_5.compute.util.VCloudDirectorComputeUtils.toComputeOs;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.domain.Credentials;
import org.jclouds.logging.Logger;
import org.jclouds.util.InetAddresses2.IsPrivateIPAddress;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorApi;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.compute.util.VCloudDirectorComputeUtils;
import org.jclouds.vcloud.director.v1_5.domain.Link;
import org.jclouds.vcloud.director.v1_5.domain.ResourceEntity.Status;
import org.jclouds.vcloud.director.v1_5.domain.VApp;
import org.jclouds.vcloud.director.v1_5.domain.Vm;
import org.jclouds.vcloud.director.v1_5.predicates.LinkPredicates;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

@Singleton
public class VmToNodeMetadata implements Function<Vm, NodeMetadata> {
   @Resource
   protected static Logger logger = Logger.NULL;

   protected final FindLocationForResource findLocationForResourceInVDC;
   protected final Function<Vm, Hardware> hardwareForVm;
   protected final Function<Status, NodeMetadata.Status> vAppStatusToNodeStatus;
   protected final Map<String, Credentials> credentialStore;
   protected final GroupNamingConvention nodeNamingConvention;
   protected final VCloudDirectorApi api;

   @Inject
   protected VmToNodeMetadata(Function<Status, NodeMetadata.Status> vAppStatusToNodeStatus, Map<String, Credentials> credentialStore,
         FindLocationForResource findLocationForResourceInVDC, Function<Vm, Hardware> hardwareForVm,
         GroupNamingConvention.Factory namingConvention, VCloudDirectorApi api) {
      this.nodeNamingConvention = checkNotNull(namingConvention, "namingConvention").createWithoutPrefix();
      this.hardwareForVm = checkNotNull(hardwareForVm, "hardwareForVm");
      this.findLocationForResourceInVDC = checkNotNull(findLocationForResourceInVDC, "findLocationForResourceInVDC");
      this.credentialStore = checkNotNull(credentialStore, "credentialStore");
      this.vAppStatusToNodeStatus = checkNotNull(vAppStatusToNodeStatus, "vAppStatusToNodeStatus");
      this.api = api;
   }

   public NodeMetadata apply(Vm from) {
      NodeMetadataBuilder builder = new NodeMetadataBuilder();
      builder.ids(from.getId());
      builder.uri(from.getHref());
      builder.name(from.getName());
      builder.hostname(from.getName());
      builder.group(nodeNamingConvention.groupInUniqueNameOrNull(from.getName()));
      URI vAppRef = VCloudDirectorComputeUtils.getVAppParent(from);
      VApp vAppParent = api.getVAppApi().get(vAppRef);
      if (vAppParent == null) {
         // See https://github.com/cloudsoft/jclouds-vcloud-director/issues/35
         // Happens if the VM has been deleted between listing all vApps and us retrieving this vApp.
         logger.debug("Failed to convert VM %s to NodeMetadata; get for vApp %s returned null (presumably deleted?)", from, vAppRef);
         return null;
      }
      Optional<Link> linkOptional = Iterables.tryFind(vAppParent.getLinks(), LinkPredicates.typeEquals(VCloudDirectorMediaType.VDC));
      if (linkOptional.isPresent()) {
         builder.location(findLocationForResourceInVDC.apply(linkOptional.get()));
      }
      builder.operatingSystem(toComputeOs(vAppParent));
      builder.hardware(hardwareForVm.apply(from));
      builder.status(vAppStatusToNodeStatus.apply(from.getStatus()));
      Set<String> addresses = getIpsFromVm(from);
      builder.publicAddresses(filter(addresses, not(IsPrivateIPAddress.INSTANCE)));
      builder.privateAddresses(filter(addresses, IsPrivateIPAddress.INSTANCE));

      // normally, we don't affect the credential store when reading vApps.
      // However, login user, etc, is actually in the metadata, so lets see
      Credentials fromApi = getCredentialsFrom(from);
      if (fromApi != null && !credentialStore.containsKey("node#" + from.getHref().toASCIIString()))
         credentialStore.put("node#" + from.getHref().toASCIIString(), fromApi);
      return builder.build();
   }

}
