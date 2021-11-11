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
package org.jclouds.vcloud.director.v1_5.builders;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Provider;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class VCloudDirectorTemplateBuilderImpl extends TemplateBuilderImpl {
    @Inject
    protected VCloudDirectorTemplateBuilderImpl(@Memoized Supplier<Set<? extends Location>> locations,
            @Memoized Supplier<Set<? extends Image>> images, @Memoized Supplier<Set<? extends Hardware>> hardwares,
            Supplier<Location> defaultLocation, @Named("DEFAULT") Provider<TemplateOptions> optionsProvider,
            @Named("DEFAULT") Provider<TemplateBuilder> defaultTemplateProvider) {
        super(locations, images, hardwares, defaultLocation, optionsProvider, defaultTemplateProvider);
    }

    /**
     * The difference between supper class' {@code buildImagePredicate()} method is that the matching criteria
     * by location metadata is removed from the predicate, which prevents filtering by location. <br> <br>
     * @return {@code Predicate<Image>}
     */
    @Override
    protected Predicate<Image> buildImagePredicate() {
        List<Predicate<Image>> predicates = newArrayList();

        final List<Predicate<OperatingSystem>> osPredicates = newArrayList();
        if (osFamily != null)
            osPredicates.add(osFamilyPredicate);
        if (osName != null)
            osPredicates.add(osNamePredicate);
        if (osDescription != null)
            osPredicates.add(osDescriptionPredicate);
        if (osVersion != null)
            osPredicates.add(osVersionPredicate);
        if (os64Bit != null)
            osPredicates.add(os64BitPredicate);
        if (osArch != null)
            osPredicates.add(osArchPredicate);
        if (!osPredicates.isEmpty())
            predicates.add(new Predicate<Image>() {

                @Override
                public boolean apply(Image input) {
                    return Predicates.and(osPredicates).apply(input.getOperatingSystem());
                }

                @Override
                public String toString() {
                    return Predicates.and(osPredicates).toString();
                }

            });
        if (imageVersion != null)
            predicates.add(imageVersionPredicate);
        if (imageName != null)
            predicates.add(imageNamePredicate);
        if (imageDescription != null)
            predicates.add(imageDescriptionPredicate);
        if (imagePredicate != null)
            predicates.add(imagePredicate);

        // looks verbose, but explicit <Image> type needed for this to compile
        // properly
        Predicate<Image> imagePredicate = predicates.size() == 1 ? Iterables.<Predicate<Image>> get(predicates, 0)
                : Predicates.<Image> and(predicates);
        return imagePredicate;
    }
}
