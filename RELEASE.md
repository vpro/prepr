

```bash
mvn release:branch -DbranchName=0.6-SNAPSHOT -DdevelopmentVersion=0.7-SNAPSHOT
git checkout 0.6-SNAPSHOT
mvn -Pdeploy release:prepare release:perform -DreleaseVersion=0.6.0 -DdevelopmentVersion=0.6.1
```
