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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.emptyToNull;

import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Named;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.internal.BaseComputeServiceContextLiveTest;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.logging.Logger;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.jclouds.vcloud.director.v1_5.compute.options.VCloudDirectorTemplateOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

@Test(groups = "live", testName = "VCloudDirectorComputeServiceContextLiveTest")
public class VCloudDirectorComputeServiceContextLiveTest extends BaseComputeServiceContextLiveTest {

   protected String vAppTemplateUrn;
   protected String vAppTemplateNameRegex;
   protected String vdcUrn;
   protected String networkUrn;
   protected String networkName;

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   public VCloudDirectorComputeServiceContextLiveTest() {
      provider = "vcloud-director";
   }

   @BeforeClass(groups = { "integration", "live" })
   public void initTestParametersFromProperties() {
      vAppTemplateUrn = emptyToNull(System.getProperty("test." + provider + ".vapptemplate-id"));
      vAppTemplateNameRegex = emptyToNull(System.getProperty("test." + provider + ".vapptemplate-name-regex"));
      vdcUrn = emptyToNull(System.getProperty("test." + provider + ".vdc-id"));
      networkUrn = emptyToNull(System.getProperty("test." + provider + ".network-id"));
      networkName = emptyToNull(System.getProperty("test." + provider + ".network-name"));
      checkArgument(vAppTemplateUrn == null || vAppTemplateNameRegex == null, 
            "Must not specify id %s and name %s for template", vAppTemplateUrn, vAppTemplateNameRegex);
      checkArgument(networkUrn == null || networkName == null, 
            "Must not specify id %s and name %s for network", networkUrn, networkName);
   }
   
   @Test
   public void testLaunchNode() throws RunNodesException {
      final String name = "node";
      ComputeServiceContext context = ContextBuilder.newBuilder(provider)
              .credentials(identity, credential)
              .endpoint(endpoint)
              .modules(ImmutableSet.of(new SLF4JLoggingModule(),
                      new SshjSshClientModule()))
              .build(ComputeServiceContext.class);

      TemplateBuilder templateBuilder = context.getComputeService().templateBuilder();

      if (vdcUrn != null) {
         templateBuilder.locationId(vdcUrn);
      }
      if (vAppTemplateUrn != null) {
         templateBuilder.imageId(vAppTemplateUrn);
      } else if (vAppTemplateNameRegex != null) {
         templateBuilder.imageNameMatches(vAppTemplateNameRegex);
      }
      
      Template template = templateBuilder
              .minCores(4)
              .minRam(2048)
              .build();
      // test passing custom options
      VCloudDirectorTemplateOptions options = template.getOptions().as(VCloudDirectorTemplateOptions.class);

      if (networkName != null) {
         options.networks(networkName);
      } else {
         options.networks("Operational_Network_01");
      }
      
      NodeMetadata node = null;
      try {
         Set<? extends NodeMetadata> nodes = context.getComputeService().createNodesInGroup(name, 1, template);
         node = Iterables.getOnlyElement(nodes);
//         logger.debug("Created Node: %s", node);
//         SshClient client = context.utils().sshForNode().apply(node);
//         client.connect();
//         ExecResponse hello = client.exec("mount");
//         logger.debug(hello.getOutput().trim());
      } finally {
         if (node != null) {
            context.getComputeService().destroyNode(node.getId());
         }
      }
   }

}
