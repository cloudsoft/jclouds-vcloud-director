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
package org.jclouds.vcloud.director.v1_5.functions;

import com.beust.jcommander.internal.Sets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.jclouds.vcloud.director.v1_5.domain.RasdItemsList;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.RasdItem;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.CimString;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.ResourceAllocationSettingData;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.namespace.QName;
import java.util.Set;

@Test(groups = "unit")
public class NewScsiLogicSASDiskTest {
    private static final RasdItemsList BASIC_DISK_SECTION;
    private static final RasdItemsList BASIC_DISK_SECTION_WITH_SAS;
    static {
        RasdItem scsiController = RasdItem.builder()
                .address("0")
                .description("SCSI Controller")
                .elementName("SCSI Controller 0")
                .instanceID("2")
                .resourceSubType("lsilogic")
                .resourceType(ResourceAllocationSettingData.ResourceType.PARALLEL_SCSI_HBA)
                .build();
        final CimString hostResource = new CimString("HostResource");
        Preconditions.checkNotNull(hostResource, "HostResource for the existing disk should not be null");
        hostResource.getOtherAttributes().put(new QName("http://www.vmware.com/vcloud/v1.5", "capacity"), "" + 1024000);
        hostResource.getOtherAttributes().put(new QName("http://www.vmware.com/vcloud/v1.5", "busSubType"), "lsilogic");
        hostResource.getOtherAttributes().put(new QName("http://www.vmware.com/vcloud/v1.5", "busType"), "6");

        RasdItem existingDisk = RasdItem.builder()
                .addressOnParent("0")
                .description("Hard disk")
                .elementName("Hard disk 1")
                .hostResources(ImmutableList.of(hostResource))
                .instanceID("2000")
                .parent(scsiController.getInstanceID())
                .resourceType(ResourceAllocationSettingData.ResourceType.DISK_DRIVE)
                .build();

        RasdItem dummyIdeController = RasdItem.builder()
                .address("0")
                .description("IDE Controller")
                .elementName("IDE Controller 0")
                .instanceID("" + (Integer.parseInt(scsiController.getInstanceID()) + 1))
                .resourceType(ResourceAllocationSettingData.ResourceType.IDE_CONTROLLER)
                .build();

        BASIC_DISK_SECTION = RasdItemsList.builder()
                .item(scsiController)
                .item(existingDisk)
                .item(dummyIdeController)
                .build();
    }

    static {
        // On first position of BASIC_DISK_SECTION should have a SCSI Controller
        RasdItem lsilogicsas = RasdItem.builder()
                .address("" + (Integer.parseInt(BASIC_DISK_SECTION.get(0).getAddress()) + 1))
                .description("SCSI Controller")
                .elementName("SCSI Controller 1")
                .instanceID("" + (Integer.parseInt(BASIC_DISK_SECTION.get(0).getInstanceID()) + 1))
                .resourceSubType("lsilogicsas")
                .resourceType(ResourceAllocationSettingData.ResourceType.PARALLEL_SCSI_HBA)
                .build();
        BASIC_DISK_SECTION_WITH_SAS = RasdItemsList.builder()
                .item(BASIC_DISK_SECTION.get(0))
                .item(BASIC_DISK_SECTION.get(1))
                .item(lsilogicsas)
                .item(RasdItem.builder()
                        .fromRasdItem(Iterables.getLast(BASIC_DISK_SECTION))
                        .instanceID("" + (Integer.parseInt(lsilogicsas.getInstanceID()) + 1))
                        .build()
                )
                .build();
    }

    @Test
    public void testFailingToAddOnNonSas() {
        try {
            new NewScsiLogicSASDisk().apply(BASIC_DISK_SECTION);
            Assert.fail("It should fail");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "New disks should be invoked when there is a SCSI Logic SAS controller.");
        }
    }

    @Test
    public void testCreatingNewHDD() {
        RasdItem newDisk = new NewScsiLogicSASDisk().apply(BASIC_DISK_SECTION_WITH_SAS);
        Preconditions.checkArgument(newDisk.getHostResources().size() == 1, "RasdDiskItem should have one HostResource");
        CimString newDiskHostResource = new CimString(Iterables.getOnlyElement(newDisk.getHostResources()));
        newDiskHostResource.getOtherAttributes().put(new QName("http://www.vmware.com/vcloud/v1.5", "capacity"), "" + 1024000);
        newDisk = RasdItem.builder().fromRasdItem(newDisk).hostResources(ImmutableList.of(newDiskHostResource)).build();

        Assert.assertTrue(newDisk.getHostResources().size() == 1);
        Assert.assertEquals(Iterables.getFirst(newDisk.getHostResources(), null).getOtherAttributes().get(new QName("http://www.vmware.com/vcloud/v1.5", "capacity")),
                "" + 1024000);
        Assert.assertEquals(Iterables.getFirst(newDisk.getHostResources(), null).getOtherAttributes().get(new QName("http://www.vmware.com/vcloud/v1.5", "busSubType")),
                "lsilogicsas");
    }

    @Test
    public void testCreatingNewSASController() {
        AddScsiLogicSASBus addScsiLogicSASBus = new AddScsiLogicSASBus();
        RasdItemsList virtualHardwareSectionDiskItems = addScsiLogicSASBus.addScsiLogicSASBus(BASIC_DISK_SECTION);

        Optional<RasdItem> scsiController = Iterables.tryFind(virtualHardwareSectionDiskItems, NewScsiLogicSASDisk.SCSI_LSILOGICSAS_PREDICATE);
        Assert.assertTrue(scsiController.isPresent(), "SCSI Controller should be available.");
        RasdItem ideController = Iterables.getLast(BASIC_DISK_SECTION);
        Assert.assertEquals(Iterables.getLast(virtualHardwareSectionDiskItems).getInstanceID(), "" + (Integer.parseInt(ideController.getInstanceID()) + 1));

        assertUniqueInstanceId(virtualHardwareSectionDiskItems);
    }

    public static void assertUniqueInstanceId(RasdItemsList virtualHardwareSectionDiskItems) {
        Set<String> instanceIds = Sets.newHashSet();
        for (RasdItem item : virtualHardwareSectionDiskItems) {
            Assert.assertFalse(instanceIds.contains(item.getInstanceID()));
            instanceIds.add(item.getInstanceID());
        }
    }
}
