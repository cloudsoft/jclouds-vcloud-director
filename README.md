jclouds-vcloud-director
=======================

[![Join the chat at https://gitter.im/cloudsoft/jclouds-vcloud-director](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cloudsoft/jclouds-vcloud-director?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

In order to release a new version:

1. create a new `release` branch out of `master` branch
2. update the version inside `release/new-branch` pom.xml
3. mvn clean install

If everything is ok, finally push it to Cloudsoft Artifactory:

4. mvn source:jar javadoc:jar deploy -DaltDeploymentRepository=cloudsoft-deploy-artifactory-release::default::http://ccweb.cloudsoftcorp.com/maven/libs-release-local/ -DskipTests
