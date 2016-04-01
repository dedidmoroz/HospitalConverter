package com.diagnosisproject.io;

import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Hospital;
import com.diagnosisproject.entities.Patient;
import com.diagnosisproject.services.PatientService;
import com.diagnosisproject.services.PatientServiceImpl;
import com.diagnosisproject.utils.Utils;
import java8.util.Iterators;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class FileIO implements ReadWriteIO<Hospital> {
    public static final String PATIENT_PATTERN = "([\\w]+)\\s([\\w]+)\\s\\(([\\w]+)\\)\\s([\\d]{4}-[\\d]{2}-[\\d]{2})\\{([\\w\\W]+)\\5*\\}";
    public static final String DIAGNOSIS_VALUE_PATTERN = "(([\\d]{4}-[\\d]{2}-[\\d]{2}):([\\w]+)),";

    private final List<Diagnosis> diagnosesList = new ArrayList<>();
    private final StringBuffer writeBuffer = new StringBuffer("");
    private final org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final Pattern pattern = Pattern.compile(PATIENT_PATTERN, Pattern.CASE_INSENSITIVE);

    private Matcher matcher;
    private Patient newPatient = null;
    private Diagnosis diagnosis = null;

    @Override
    public void write(Hospital entity, String path) {

        writeBuffer.append(entity.getTitle() + "\n");

        java8.util.Iterators.forEachRemaining(entity.getPatients().iterator(),
                (Patient patient) -> {
                    writeBuffer.append(
                            patient.getName() + " " + patient.getSurName()
                                    + " (" + patient.getAddress()
                                    + ") " + patient.getBirthDay().getYear() + "-" + patient.getBirthDay().getMonthOfYear() + "-" + patient.getBirthDay().getDayOfMonth() + "{");
                    Iterators.forEachRemaining(patient.getDiagnosesList().iterator(),
                            (Diagnosis diagnosis) -> {
                                writeBuffer.append(diagnosis.getDate().getYear() + "-" + diagnosis.getDate().getMonthOfYear() + "-" + diagnosis.getDate().getDayOfMonth() + ":"
                                        + diagnosis.getSummary() + ",");
                            });

                    writeBuffer.append("}\n");
                });


        try {
            Files.write(new File(path).toPath(), writeBuffer.toString()
                    .getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Hospital read(String filePath) {
        Hospital hospital = new Hospital();
        hospital.setId(1);
        try {
            List<String> result = Files.readAllLines(new File(filePath).toPath());
            result.stream().forEach(System.out::println);
            Iterator<String> resultIterator = result.iterator();
            if (resultIterator.hasNext()) {
                hospital.setTitle(resultIterator.next());
            }
            String firstP = resultIterator.next();


            String item = null;
            String patient = null;
            String diagnoses = null;
            while (resultIterator.hasNext()) {
                item = resultIterator.next();
                patient = item.substring(0, item.indexOf("{"));
                diagnoses = item.substring(patient.length() + 1, item.length() - 1);
                String[] patientData = patient.split(" "); //patient part starts
                Patient newPatient = new Patient();
                newPatient.setId(System.currentTimeMillis());
                newPatient.setName(patientData[0]);
                newPatient.setSurName(patientData[1]);
                newPatient.setAddress(patientData[2]);
                newPatient.setBirthDay(format.parseDateTime(patientData[3]));

                String[] diagnoesesArray = diagnoses.split(","); // split diagnoses row and then build Diagnosis object

                diagnosesList.clear();
                for (String i : diagnoesesArray) {
                    String[] diagnosesData = i.split(":");
                    Diagnosis newDiagnosis = new Diagnosis();
                    newDiagnosis.setId(System.currentTimeMillis());
                    newDiagnosis.setDate(format.parseDateTime(diagnosesData[0]));
                    newDiagnosis.setSummary(diagnosesData[1]);
                    diagnosesList.add(newDiagnosis);
                }
                ;
                newPatient.setDiagnosesList(diagnosesList);
                hospital.addPatient(newPatient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hospital;

    }


    @Override
    public Hospital readWithPattern(String filePath) throws IOException {
        Hospital newHospital = new Hospital();
        newHospital.setId(1);

        List<String> result = Files.readAllLines(new File(filePath).toPath());


        Iterator<String> resultIterator = result.iterator();
        if (resultIterator.hasNext()) {
            newHospital.setTitle(resultIterator.next());
        }
        String other = null;
        while (resultIterator.hasNext()) {
            String firstP = resultIterator.next();
            this.matcher = pattern.matcher(firstP);

            if (matcher.find()) {
                newPatient = Patient.create().setId(Long.valueOf(System.currentTimeMillis()))
                        .setName(matcher.group(1))
                        .setSurName(matcher.group(2))
                        .setAddress(matcher.group(3))
                        .setBirthDay(format.parseDateTime(matcher.group(4)))
                        .build();
                other = matcher.group(5);

                matcher = Pattern.compile(DIAGNOSIS_VALUE_PATTERN).matcher(other);

                while (matcher.find()) {
                    diagnosis = Diagnosis.create()
                            .setId(Long.valueOf(System.currentTimeMillis()))
                            .setDate(format.parseDateTime(matcher.group(2)))
                            .setSummary(matcher.group(3))
                            .build();
                    Utils.PATIENT_SERVICE.addDiagnosis(newPatient, diagnosis);
                }
                newHospital.addPatient(newPatient);
            }
        }


        return newHospital;
    }

    class FileInvalidValueException extends Exception{
        public FileInvalidValueException(String message) {
            super(message);
        }
    }
}
