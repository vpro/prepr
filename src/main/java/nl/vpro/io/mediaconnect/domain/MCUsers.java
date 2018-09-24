package nl.vpro.io.mediaconnect.domain;

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
@JsonDeserialize(using = MCUsers.Deserializer.class)
@Data
@Slf4j
public class MCUsers {

    Map<UUID, List<MCUser>> users;

     public static class Deserializer extends JsonDeserializer<MCUsers> {

         @Override
         public MCUsers deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
             MCUsers result = new MCUsers();
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
                         MCUser[] users = ctxt.getParser().readValueAs(MCUser[].class);
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
