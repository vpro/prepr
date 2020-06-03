

```bash
mvn release:branch -DbranchName=0.6-SNAPSHOT -DdevelopmentVersion=0.7-SNAPSHOT
git checkout 0.6-SNAPSHOT
mvn -Pdeploy release:prepare release:perform -DreleaseVersion=0.6.0 -DdevelopmentVersion=0.6.1
```
to get it in vpro nexus quicker:
```bash
mvn -Pnexus -DaltReleaseDeploymentRepository=nexusvpro::default::http://nexus.vpro.nl/content/repositories/releases  deploy
```
and snapshots:
```bash
mvn -Pnexus -DaltSnapshotDeploymentRepository=nexusvpro::default::http://nexus.vpro.nl/content/repositories/snapshots  deploy
```
