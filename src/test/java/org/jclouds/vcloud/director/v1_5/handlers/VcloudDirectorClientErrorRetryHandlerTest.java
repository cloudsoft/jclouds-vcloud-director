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
package org.jclouds.vcloud.director.v1_5.handlers;

import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.easymock.IAnswer;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.jclouds.vcloud.director.v1_5.domain.VcloudDirectorError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Tests behavior of {@code VcloudDirectorClientErrorRetryHandler}
 */
@Test(groups = "unit", testName = "VcloudDirectorClientErrorRetryHandlerTest")
public class VcloudDirectorClientErrorRetryHandlerTest {
   @Test
   public void testUnknown400DoesNotRetry() {
      VcloudDirectorUtils utils = createMock(VcloudDirectorUtils.class);
      HttpCommand command = createMock(HttpCommand.class);

      replay(utils, command);

      VcloudDirectorClientErrorRetryHandler retry = new VcloudDirectorClientErrorRetryHandler(utils,
            ImmutableSet.<String> of());

      assertFalse(retry.shouldRetryRequest(command, HttpResponse.builder().statusCode(BAD_REQUEST.getStatusCode()).build()));

      verify(utils, command);

   }

   @DataProvider(name = "codes")
   public Object[][] createData() {
      return new Object[][] {
              { BAD_REQUEST.getStatusCode(), "OPERATION_LIMITS_EXCEEDED" },
      };
   }

   @Test(dataProvider = "codes")
   public void testDoesBackoffAndRetryForHttpStatusCodeAndErrorCode(int httpStatusCode, String errorCode) {
      VcloudDirectorUtils utils = createMock(VcloudDirectorUtils.class);
      HttpCommand command = createMock(HttpCommand.class);
      
      HttpRequest req = HttpRequest.builder().method(POST)
            .endpoint("https://acme.com/api/vdc/dcd952e3-6f07-42dd-b142-fc94b0a55062/action/composeVApp").build();

      String responsePayload = String.format(
            "<Error xmlns=\"http://www.vmware.com/vcloud/v1.5\"" 
                  + " minorErrorCode=\"OPERATION_LIMITS_EXCEEDED\"" 
                  + " message=\"The maximum number of simultaneous operations for user &quot;myname&quot; on organization &quot;my-org&quot; has been reached.\"" 
                  + " majorErrorCode=\"400\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                  + " xsi:schemaLocation=\"http://www.vmware.com/vcloud/v1.5 http://acme.com/api/v1.5/schema/master.xsd\">"
                  + "</Error>",
            errorCode);

      HttpResponse response = HttpResponse.builder().statusCode(httpStatusCode)
            .payload(Payloads.newStringPayload(responsePayload)).build();

      expect(command.getCurrentRequest()).andReturn(req);
      final AtomicInteger counter = new AtomicInteger();
      expect(command.incrementFailureCount()).andAnswer(new IAnswer<Integer>() {
         @Override
         public Integer answer() throws Throwable {
            return counter.incrementAndGet();
         }
      }).anyTimes();
      expect(command.isReplayable()).andReturn(true).anyTimes();
      expect(command.getFailureCount()).andAnswer(new IAnswer<Integer>() {
         @Override
         public Integer answer() throws Throwable {
            return counter.get();
         }
      }).anyTimes();

      VcloudDirectorError error = new VcloudDirectorError();
      error.setMinorErrorCode(errorCode);

      expect(utils.parseVcloudDirectorErrorFromContent(req, response)).andReturn(error);

      replay(utils, command);

      VcloudDirectorClientErrorRetryHandler retry = new VcloudDirectorClientErrorRetryHandler(utils,
            ImmutableSet.of("OPERATION_LIMITS_EXCEEDED"));

      assert retry.shouldRetryRequest(command, response);

      verify(utils, command);
   }
}
