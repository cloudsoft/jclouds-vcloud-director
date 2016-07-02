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

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.jclouds.providers.AnonymousProviderMetadata.forApiOnEndpoint;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.jclouds.ContextBuilder;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.IntegrationTestClient;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.rest.internal.BaseRestApiTest.MockModule;
import org.jclouds.vcloud.director.v1_5.domain.VcloudDirectorError;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.google.inject.Module;

@Test(singleThreaded = true, groups = "unit", testName = "VcloudDirectorUtilsTest")
public class VcloudDirectorUtilsTest {
   
   private static final String EXPECTED_CONTENT_TYPE = "application/vnd.vmware.vcloud.error+xml;version=1.5";
   
   public static Injector injector(Credentials creds) {
      return ContextBuilder
            .newBuilder(forApiOnEndpoint(IntegrationTestClient.class, "http://localhost"))
            .credentialsSupplier(Suppliers.<Credentials> ofInstance(creds)).apiVersion("apiVersion")
            .modules(ImmutableList.<Module> of(new MockModule(), new NullLoggingModule())).buildInjector();
   }

   VcloudDirectorUtils utils = null;
   private HttpCommand command;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      utils = injector(new Credentials("identity", "credential")).getInstance(VcloudDirectorUtils.class);
      
      command = createMock(HttpCommand.class);
      HttpRequest httpRequest = createMock(HttpRequest.class);
      expect(httpRequest.getRequestLine()).andReturn("whatever").anyTimes();
      expect(command.getCurrentRequest()).andReturn(httpRequest).atLeastOnce();
      replay(httpRequest);
      replay(command);
   }

   @AfterTest
   protected void tearDownInjector() {
      utils = null;
   }

   HttpResponse response(InputStream content) {
      HttpResponse response = HttpResponse.builder().statusCode(BAD_REQUEST.getStatusCode())
               .message("boa")
               .payload(content)
               .addHeader("x-amz-request-id", "requestid")
               .addHeader("x-amz-id-2", "requesttoken").build();
      response.getPayload().getContentMetadata().setContentType(EXPECTED_CONTENT_TYPE);
      return response;
   }
   
   /**
    * HEAD requests don't have a payload
    */
   @Test
   public void testNoExceptionWhenNoPayload() {
      HttpResponse response = HttpResponse.builder().statusCode(BAD_REQUEST.getStatusCode()).build();
      assertNull(utils.parseVcloudDirectorErrorFromContent(command.getCurrentRequest(), response));
   }
   
   /**
    * clones or proxies can mess up the error message.
    */
   @Test
   public void testNoExceptionParsingBadResponse() {
      HttpResponse response = HttpResponse.builder().statusCode(BAD_REQUEST.getStatusCode()).payload("foo bar").build();
      response.getPayload().getContentMetadata().setContentType(EXPECTED_CONTENT_TYPE);
      assertNull(utils.parseVcloudDirectorErrorFromContent(command.getCurrentRequest(), response));
   }

   @Test
   public void testParseVcloudDirectorErrorFromContentHttpCommandHttpResponseInputStream() {
      VcloudDirectorError error = utils.parseVcloudDirectorErrorFromContent(command.getCurrentRequest(), response(getClass()
            .getResourceAsStream("/error400-operationLimitsExceeded.xml")));
      assertEquals(error.getMajorErrorCode(), "400");
      assertEquals(error.getMessage(), "The maximum number of simultaneous operations for user \"myname\" on organization \"my-org\" has been reached.");
      assertEquals(error.getMinorErrorCode(), "OPERATION_LIMITS_EXCEEDED");
   }
}
