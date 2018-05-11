package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
//@JsonSerialize(using = MCUsers.Serializer.class)
@JsonDeserialize(using = MCUsers.Deserializer.class)

@Data
public class MCUsers {

    Map<UUID, List<MCUser>> users;

     public static class Deserializer extends JsonDeserializer<MCUsers> {

         @Override
         public MCUsers deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
             MCUsers result = new MCUsers();
             result.users = new HashMap<>();
             p.nextToken();
             String role = p.getCurrentName();
             p.nextToken();
             MCUser[] events = ctxt.getParser().readValueAs(MCUser[].class);
             result.users.put(UUID.fromString(role), Arrays.asList(events));
             return result;

         }
     }
}
