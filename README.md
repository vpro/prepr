# prepr
Domain classes and client for https://developers.prepr.io/ ('prepr')

https://prepr.io, https://docs.prepr.io/


[![Build Status](https://travis-ci.org/vpro/prepr.svg?)](https://travis-ci.org/vpro/prepr)
[![Maven Central](https://img.shields.io/maven-central/v/nl.vpro/prepr.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22nl.vpro%22%20AND%20a:%22prepr%22)
[![snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/nl.vpro/prepr.svg)](https://oss.sonatype.org/content/repositories/staging/nl/vpro/prepr)
[![javadoc](http://www.javadoc.io/badge/nl.vpro/prepr.svg?color=blue)](http://www.javadoc.io/doc/nl.vpro/prepr)


This was created because [POMS](https://rs.poms.omroep.nl/v1) (at the moment a future version of it) sychronizes data from prepr for radio related data of the dutch public broadcasters


Example code:
```java
   PreprRepositoryImpl impl = new PreprRepositoryImpl(
       Prepr.
        .builder()
        .clientId("<your client id>")
        .clientSecret("<your client secret>")
        .build();

 
   
    public void getSchedule() throws IOException, URISyntaxException {
        log.info("schedule: {}",
            impl.getTimelines().getSchedule(UUID.fromString("59ad94c1-7dec-4ea0-a9b4-b9eb4b6cfb16") // Channel.RAD5)
                , LocalDate.of(2018, 5, 7), LocalDate.of(2018, 5, 8))
        );
    }
```
It is also possible to instantiate a bunch of  repositories using spring
```xml
  <bean class="nl.vpro.io.prepr.spring.SpringPreprRepositoriesConfiguration">
    <constructor-arg value="media.properties" />
  </bean>
```
This will create ``PreprRepositoryImpl`` beans ``prepr.<channel>``, and also one instance of ``PreprRepositories``


Something similar can also be accomplished without spring
```java
nl.vpro.io.prepr.StandalonePreprRepositories.fromMap()

```
(note that ``@CacheResult`` is not working then.)

## Testing

There are junit test in the normal spots. Some things are in '*ITest' classes. These are currently mainly used to try things out. They find credentials in `${USER.HOME}/conf/prepr.properties`. We don't have proper integration tests yet.
