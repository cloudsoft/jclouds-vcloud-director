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
package org.jclouds.vcloud.director.v1_5.domain.network;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@XmlRootElement(name = "IpScopes")
@XmlAccessorType(XmlAccessType.FIELD)
public class IpScopes {

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromIpScopes(this);
   }

   public static class Builder {
      private List<IpScope> ipScopes = Lists.newArrayList();

      public Builder ipScopes(List<IpScope> ipScopes) {
         this.ipScopes = Lists.newArrayList(checkNotNull(ipScopes, "ipScopes"));
         return this;
      }

      public Builder ipScope(IpScope ipScope) {
         ipScopes.add(checkNotNull(ipScope, "ipScope"));
         return this;
      }

      public IpScopes build() {
         return new IpScopes(ipScopes);
      }

      public Builder fromIpScopes(IpScopes in) {
         return ipScopes(in.getIpsScopes());
      }
   }

   @XmlElement(name = "IpScope")
   private List<IpScope> ipsScopes = Lists.newArrayList();

   public List<IpScope> getIpsScopes() {
      return Collections.unmodifiableList(ipsScopes);
   }

   IpScopes() {
      // For JAXB
   }

   public IpScopes(List<IpScope> ipScopes) {
      this.ipsScopes = ImmutableList.copyOf(ipScopes);
   }
}
