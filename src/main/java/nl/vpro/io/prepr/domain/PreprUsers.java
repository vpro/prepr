package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@JsonDeserialize(using = PreprUsers.Deserializer.class)
@Data
@Slf4j
public class PreprUsers {

    Map<UUID, List<PreprPerson>> users;

     public static class Deserializer extends JsonDeserializer<PreprUsers> {

         @Override
         public PreprUsers deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
             PreprUsers result = new PreprUsers();
             result.users = new HashMap<>();
             JsonToken t = p.getCurrentToken();
             if (t.isStructStart()) {
                 while (true) {
                     JsonToken token = p.nextToken();
                     if (token.isStructEnd()) {
                         break;
                     }

                     String role = p.getCurrentName();
                     token = p.nextToken();
                     if (token != JsonToken.START_ARRAY) {
                         throw new IllegalStateException();
                     }
                     try {
                         PreprPerson[] users = ctxt.getParser().readValueAs(PreprPerson[].class);
                         result.users.put(UUID.fromString(role), Arrays.asList(users));
                     } catch (JsonMappingException jma) {
                         log.error(jma.getMessage(), jma);
                     }
                 }
             }
             return result;
         }
     }
}
