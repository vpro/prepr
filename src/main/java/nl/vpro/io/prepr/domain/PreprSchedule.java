package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonSerialize(using = PreprSchedule.Serializer.class)
@JsonDeserialize(using = PreprSchedule.Deserializer.class)
@Slf4j
public class PreprSchedule implements Iterable<Map.Entry<LocalDate, List<PreprEvent>>> {

    SortedMap<LocalDate, List<PreprEvent>> days;

    @NonNull
    @Override
    public Iterator<Map.Entry<LocalDate, List<PreprEvent>>> iterator() {
        return days.entrySet().iterator();
    }


    public static class Serializer extends JsonSerializer<PreprSchedule> {


        @Override
        public void serialize(PreprSchedule value, JsonGenerator gen, SerializerProvider serializers) {
            throw new UnsupportedOperationException();
        }
    }

     public static class Deserializer extends JsonDeserializer<PreprSchedule> {

         @Override
         public PreprSchedule deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
             PreprSchedule result = new PreprSchedule();
             result.days = new TreeMap<>();
             JsonToken t = p.getCurrentToken();

             if (t.isStructStart()) {
                 while (true) {
                     JsonToken token = p.nextToken();
                     if (token.isStructEnd()) {
                         break;
                     }

                     String date = p.getCurrentName();
                     p.nextToken();
                     List<PreprEvent> events = new ArrayList<>();
                     result.days.put(LocalDate.parse(date), events);
                     try {
                         JsonNode[] jsonNodes = ctxt.getParser().readValueAs(JsonNode[].class);
                         for (JsonNode jsonNode : jsonNodes) {
                             try {
                                 events.add(p.getCodec().treeToValue(jsonNode, PreprEvent.class));
                             } catch (JsonMappingException jma) {
                                 log.error("{} -> {}: {}, {}", jsonNode, jma.getClass().getSimpleName(), jma.getMessage(), jma);
                             }
                         }
                     } catch (JsonMappingException jma) {
                         log.error("{}: {}", jma.getClass().getSimpleName(), jma.getMessage(), jma);
                     } catch (DateTimeParseException pe) {
                         log.error(pe.getMessage(), pe);
                     }
                 }
             }
             return result;
         }
     }
}
