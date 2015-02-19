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
package org.jclouds.vcloud.director.v1_5.domain.dmtf;

import static org.testng.Assert.assertNotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;

@Test(groups = "unit", testName = "EnvelopeTest")
public class EnvelopeTest {

   private JAXBContext jc;
   @BeforeClass
   protected void setup() throws JAXBException {
      jc = JAXBContext.newInstance(Envelope.class);
      assertNotNull(jc);
   }

   public void testUnmarshallEnvelope() throws JAXBException {
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
      Envelope envelope = (Envelope) unmarshaller.unmarshal(Resources.getResource("dmtf/envelope.xml"));
      assertNotNull(envelope);
   }

}