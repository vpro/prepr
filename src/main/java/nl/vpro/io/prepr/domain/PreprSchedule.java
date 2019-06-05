package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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


    Map<LocalDate, List<PreprEvent>> days;

    @Nonnull
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
             result.days = new LinkedHashMap<>();
             JsonToken t = p.getCurrentToken();

             if (t.isStructStart()) {
                 while (true) {
                     JsonToken token = p.nextToken();
                     if (token.isStructEnd()) {
                         break;
                     }

                     String date = p.getCurrentName();
                     p.nextToken();
                     try {
                         PreprEvent[] events = ctxt.getParser().readValueAs(PreprEvent[].class);
                         result.days.put(LocalDate.parse(date), Arrays.asList(events));
                     } catch (JsonMappingException jma) {
                         log.error(jma.getMessage(), jma);
                     } catch (DateTimeParseException pe) {
                         log.error(pe.getMessage(), pe);


                     }
                 }
             }
             return result;
         }
     }
}
