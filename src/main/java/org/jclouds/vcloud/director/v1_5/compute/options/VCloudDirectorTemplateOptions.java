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
package org.jclouds.vcloud.director.v1_5.compute.options;

import static com.google.common.base.Objects.equal;
import java.util.Map;

import com.google.common.base.MoreObjects;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.domain.Statements;

import com.google.common.base.Objects;

public class VCloudDirectorTemplateOptions extends TemplateOptions implements Cloneable {

   protected Statement guestCustomizationScript;
   protected Integer memory;
   protected Integer virtualCpus;
   protected Integer disk;

   /**
    * Specifies a script to be added to the GuestCustomizationSection
    */
   public VCloudDirectorTemplateOptions guestCustomizationScript(@Nullable String guestCustomizationScript) {
      return guestCustomizationScript(Statements.exec(guestCustomizationScript));
   }

   /**
    * Specifies a script to be added to the GuestCustomizationSection
    */
   public VCloudDirectorTemplateOptions guestCustomizationScript(@Nullable Statement guestCustomizationScript) {
      this.guestCustomizationScript = guestCustomizationScript;
      return this;
   }

   public VCloudDirectorTemplateOptions memory(@Nullable Integer memory) {
      this.memory = memory;
      return this;
   }

   public VCloudDirectorTemplateOptions virtualCpus(@Nullable Integer virtualCpus) {
      this.virtualCpus = virtualCpus;
      return this;
   }

   /**
    * Specifies the size of the first disk (in MB). If null, the disk will be left at its 
    * default size.
    */
   public VCloudDirectorTemplateOptions disk(@Nullable Integer disk) {
      this.disk = disk;
      return this;
   }

   public Statement getGuestCustomizationScript() {
      return guestCustomizationScript;
   }

   public Integer getMemory() { return memory; }

   public Integer getVirtualCpus() { return virtualCpus; }

   public Integer getDisk() { return disk; }

   @Override
   public VCloudDirectorTemplateOptions clone() {
      VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
      copyTo(options);
      return options;
   }

   @Override
   public void copyTo(TemplateOptions to) {
      super.copyTo(to);
      if (to instanceof VCloudDirectorTemplateOptions) {
         VCloudDirectorTemplateOptions vto = VCloudDirectorTemplateOptions.class.cast(to);
         vto.guestCustomizationScript(getGuestCustomizationScript());
         vto.memory(memory);
         vto.virtualCpus(virtualCpus);
         vto.disk(disk);
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      VCloudDirectorTemplateOptions that = VCloudDirectorTemplateOptions.class.cast(o);
      return super.equals(that) && equal(this.guestCustomizationScript, that.guestCustomizationScript)
            && equal(this.memory, that.memory) && equal(this.virtualCpus, that.virtualCpus) 
            && equal(this.disk, that.disk);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(super.hashCode(), guestCustomizationScript, memory, virtualCpus, disk);
   }

   @Override
   public MoreObjects.ToStringHelper string() {
      MoreObjects.ToStringHelper toString = super.string();
      if (guestCustomizationScript != null)
         toString.add("guestCustomizationScript", guestCustomizationScript);
      if (memory != null)
         toString.add("memory", memory);
      if (virtualCpus != null)
         toString.add("virtualCpus", virtualCpus);
      if (disk != null)
         toString.add("disk", disk);
      return toString;
   }

   public static class Builder {

      /**
       * @see #guestCustomizationScript
       */
      public static VCloudDirectorTemplateOptions guestCustomizationScript(String guestCustomizationScript) {
         return guestCustomizationScript(Statements.exec(guestCustomizationScript));
      }

      public static VCloudDirectorTemplateOptions guestCustomizationScript(Statement guestCustomizationScript) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.guestCustomizationScript(guestCustomizationScript);
      }

      /**
       * @see #memory
       */
      public static VCloudDirectorTemplateOptions memory(Integer memory) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.memory(memory);
      }

      /**
       * @see #virtualCpus
       */
      public static VCloudDirectorTemplateOptions virtualCpus(Integer virtualCpus) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.virtualCpus(virtualCpus);
      }

      /**
       * @see #disk
       */
      public static VCloudDirectorTemplateOptions disk(Integer disk) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.disk(disk);
      }

      /**
       * @see TemplateOptions#inboundPorts(int...)
       */
      public static VCloudDirectorTemplateOptions inboundPorts(int... ports) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.inboundPorts(ports);
      }

      /**
       * @see TemplateOptions#blockOnPort(int, int)
       */
      public static VCloudDirectorTemplateOptions blockOnPort(int port, int seconds) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.blockOnPort(port, seconds);
      }

      /**
       * @see TemplateOptions#installPrivateKey(String)
       */
      public static VCloudDirectorTemplateOptions installPrivateKey(String rsaKey) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.installPrivateKey(rsaKey);
      }

      /**
       * @see TemplateOptions#authorizePublicKey(String)
       */
      public static VCloudDirectorTemplateOptions authorizePublicKey(String rsaKey) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.authorizePublicKey(rsaKey);
      }

      /**
       * @see TemplateOptions#userMetadata(Map)
       */
      public static VCloudDirectorTemplateOptions userMetadata(Map<String, String> userMetadata) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.userMetadata(userMetadata);
      }

      /**
       * @see TemplateOptions#nodeNames(Iterable)
       */
      public static VCloudDirectorTemplateOptions nodeNames(Iterable<String> nodeNames) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.nodeNames(nodeNames);
      }

      /**
       * @see TemplateOptions#networks(Iterable)
       */
      public static VCloudDirectorTemplateOptions networks(Iterable<String> networks) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.networks(networks);
      }

      /**
       * @see TemplateOptions#overrideLoginUser(String)
       */
      public static VCloudDirectorTemplateOptions overrideLoginUser(String user) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.overrideLoginUser(user);
      }

      /**
       * @see TemplateOptions#overrideLoginPassword(String)
       */
      public static VCloudDirectorTemplateOptions overrideLoginPassword(String password) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.overrideLoginPassword(password);
      }

      /**
       * @see TemplateOptions#overrideLoginPrivateKey(String)
       */
      public static VCloudDirectorTemplateOptions overrideLoginPrivateKey(String privateKey) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.overrideLoginPrivateKey(privateKey);
      }

      /**
       * @see TemplateOptions#overrideAuthenticateSudo(boolean)
       */
      public static VCloudDirectorTemplateOptions overrideAuthenticateSudo(boolean authenticateSudo) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.overrideAuthenticateSudo(authenticateSudo);
      }

      /**
       * @see TemplateOptions#overrideLoginCredentials(LoginCredentials)
       */
      public static VCloudDirectorTemplateOptions overrideLoginCredentials(LoginCredentials credentials) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.overrideLoginCredentials(credentials);
      }

      /**
       * @see TemplateOptions#blockUntilRunning(boolean)
       */
      public static VCloudDirectorTemplateOptions blockUntilRunning(boolean blockUntilRunning) {
         VCloudDirectorTemplateOptions options = new VCloudDirectorTemplateOptions();
         return options.blockUntilRunning(blockUntilRunning);
      }

   }


   // methods that only facilitate returning the correct object type

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions blockOnPort(int port, int seconds) {
      return VCloudDirectorTemplateOptions.class.cast(super.blockOnPort(port, seconds));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions inboundPorts(int... ports) {
      return VCloudDirectorTemplateOptions.class.cast(super.inboundPorts(ports));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions authorizePublicKey(String publicKey) {
      return VCloudDirectorTemplateOptions.class.cast(super.authorizePublicKey(publicKey));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions installPrivateKey(String privateKey) {
      return VCloudDirectorTemplateOptions.class.cast(super.installPrivateKey(privateKey));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions blockUntilRunning(boolean blockUntilRunning) {
      return VCloudDirectorTemplateOptions.class.cast(super.blockUntilRunning(blockUntilRunning));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions dontAuthorizePublicKey() {
      return VCloudDirectorTemplateOptions.class.cast(super.dontAuthorizePublicKey());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions nameTask(String name) {
      return VCloudDirectorTemplateOptions.class.cast(super.nameTask(name));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions runAsRoot(boolean runAsRoot) {
      return VCloudDirectorTemplateOptions.class.cast(super.runAsRoot(runAsRoot));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions runScript(Statement script) {
      return VCloudDirectorTemplateOptions.class.cast(super.runScript(script));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions overrideLoginCredentials(LoginCredentials overridingCredentials) {
      return VCloudDirectorTemplateOptions.class.cast(super.overrideLoginCredentials(overridingCredentials));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions overrideLoginPassword(String password) {
      return VCloudDirectorTemplateOptions.class.cast(super.overrideLoginPassword(password));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions overrideLoginPrivateKey(String privateKey) {
      return VCloudDirectorTemplateOptions.class.cast(super.overrideLoginPrivateKey(privateKey));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions overrideLoginUser(String loginUser) {
      return VCloudDirectorTemplateOptions.class.cast(super.overrideLoginUser(loginUser));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions overrideAuthenticateSudo(boolean authenticateSudo) {
      return VCloudDirectorTemplateOptions.class.cast(super.overrideAuthenticateSudo(authenticateSudo));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions userMetadata(Map<String, String> userMetadata) {
      return VCloudDirectorTemplateOptions.class.cast(super.userMetadata(userMetadata));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions userMetadata(String key, String value) {
      return VCloudDirectorTemplateOptions.class.cast(super.userMetadata(key, value));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions nodeNames(Iterable<String> nodeNames) {
      return VCloudDirectorTemplateOptions.class.cast(super.nodeNames(nodeNames));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public VCloudDirectorTemplateOptions networks(Iterable<String> networks) {
      return VCloudDirectorTemplateOptions.class.cast(super.networks(networks));
   }

}
