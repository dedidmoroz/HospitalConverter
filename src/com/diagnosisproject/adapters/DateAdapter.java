package com.diagnosisproject.adapters;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class DateAdapter extends XmlAdapter<String, DateTime> {
    public static final String format = "yyyy-MM-dd";
    private final DateTimeFormatter parser = DateTimeFormat.forPattern(format);

    @Override
    public DateTime unmarshal(String v) throws Exception {
        return parser.parseDateTime(v);
    }

    @Override
    public String marshal(DateTime v) throws Exception {
        return v.toLocalDate().toString();
    }
}
