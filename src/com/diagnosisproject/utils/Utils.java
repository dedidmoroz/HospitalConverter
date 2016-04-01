package com.diagnosisproject.utils;

import com.diagnosisproject.services.PatientService;
import com.diagnosisproject.services.PatientServiceImpl;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by deplague on 4/1/16.
 */
public class Utils {
    //whole patterns for parsing
    public static final String ID_PATTERN = "[\\d]+";
    public static final String NAME_SURNAME_PATTERN = "[a-zA-Z]+";
    public static final String SUMMARY_PATTERN = "[\\w]+";
    public static final String ADDRESS_PATTERN = "[\\w\\W\\d]+";
    public static final String DATE_PATTERN = "[1-2][09][0-9][0-9]-([0-1])?[1-9]([0-2])?-[\\d]{2}";
    //date parser
    public static final String FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter PARSER = DateTimeFormat.forPattern(FORMAT);

    //services
    public static final PatientService PATIENT_SERVICE = new PatientServiceImpl();

}
