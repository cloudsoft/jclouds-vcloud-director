package org.jclouds.vcloud.director.v1_5.compute.strategy;

import static java.util.logging.Logger.getAnonymousLogger;
import static org.jclouds.Constants.PROPERTY_USER_THREADS;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.net.URI;
import java.util.List;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.internal.BaseComputeServiceContextLiveTest;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorApi;
import org.jclouds.vcloud.director.v1_5.domain.Link;
import org.jclouds.vcloud.director.v1_5.domain.Vm;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

@Test(groups = "live", testName = "VCloudDirectorComputeServiceLiveTest")
public class VCloudDirectorComputeServiceLiveTest extends BaseComputeServiceContextLiveTest {

   protected ComputeService client;
   protected Template template;

   public VCloudDirectorComputeServiceLiveTest() {
      provider = "vcloud-director";
   }

   @Override
   protected Module getSshModule() {
      return new SshjSshClientModule();
   }

   @Override
   protected void initializeContext() {
      super.initializeContext();
      client = view.getComputeService();
   }

   public void testConcurrentlyDestroyNodes() throws Exception {

      final String group = "twins";
      List<NodeMetadata> nodes = Lists.newArrayList();
      List<ListenableFuture<?>> futures = Lists.newArrayList();
      ListeningExecutorService userExecutor = context.utils().injector()
              .getInstance(Key.get(ListeningExecutorService.class, Names.named(PROPERTY_USER_THREADS)));

      try {
         template = buildTemplate(client.templateBuilder().imageNameMatches("cloudsoft-template"));
         template.getOptions().inboundPorts(22);
         nodes.addAll(client.createNodesInGroup(group, 2, template));
      } finally {
         for (final NodeMetadata node : nodes) {
            ListenableFuture<?> future = userExecutor.submit(new Runnable() {
               @Override
               public void run() {
                  getAnonymousLogger().info("Destroying node " + node.getId());
                  client.destroyNode(node.getId());
               }
            });
            futures.add(future);
         }
         Futures.allAsList(futures).get();
         assertTrue(client.listNodesDetailsMatching(new Predicate<ComputeMetadata>() {
            @Override
            public boolean apply(ComputeMetadata input) {
               final String nodeMetadataGroup = ((NodeMetadata) input).getGroup();
               return nodeMetadataGroup != null && nodeMetadataGroup.equals(group);
            }
         }).isEmpty());
      }
   }

   public void testDestroyNodeAndItsVApp() throws Exception {

      final String group = "destroy-node-and-vapp";

      template = buildTemplate(client.templateBuilder().imageNameMatches("cloudsoft-template"));
      template.getOptions().inboundPorts(22);
      final NodeMetadata node = Iterables.getOnlyElement(client.createNodesInGroup(group, 1, template));

      final URI vmRef = node.getUri();
      Vm vm = client.getContext().unwrapApi(VCloudDirectorApi.class).getVmApi().get(vmRef);
      Optional<Link> optionalLink = Iterables.tryFind(vm.getLinks(), new Predicate<Link>() {
         @Override
         public boolean apply(Link link) {
            return link.getRel() != null && link.getRel() == Link.Rel.UP;
         }
      });
      if (!optionalLink.isPresent()) {
         Assert.fail("Cannot find the vAppRef that contains the vm with id(" + node.getId() + ")");
      }
      final URI vAppRef = optionalLink.get().getHref();

      client.destroyNode(node.getId());
      assertNull(client.getNodeMetadata(node.getId()));
      assertNull(client.getContext().unwrapApi(VCloudDirectorApi.class).getVAppApi().get(vAppRef));
      assertNull(client.getContext().unwrapApi(VCloudDirectorApi.class).getVmApi().get(vmRef));
   }

   protected Template buildTemplate(TemplateBuilder templateBuilder) {
      return templateBuilder.build();
   }

}