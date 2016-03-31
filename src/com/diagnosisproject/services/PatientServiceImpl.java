package com.diagnosisproject.services;

import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Patient;
import org.joda.time.DateTime;

import java.util.Arrays;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class PatientServiceImpl implements PatientService {

    @Override
    public void addDiagnosis(Patient patient, Diagnosis... diagnosises) {
        patient.getDiagnosesList().addAll(Arrays.asList(diagnosises));
    }

    @Override
    public Diagnosis getPatientDiagnosisByDate(Patient patient, DateTime date) {
        for(Diagnosis diagnosis:patient.getDiagnosesList()){
            if(diagnosis.getDate().equals(date)){
                return diagnosis;
            }
        }
        return null;

    }
}
