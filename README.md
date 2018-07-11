# mediaconnect
Domain classes and client for https://developers.mediaconnect.io/


[![Build Status](https://travis-ci.org/vpro/mediaconnect.svg?)](https://travis-ci.org/vpro/mediaconnect)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.vpro/mediaconnect/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/nl.vpro/mediaconnect)


This was created because [POMS](https://rs.poms.omroep.nl/v3) (at the moment a future version of it) sychronizes data from mediaconnect for radio related data of the dutch public broadcasters


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
