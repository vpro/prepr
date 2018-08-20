package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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
public class MCSchedule implements Iterable<Map.Entry<LocalDate, List<MCEvent>>> {


    Map<LocalDate, List<MCEvent>> days;

    @Override
    public Iterator<Map.Entry<LocalDate, List<MCEvent>>> iterator() {
        return days.entrySet().iterator();

    }


    public static class Serializer extends JsonSerializer<MCSchedule> {


        @Override
        public void serialize(MCSchedule value, JsonGenerator gen, SerializerProvider serializers) {
            throw new UnsupportedOperationException();
        }
    }

     public static class Deserializer extends JsonDeserializer<MCSchedule> {

         @Override
         public MCSchedule deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
             MCSchedule result = new MCSchedule();
             result.days = new HashMap<>();
             JsonToken t = p.getCurrentToken();

             if (t.isStructStart()) {
                 while (true) {
                     JsonToken token = p.nextToken();
                     if (token.isStructEnd()) {
                         break;
                     }

                     String date = p.getCurrentName();
                     JsonToken array = p.nextToken();
                     MCEvent[] events = ctxt.getParser().readValueAs(MCEvent[].class);
                     result.days.put(LocalDate.parse(date), Arrays.asList(events));
                 }
             }
             return result;
         }
     }
}
