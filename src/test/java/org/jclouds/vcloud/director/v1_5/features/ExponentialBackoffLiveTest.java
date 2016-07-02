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
package org.jclouds.vcloud.director.v1_5.features;

import static org.jclouds.util.Predicates2.retry;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorLiveTestConstants.TASK_COMPLETE_TIMELY;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorLiveTestConstants.URN_REQ_LIVE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.VApp;
import org.jclouds.vcloud.director.v1_5.domain.params.ComposeVAppParams;
import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorApiLiveTest;
import org.jclouds.vcloud.director.v1_5.predicates.TaskSuccess;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Attempts to induce rate-limiting by firing many concurrent requests.
 */
@Test(groups = { "live", "user" }, singleThreaded = true, testName = "VdcApiLiveTest")
public class ExponentialBackoffLiveTest extends BaseVCloudDirectorApiLiveTest {

   public static final String VDC = "vdc";

   /*
    * Convenience reference to API api.
    */
   protected VdcApi vdcApi;

   private ListeningExecutorService executor;
   
   @Override
   @BeforeClass(alwaysRun = true)
   public void setupRequiredApis() {
      vdcApi = api.getVdcApi();

      assertNotNull(vdcUrn, String.format(URN_REQ_LIVE, VDC));
      
      executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
   }

   @AfterClass(alwaysRun = true)
   public void cleanUp() throws Exception {
      if (executor != null) {
         executor.shutdownNow();
      }
   }

   // These repeated GET requests don't seem to induce rate limit
   @Test(description = "GET /vdc/{id} many times concurrently")
   public void testGetVdcConcurrently() throws InterruptedException, ExecutionException {
      final int numRuns = 100;
      List<ListenableFuture<?>> futures = Lists.newArrayList();
      for (int i = 0; i < numRuns; i++) {
         futures.add(executor.submit(new Runnable() {
            public void run() {
               api.getVdcApi().get(vdcUrn);
            }}));
      }
      Futures.successfulAsList(futures).get(); // wait for all, so that we clean up correctly
      Futures.allAsList(futures).get(); // fail if any of them failed
   }

   /**
    * To (manually) confirm that this really did cause rate-limiting, expect to see in the log things like:
    *   << "<Error xmlns="http://www.vmware.com/vcloud/v1.5" minorErrorCode="OPERATION_LIMITS_EXCEEDED" message="[ c5dfea46-f54f-42d0-95ba-77627424ace0 ] The maximum number of simultaneous operations for user &quot;cloudsoft&quot; on organization &quot;ctd-emea01&quot; has been reached." majorErrorCode="400" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.vmware.com/vcloud/v1.5 http://acme.com/api/v1.5/schema/master.xsd"></Error>[\n]"
    * and logging from BackoffLimitedRetryHandler.imposeBackoffExponentialDelay, like: 
    *   DEBUG Retry 1/6: delaying for 103 ms: server error: [method=org.jclouds.vcloud.director.v1_5.features.VAppApi.public abstract org.jclouds.vcloud.director.v1_5.domain.Task org.jclouds.vcloud.director.v1_5.features.VAppApi.remove(java.lang.String)[urn:vcloud:vapp:4208b121-a0aa-431a-a0f1-35d26bc10236], request=DELETE https://acme.com/api/vApp/vapp-4208b121-a0aa-431a-a0f1-35d26bc10236 HTTP/1.1]
    * 
    * If rate-limiting is not happening, increase the numRuns.
    */
   @Test(description = "POST /vdc/{id}/action/composeVApp many times concurrently")
   public void testComposeVAppsConcurrently() throws Exception {
      final int numRuns = 10;
      List<ListenableFuture<?>> futures = Lists.newArrayList();
      for (int i = 0; i < numRuns; i++) {
         futures.add(executor.submit(new Callable<Object>() {
            public Object call() {
               String name = name("composed-");

               VApp composedVApp = vdcApi.composeVApp(vdcUrn, ComposeVAppParams.builder().name(name)
                        .build());
               try {
                  Task task = Iterables.getFirst(composedVApp.getTasks(), null);
                  assertNotNull(task, "vdcApi.composeVApp returned VApp that did not contain any tasks");
                  assertTaskSucceedsLong(task);
               } finally {
                  cleanUpVApp(composedVApp, true);
               }
               return true;
            }}));
      }
      Futures.successfulAsList(futures).get(); // wait for all, so that we clean up correctly
      Futures.allAsList(futures).get(); // fail if any of them failed
   }

   // TODO super.assertTaskSucceedsLong failed; is retryTaskSuccessLong not being injected?
   @Override
   protected void assertTaskSucceedsLong(Task task) {
      final TaskApi taskApi = api.getTaskApi();
      final Predicate<Task> taskSuccess = new TaskSuccess(taskApi);
      final Predicate<Task> retryTaskSuccessLong = retry(taskSuccess, LONG_TASK_TIMEOUT_SECONDS * 1000L);
      assertTrue(retryTaskSuccessLong.apply(task), String.format(TASK_COMPLETE_TIMELY, task));
   }
}
