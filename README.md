jclouds-vcloud-director
=======================
[![Build Status](https://api.travis-ci.org/cloudsoft/jclouds-vcloud-director.svg?branch=1.9.x)](https://travis-ci.org/cloudsoft/jclouds-vcloud-director)
-----------------------

In order to release a new version:

1. check out the previous most recent release (e.g. if going from 2.1.0 to 2.1.2, then checkout `2.1.0.x`).
2. create a new branch with the name of the new version (e.g. `2.1.2.x`)
3. update the version inside this branch
4. `mvn clean install`
   `mvn source:jar javadoc:jar install`

If everything is ok, finally push it to Cloudsoft Artifactory:

5. `mvn source:jar javadoc:jar deploy -DaltDeploymentRepository=cloudsoft-artifactory-repo::default::https://artifactory.cloudsoftcorp.com/artifactory/libs-release-local -DskipTests`
