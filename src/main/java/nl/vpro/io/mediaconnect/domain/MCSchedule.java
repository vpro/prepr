package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Michiel Meeuwissen
* @since 0.1
 */
@Data
@JsonSerialize(using = MCSchedule.Serializer.class)
@JsonDeserialize(using = MCSchedule.Deserializer.class)

@Slf4j
public class MCSchedule {


    Map<LocalDate, List<MCEvent>> days;


    public static class Serializer extends JsonSerializer<MCSchedule> {


        @Override
        public void serialize(MCSchedule value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            log.info("--");

        }
    }

     public static class Deserializer extends JsonDeserializer<MCSchedule> {

         @Override
         public MCSchedule deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
             MCSchedule result = new MCSchedule();
             result.days = new HashMap<>();
             p.nextToken();
             String date = p.getCurrentName();
             p.nextToken();
             MCEvent[] events = ctxt.getParser().readValueAs(MCEvent[].class);
             result.days.put(LocalDate.parse(date), Arrays.asList(events));
             return result;

         }
     }
}
