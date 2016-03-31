package com.diagnosisproject.adapters;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;


import java.io.IOException;

/**
 * Created by deplague on 3/30/16.
 */
public class JsonDateSerialiser extends JsonSerializer<DateTime> {


    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(dateTime.getDayOfMonth()+"-"+dateTime.getMonthOfYear()+"-"+dateTime.getYear());
    }
}
