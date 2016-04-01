package com.diagnosisproject.adapters;

import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Hospital;
import com.diagnosisproject.entities.Patient;
import com.diagnosisproject.utils.Utils;
import java8.util.Iterators;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deplague on 3/31/16.
 *
 * @version 1.0
 */
public class JsonDeserializer extends org.codehaus.jackson.map.JsonDeserializer<Hospital> {

    private Pattern patternMatching = null;
    private Matcher matcher = null;

    /**
     * <p>Build new Hospital instance</p>
     *
     * @param hospitalNode root not that will be using for building instances
     * @return Hospital, new instance builded from JSON document
     */
    private Hospital newHospital = null;
    private Patient newPatient = null;
    private Diagnosis newDiagnosis = null;

    private Boolean shutDown = false;

    @Override
    public Hospital deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode hospitalNode = jsonParser.getCodec().readTree(jsonParser);
        this.validateJSON(hospitalNode);
        if (shutDown) {
            System.exit(1);
        }
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
                    .setBirthDay(Utils.PARSER.parseDateTime(patientNode.get("birthDay").asText()))
                    .setAddress(patientNode.get("address").asText())
                    .build();

            Iterators.forEachRemaining(patientNode.get("diagnosesList").iterator(), (diagnosesNode) -> { //fill diagnoses list
                newDiagnosis = Diagnosis.create()
                        .setDate(Utils.PARSER.parseDateTime(diagnosesNode.get("date").asText()))
                        .setSummary(diagnosesNode.get("summary").asText())
                        .build();
                Utils.PATIENT_SERVICE.addDiagnosis(newPatient, newDiagnosis);
            });

            newHospital.addPatient(newPatient);
        });
        return newHospital;
    }

    /**
     * <p>parse whole JSON document and validate field values. If found some error</p>
     *
     * @param hospitalNode root node of JSON document
     */
    public void validateJSON(JsonNode hospitalNode) {
        Iterators.forEachRemaining(hospitalNode.getFields(), (json) -> {

            if (!json.getValue().isContainerNode()) {
                this.validate(json, "hospital", json.getKey(), json.getValue().asText(), getPatternForNode(json.getKey()));
            } else {

                Iterators.forEachRemaining(json.getValue().iterator(), (jsonArray) -> {
                    Iterators.forEachRemaining(jsonArray.getFields(), (patientNode) -> {

                        if (!patientNode.getValue().isContainerNode()) {
                            this.validate(patientNode, "patient", patientNode.getKey(), patientNode.getValue().asText(), getPatternForNode(patientNode.getKey()));
                        } else {

                            Iterators.forEachRemaining(patientNode.getValue().iterator(), (patientArray) -> {
                                Iterators.forEachRemaining(patientArray.getFields(), (diagnosisNode) -> {
                                    if (!diagnosisNode.getValue().isContainerNode()) {
                                        this.validate(diagnosisNode, "diagnosis", diagnosisNode.getKey(), diagnosisNode.getValue().asText(), getPatternForNode(diagnosisNode.getKey()));
                                    }
                                });
                            });
                        }
                    });
                });
            }
        });

    }

    /**
     * <p>
     * validate passed value: match it with @param pattern.
     * if incorrect, throw exception
     * </p>
     *
     * @param node    is current node validated by system
     * @param key     is a key of validated field
     * @param value   is validated value
     * @param pattern is uses by validator for matching with value
     * @throws JSONFieldInvalidException validate all fields
     */
    public void validate(Map.Entry<String, JsonNode> node, String nodeName, String key, String value, String pattern) {
        patternMatching = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        matcher = patternMatching.matcher(value);
        String nodeType = node.getValue().asToken().toString();

        if (!matcher.matches()) {
            try {
                throw new JSONFieldInvalidException(wrapError(nodeName,
                        nodeType.substring(nodeType.indexOf('_') + 1, nodeType.length()),
                        key, value));
            } catch (JSONFieldInvalidException e) {
                e.printStackTrace();
                this.shutDown = true;
            }
        }
    }

    public String wrapError(String nodeName, String nodeType, String key, String value) {
        return "Can not construct instance with node ["
                + nodeName
                + " { " + key + " : " + value + " }].Not a valid. \n" +
                "Node value type " + nodeType + "\n";
    }

    public String getPatternForNode(String key) {
        switch (key) {
            case "id":
                return Utils.ID_PATTERN;
            case "title":
                return Utils.NAME_SURNAME_PATTERN;
            case "name":
                return Utils.NAME_SURNAME_PATTERN;
            case "surName":
                return Utils.NAME_SURNAME_PATTERN;
            case "address":
                return Utils.ADDRESS_PATTERN;
            case "birthDay":
                return Utils.DATE_PATTERN;
            case "summary":
                return Utils.SUMMARY_PATTERN;
            case "date":
                return Utils.DATE_PATTERN;
            default:
                return null;

        }
    }


    class JSONFieldInvalidException extends JsonMappingException {
        public JSONFieldInvalidException(String message) {
            super(message);
        }
    }
}
