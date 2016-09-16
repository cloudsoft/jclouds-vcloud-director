jclouds-vcloud-director
=======================

In order to release a new version:

1. create a new `release` branch out of `master` branch
2. update the version inside `release/new-branch` pom.xml
3. mvn clean install

If everything is ok, finally push it to Cloudsoft Artifactory:

4. mvn deploy -DaltDeploymentRepository=cloudsoft-deploy-artifactory-release::default::http://ccweb.cloudsoftcorp.com/maven/libs-release-local/ -DskipTests
