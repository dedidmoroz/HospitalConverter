package com.diagnosisproject.adapters;

import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Hospital;
import com.diagnosisproject.entities.Patient;
import com.diagnosisproject.services.PatientService;
import com.diagnosisproject.services.PatientServiceImpl;
import java8.util.Iterators;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deplague on 3/31/16.
 */
public class JsonDeserializer extends org.codehaus.jackson.map.JsonDeserializer<Hospital> {
    private static final String format = "yyyy-MM-dd";
    private static final DateTimeFormatter parser = DateTimeFormat.forPattern(format);
    private static final PatientService patientService = new PatientServiceImpl();
    private static final String idPattern = "[\\d]+";
    private static final String nameSurnamePattern = "[a-zA-Z]+";
    private static final String summaryPattern = "[\\w]+";
    private static final String addressPattern = "[\\w\\W\\d]+";
    private static final String datePattern = "[\\d]{4}-[\\d]{2}-[\\d]{2}";


    private Pattern patternMatching = null;
    private Matcher matcher = null;
    private String resultValidation = "Error in:\n";
    /**
     * <p>Build new Hospital instance</p>
     *
     * @param hospitalNode root not that will be using for building instances
     * @return Hospital, new instance builded from JSON document
     */
    private Hospital newHospital = null;
    private Patient newPatient = null;
    private Diagnosis newDiagnosis = null;

    @Override
    public Hospital deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode hospitalNode = jsonParser.getCodec().readTree(jsonParser);
        this.validateJSON(hospitalNode);


        System.out.println(resultValidation);
        return buildHospital(hospitalNode);
    }

    public Hospital buildHospital(JsonNode hospitalNode) {
        newHospital = Hospital.create()
                .setId(hospitalNode.get("id").asInt())
                .setTitle(hospitalNode.get("title").asText())
                .build();

        Iterators.forEachRemaining(hospitalNode.get("patients").iterator(), (patientNode) -> {
            newPatient = Patient.create()
                    .setId(Long.valueOf(patientNode.get("id").asInt()))
                    .setName(patientNode.get("name").asText())
                    .setSurName(patientNode.get("surName").asText())
                    .setBirthDay(parser.parseDateTime(patientNode.get("birthDay").asText()))
                    .setAddress(patientNode.get("address").asText())
                    .build();

            Iterators.forEachRemaining(patientNode.get("diagnosesList").iterator(), (diagnosesNode) -> { //fill diagnoses list
                newDiagnosis = Diagnosis.create()
                        .setDate(parser.parseDateTime(diagnosesNode.get("date").asText()))
                        .setSummary(diagnosesNode.get("summary").asText())
                        .build();
                patientService.addDiagnosis(newPatient, newDiagnosis);
            });

            newHospital.addPatient(newPatient);
        });
        return newHospital;
    }

    /**
     * <p>parse whole JSON document and validate field values. If found some error, write it to resultValidation</p>
     *
     * @param hospitalNode root node of JSON document
     */
    public void validateJSON(JsonNode hospitalNode) {
        Iterators.forEachRemaining(hospitalNode.getFields(), (json) -> {

            if (!json.getValue().isContainerNode()) {
                this.validate(json.getKey(), json.getValue().asText(), getPatternForNode(json.getKey()));
            } else {

                Iterators.forEachRemaining(json.getValue().iterator(), (jsonArray) -> {
                    Iterators.forEachRemaining(jsonArray.getFields(), (patientNode) -> {

                        if (!patientNode.getValue().isContainerNode()) {
                            this.validate(patientNode.getKey(), patientNode.getValue().asText(), getPatternForNode(patientNode.getKey()));
                        } else {

                            Iterators.forEachRemaining(patientNode.getValue().iterator(), (patientArray) -> {
                                Iterators.forEachRemaining(patientArray.getFields(), (diagnosisNode) -> {
                                    if (!diagnosisNode.getValue().isContainerNode()) {
                                        this.validate(diagnosisNode.getKey(), diagnosisNode.getValue().asText(), getPatternForNode(diagnosisNode.getKey()));
                                    }
                                });
                            });
                        }
                    });
                });
            }
        });

    }

    public void validate(String key, String value, String pattern) {
        patternMatching = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        matcher = patternMatching.matcher(value);

        if (!matcher.matches()) {
            wrapError(key, value);
        }
    }

    public void wrapError(String key, String value) {
        this.resultValidation += "\tError " + key + ":" + value + "!\n";
    }

    public String getPatternForNode(String key) {
        switch (key) {
            case "id":
                return idPattern;
            case "title":
                return nameSurnamePattern;
            case "name":
                return nameSurnamePattern;
            case "surName":
                return nameSurnamePattern;
            case "address":
                return addressPattern;
            case "birthDay":
                return datePattern;
            case "summary":
                return summaryPattern;
            case "date":
                return datePattern;
            default:
                return null;

        }
    }


}
