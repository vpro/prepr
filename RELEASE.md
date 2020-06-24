

```bash
mvn release:branch -DbranchName=1.0-SNAPSHOT -DdevelopmentVersion=1.1-SNAPSHOT
git checkout 1.0-SNAPSHOT
mvn -Pdeploy release:prepare release:perform -DreleaseVersion=1.0.0 -DdevelopmentVersion=1.0.1
```
to get it in vpro nexus quicker:
```bash
mvn -Pnexus -DaltReleaseDeploymentRepository=nexusvpro::default::http://nexus.vpro.nl/content/repositories/releases  deploy
```
and snapshots:
```bash
mvn -Pnexus -DaltSnapshotDeploymentRepository=nexusvpro::default::http://nexus.vpro.nl/content/repositories/snapshots  deploy
```
