package com.diagnosisproject.adapters;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by deplague on 3/30/16.
 */
public class JsonDateDeserialiser extends JsonDeserializer<DateTime> {
    public static final String format = "yyyy-MM-dd";
    public static final DateTimeFormatter parser = DateTimeFormat
            .forPattern(format);
    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return parser.parseDateTime(jsonParser.getText());
    }
}
