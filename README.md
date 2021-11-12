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
=======
jclouds-vcloud-director latest development version
==================================================

Latest branch from which jclouds-vcloud-director was built is `2.0.2.x`.
Please submit PRs against the latest branch.


## Older version branches

- `1.9.x`
- `2.0.0.x`
- `2.0.1.x`


## Updating jclouds-vcloud-director to a more recent jclouds version

This is needed when Apache Brooklyn updated its [jclouds](https://github.com/apache/brooklyn-server/blob/master/pom.xml#L104) version.
1. Checkout to "latest" branch and create a new branch for it e.g. `2.2.x` and push it.
2. Create a new branch from newly created "2.2.x" called "2.2.x-upgrade" and make a pull request with the necessary changes.
3. Submit a PR in README.md in `master` pointing the correct "latest" branch.


Produce a jclouds-vcloud-director build
=======================================

In order to release a new version from the "latest" branch:

1. create a new `release` branch out of "latest" branch
2. update the version inside `release/new-branch` pom.xml
3. mvn clean install
If everything is ok, finally push it to Cloudsoft Artifactory:
4. mvn deploy -DaltDeploymentRepository=cloudsoft-deploy-artifactory-release::default::http://ccweb.cloudsoftcorp.com/maven/libs-release-local/ -DskipTests
