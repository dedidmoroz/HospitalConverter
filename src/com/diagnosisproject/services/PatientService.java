package com.diagnosisproject.services;

import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Patient;
import org.joda.time.DateTime;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public interface PatientService {
    public void addDiagnosis(Patient entity,Diagnosis ... diagnosises);
    public Diagnosis getPatientDiagnosisByDate(Patient patient,DateTime date);
}
