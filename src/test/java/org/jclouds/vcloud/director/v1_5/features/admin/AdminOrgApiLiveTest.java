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
package org.jclouds.vcloud.director.v1_5.features.admin;

import static com.google.common.base.Objects.equal;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorLiveTestConstants.OBJ_FIELD_UPDATABLE;
import static org.testng.Assert.assertTrue;

import org.jclouds.vcloud.director.v1_5.domain.Checks;
import org.jclouds.vcloud.director.v1_5.domain.network.SmtpServerSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.AdminOrg;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgEmailSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgGeneralSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgLdapSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgLeaseSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgPasswordPolicySettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgSettings;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgVAppTemplateLeaseSettings;
import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorApiLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests live behavior of {@link AdminOrgApi}.
 */
@Test(groups = { "live", "admin" }, singleThreaded = true, testName = "AdminOrgApiLiveTest")
public class AdminOrgApiLiveTest extends BaseVCloudDirectorApiLiveTest {

   public static final String ORG = "admin org";

   /*
    * Convenience references to API apis.
    */

   private AdminOrgApi adminOrgApi;

   /*
    * Shared state between dependant tests.
    */
   private OrgSettings settings;
   private OrgEmailSettings emailSettings;
   private OrgGeneralSettings generalSettings;
   private OrgLdapSettings ldapSettings;
   private OrgPasswordPolicySettings passwordPolicy;
   private OrgLeaseSettings vAppLeaseSettings;
   private OrgVAppTemplateLeaseSettings vAppTemplateLeaseSettings;

   @Override
   @BeforeClass(alwaysRun = true)
   public void setupRequiredApis() {
      adminOrgApi = api.getAdminOrgApi();
   }

   @Test(description = "GET /admin/org/{id}")
   public void testGetAdminOrg() {
      AdminOrg adminOrg = adminOrgApi.get(org.getId());

      Checks.checkAdminOrg(adminOrg);
   }

   @Test(description = "GET /admin/org/{id}/settings/email")
   public void testGetEmailSettings() {
      emailSettings = adminOrgApi.getEmailSettings(org.getId());

      Checks.checkEmailSettings(emailSettings);
   }

   @Test(description = "PUT /admin/org/{id}/settings/email", dependsOnMethods = { "testGetEmailSettings" })
   public void testEditEmailSettings() {
      boolean isDefaultSmtpServer = emailSettings.isDefaultSmtpServer();
      boolean isDefaultOrgEmail = emailSettings.isDefaultOrgEmail();
      String oldFromEmailAddress = emailSettings.getFromEmailAddress();
      String newFromEmailAddress = "test@test.com";
      String oldDefaultSubjectPrefix = emailSettings.getDefaultSubjectPrefix();
      String newDefaultSubjectPrefix = "new" + oldDefaultSubjectPrefix;
      boolean isAlertEmailToAllAdmins = emailSettings.isAlertEmailToAllAdmins();
      SmtpServerSettings oldSmtpServerSettings = emailSettings.getSmtpServerSettings();
      SmtpServerSettings newSmtpServerSettings = oldSmtpServerSettings.toBuilder()
               .useAuthentication(!oldSmtpServerSettings.useAuthentication())
               .host("new" + oldSmtpServerSettings.getHost()).username("new" + oldSmtpServerSettings.getUsername())
               .password("new" + oldSmtpServerSettings.getPassword()).build();

      try {
         OrgEmailSettings newEmailSettings = emailSettings.toBuilder().isDefaultSmtpServer(!isDefaultSmtpServer)
                  .isDefaultOrgEmail(!isDefaultOrgEmail).fromEmailAddress(newFromEmailAddress)
                  .defaultSubjectPrefix(newDefaultSubjectPrefix).isAlertEmailToAllAdmins(!isAlertEmailToAllAdmins)
                  .smtpServerSettings(newSmtpServerSettings).build();

         emailSettings = adminOrgApi.editEmailSettings(org.getId(), newEmailSettings);

         assertTrue(equal(emailSettings.isDefaultSmtpServer(), !isDefaultSmtpServer),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "isDefaultSmtpServer"));
         assertTrue(equal(emailSettings.isDefaultOrgEmail(), !isDefaultOrgEmail),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "isDefaultOrgEmail"));
         assertTrue(equal(emailSettings.getFromEmailAddress(), newFromEmailAddress),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "fromEmailAddress"));
         assertTrue(equal(emailSettings.getDefaultSubjectPrefix(), newDefaultSubjectPrefix),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "defaultSubjectPrefix"));
         assertTrue(equal(emailSettings.isAlertEmailToAllAdmins(), !isAlertEmailToAllAdmins),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "isAlertEmailToAllAdmins"));
         assertTrue(equal(emailSettings.getSmtpServerSettings(), newSmtpServerSettings),
                  String.format(OBJ_FIELD_UPDATABLE, "emailSettings", "smtpServerSettings"));

         // TODO negative tests?

         Checks.checkEmailSettings(emailSettings);
      } finally {
         emailSettings = emailSettings.toBuilder().isDefaultSmtpServer(isDefaultSmtpServer)
                  .isDefaultOrgEmail(isDefaultOrgEmail).fromEmailAddress(oldFromEmailAddress)
                  .defaultSubjectPrefix(oldDefaultSubjectPrefix).isAlertEmailToAllAdmins(isAlertEmailToAllAdmins)
                  .smtpServerSettings(oldSmtpServerSettings).build();

         emailSettings = adminOrgApi.editEmailSettings(org.getId(), emailSettings);
      }
   }

   @Test(description = "GET /admin/org/{id}/settings/general")
   public void testGetGeneralSettings() {
      generalSettings = adminOrgApi.getGeneralSettings(org.getId());

      Checks.checkGeneralSettings(generalSettings);
   }

   @Test(description = "PUT /admin/org/{id}/settings/general", dependsOnMethods = { "testGetGeneralSettings" })
   public void testEditGeneralSettings() {
      // FIXME: canPublishCatalogs does not edit
      // boolean canPublishCatalogs = generalSettings.canPublishCatalogs();
      Integer deployedVMQuota = generalSettings.getDeployedVMQuota();
      Integer storedVmQuota = generalSettings.getStoredVmQuota();
      boolean useServerBootSequence = generalSettings.useServerBootSequence();
      Integer delayAfterPowerOnSeconds = generalSettings.getDelayAfterPowerOnSeconds();

      try {
         OrgGeneralSettings newGeneralSettings = generalSettings
                  .toBuilder()
                  // .canPublishCatalogs(!canPublishCatalogs)
                  .deployedVMQuota(deployedVMQuota + 1).storedVmQuota(storedVmQuota + 1)
                  .useServerBootSequence(!useServerBootSequence).delayAfterPowerOnSeconds(delayAfterPowerOnSeconds + 1)
                  .build();

         generalSettings = adminOrgApi.editGeneralSettings(org.getId(), newGeneralSettings);

         // assertTrue(equal(generalSettings.canPublishCatalogs(), !canPublishCatalogs),
         // String.format(OBJ_FIELD_UPDATABLE,
         // "generalSettings", "canPublishCatalogs"));
         assertTrue(equal(generalSettings.getDeployedVMQuota(), deployedVMQuota + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "generalSettings", "deployedVMQuota"));
         assertTrue(equal(generalSettings.getStoredVmQuota(), storedVmQuota + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "generalSettings", "storedVmQuota"));
         assertTrue(equal(generalSettings.useServerBootSequence(), !useServerBootSequence),
                  String.format(OBJ_FIELD_UPDATABLE, "generalSettings", "useServerBootSequence"));
         assertTrue(equal(generalSettings.getDelayAfterPowerOnSeconds(), delayAfterPowerOnSeconds + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "generalSettings", "delayAfterPowerOnSeconds"));

         // TODO negative tests?

         Checks.checkGeneralSettings(generalSettings);
      } finally {
         generalSettings = generalSettings
                  .toBuilder()
                  // .canPublishCatalogs(canPublishCatalogs)
                  .deployedVMQuota(deployedVMQuota).storedVmQuota(storedVmQuota)
                  .useServerBootSequence(useServerBootSequence).delayAfterPowerOnSeconds(delayAfterPowerOnSeconds)
                  .build();

         generalSettings = adminOrgApi.editGeneralSettings(org.getId(), generalSettings);
      }
   }

   @Test(description = "GET /admin/org/{id}/settings/ldap")
   public void testGetLdapSettings() {
      ldapSettings = adminOrgApi.getLdapSettings(org.getId());

      Checks.checkLdapSettings(ldapSettings);
   }

   @Test(description = "GET /admin/org/{id}/settings/passwordPolicy")
   public void testGetPasswordPolicy() {
      passwordPolicy = adminOrgApi.getPasswordPolicy(org.getId());

      Checks.checkPasswordPolicySettings(passwordPolicy);
   }

   @Test(description = "PUT /admin/org/{id}/settings/passwordPolicy", dependsOnMethods = { "testGetPasswordPolicy" })
   public void testEditPasswordPolicy() {
      boolean accountLockoutEnabled = passwordPolicy.isAccountLockoutEnabled();
      Integer invalidLoginsBeforeLockout = passwordPolicy.getInvalidLoginsBeforeLockout();
      Integer accountLockoutIntervalMinutes = passwordPolicy.getAccountLockoutIntervalMinutes();

      try {
         OrgPasswordPolicySettings newPasswordPolicy = passwordPolicy.toBuilder()
                  .accountLockoutEnabled(!accountLockoutEnabled)
                  .invalidLoginsBeforeLockout(invalidLoginsBeforeLockout + 1)
                  .accountLockoutIntervalMinutes(accountLockoutIntervalMinutes + 1).build();

         passwordPolicy = adminOrgApi.editPasswordPolicy(org.getId(), newPasswordPolicy);

         assertTrue(equal(passwordPolicy.isAccountLockoutEnabled(), !accountLockoutEnabled),
                  String.format(OBJ_FIELD_UPDATABLE, "PasswordPolicySettings", "deleteOnStorageLeaseExpiration"));
         assertTrue(equal(passwordPolicy.getInvalidLoginsBeforeLockout(), invalidLoginsBeforeLockout + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "PasswordPolicySettings", "storageLeaseSeconds"));
         assertTrue(equal(passwordPolicy.getAccountLockoutIntervalMinutes(), accountLockoutIntervalMinutes + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "PasswordPolicySettings", "deploymentLeaseSeconds"));

         // TODO negative tests?

         Checks.checkPasswordPolicySettings(passwordPolicy);
      } finally {
         passwordPolicy = passwordPolicy.toBuilder().accountLockoutEnabled(accountLockoutEnabled)
                  .invalidLoginsBeforeLockout(invalidLoginsBeforeLockout)
                  .accountLockoutIntervalMinutes(accountLockoutIntervalMinutes).build();

         passwordPolicy = adminOrgApi.editPasswordPolicy(org.getId(), passwordPolicy);
      }
   }

   @Test(description = "GET /admin/org/{id}/settings/vAppLeaseSettings")
   public void testGetVAppLeaseSettings() {
      vAppLeaseSettings = adminOrgApi.getVAppLeaseSettings(org.getId());

      Checks.checkVAppLeaseSettings(vAppLeaseSettings);
   }

   @Test(description = "PUT /admin/org/{id}/settings/vAppLeaseSettings", dependsOnMethods = { "testGetVAppLeaseSettings" })
   // FIXME: fails with 403 forbidden
   public void testEditVAppLeaseSettings() {
      boolean deleteOnStorageLeaseExpiration = vAppLeaseSettings.deleteOnStorageLeaseExpiration();
      Integer storageLeaseSeconds = vAppLeaseSettings.getStorageLeaseSeconds();
      Integer deploymentLeaseSeconds = vAppLeaseSettings.getDeploymentLeaseSeconds();

      try {
         OrgLeaseSettings newVAppLeaseSettings = vAppLeaseSettings.toBuilder()
                  .deleteOnStorageLeaseExpiration(!deleteOnStorageLeaseExpiration)
                  .storageLeaseSeconds(storageLeaseSeconds + 1).deploymentLeaseSeconds(deploymentLeaseSeconds + 1)
                  .build();

         vAppLeaseSettings = adminOrgApi.editVAppLeaseSettings(org.getId(), newVAppLeaseSettings);

         assertTrue(equal(vAppLeaseSettings.deleteOnStorageLeaseExpiration(), !deleteOnStorageLeaseExpiration),
                  String.format(OBJ_FIELD_UPDATABLE, "vAppLeaseSettings", "deleteOnStorageLeaseExpiration"));
         assertTrue(equal(vAppLeaseSettings.getStorageLeaseSeconds(), storageLeaseSeconds + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "vAppLeaseSettings", "storageLeaseSeconds"));
         assertTrue(equal(vAppLeaseSettings.getDeploymentLeaseSeconds(), deploymentLeaseSeconds + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "vAppLeaseSettings", "deploymentLeaseSeconds"));

         // TODO negative tests?

         Checks.checkVAppLeaseSettings(vAppLeaseSettings);
      } finally {
         vAppLeaseSettings = vAppLeaseSettings.toBuilder()
                  .deleteOnStorageLeaseExpiration(deleteOnStorageLeaseExpiration)
                  .storageLeaseSeconds(storageLeaseSeconds).deploymentLeaseSeconds(deploymentLeaseSeconds).build();

         vAppLeaseSettings = adminOrgApi.editVAppLeaseSettings(org.getId(), vAppLeaseSettings);
      }
   }

   @Test(description = "GET /admin/org/{id}/settings/vAppTemplateLeaseSettings")
   public void testGetVAppTemplateLeaseSettings() {
      vAppTemplateLeaseSettings = adminOrgApi.getVAppTemplateLeaseSettings(org.getId());

      Checks.checkVAppTemplateLeaseSettings(vAppTemplateLeaseSettings);
   }

   @Test(description = "PUT /admin/org/{id}/settings/vAppTemplateLeaseSettings", dependsOnMethods = { "testGetVAppTemplateLeaseSettings" })
   // FIXME: fails with 403 forbidden
   public void testEditVAppTemplateLeaseSettings() {
      boolean deleteOnStorageLeaseExpiration = vAppTemplateLeaseSettings.deleteOnStorageLeaseExpiration();
      Integer storageLeaseSeconds = vAppTemplateLeaseSettings.getStorageLeaseSeconds();

      try {
         OrgVAppTemplateLeaseSettings newVAppTemplateLeaseSettings = vAppTemplateLeaseSettings.toBuilder()
                  .deleteOnStorageLeaseExpiration(!deleteOnStorageLeaseExpiration)
                  .storageLeaseSeconds(storageLeaseSeconds + 1).build();

         vAppTemplateLeaseSettings = adminOrgApi.editVAppTemplateLeaseSettings(org.getId(), newVAppTemplateLeaseSettings);

         assertTrue(equal(vAppTemplateLeaseSettings.deleteOnStorageLeaseExpiration(), !deleteOnStorageLeaseExpiration),
                  String.format(OBJ_FIELD_UPDATABLE, "vAppTemplateLeaseSettings", "deleteOnStorageLeaseExpiration"));
         assertTrue(equal(vAppTemplateLeaseSettings.getStorageLeaseSeconds(), storageLeaseSeconds + 1),
                  String.format(OBJ_FIELD_UPDATABLE, "vAppTemplateLeaseSettings", "storageLeaseSeconds"));

         // TODO negative tests?

         Checks.checkVAppTemplateLeaseSettings(vAppTemplateLeaseSettings);
      } finally {
         vAppTemplateLeaseSettings = vAppTemplateLeaseSettings.toBuilder()
                  .deleteOnStorageLeaseExpiration(deleteOnStorageLeaseExpiration)
                  .storageLeaseSeconds(storageLeaseSeconds).build();

         vAppTemplateLeaseSettings = adminOrgApi.editVAppTemplateLeaseSettings(org.getId(), vAppTemplateLeaseSettings);
      }
   }

   @Test(description = "GET /admin/org/{id}/settings")
   public void testGetSettings() {
      settings = adminOrgApi.getSettings(org.getId());

      Checks.checkOrgSettings(settings);
   }

   @Test(description = "PUT /admin/org/{id}/settings", dependsOnMethods = { "testGetEmailSettings" })
   public void testEditSettings() throws Exception {
      String newFromEmailAddress = "test" + random.nextInt(Integer.MAX_VALUE) + "@test.com";
      Exception exceptionInFinally = null;

      try {
         OrgSettings newSettings = OrgSettings.builder()
                  .emailSettings(emailSettings.toBuilder().fromEmailAddress(newFromEmailAddress).build()).build();

         OrgSettings modified = adminOrgApi.editSettings(org.getId(), newSettings);

         Checks.checkOrgSettings(settings);
         assertTrue(equal(modified.getEmailSettings().getFromEmailAddress(), newFromEmailAddress),
                  String.format(OBJ_FIELD_UPDATABLE, "orgSettings", "emailSettings"));

      } finally {
         try {
            OrgSettings restorableSettings = OrgSettings.builder().emailSettings(emailSettings).build();

            settings = adminOrgApi.editSettings(org.getId(), restorableSettings);
         } catch (Exception e) {
            exceptionInFinally = e;
            logger.warn(e, "Error resetting settings");
         }
      }
      if (exceptionInFinally != null) {
         throw exceptionInFinally;
      }
   }
}
