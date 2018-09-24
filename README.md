# mediaconnect
Domain classes and client for https://developers.mediaconnect.io/


[![Build Status](https://travis-ci.org/vpro/mediaconnect.svg?)](https://travis-ci.org/vpro/mediaconnect)
[![Maven Central](https://img.shields.io/maven-central/v/nl.vpro/mediaconnect.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22nl.vpro%22%20AND%20a:%22mediaconnect%22)


This was created because [POMS](https://rs.poms.omroep.nl/v1) (at the moment a future version of it) sychronizes data from mediaconnect for radio related data of the dutch public broadcasters


Example code:
```java
   MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl
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

## Testing

There are junit test in the normal spots. Some things are in '*ITest' classes. These are currently mainly used to try things out. They find credentials in `${USER.HOME}/conf/mediaconnect.properties`. We don't have proper integration tests yet.
