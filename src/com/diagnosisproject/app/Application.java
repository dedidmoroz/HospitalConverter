package com.diagnosisproject.app;

import com.diagnosisproject.io.FileIO;
import com.diagnosisproject.io.IOContext;
import com.diagnosisproject.io.JsonIO;
import com.diagnosisproject.io.XmlIO;
import com.diagnosisproject.services.PatientService;
import com.diagnosisproject.services.PatientServiceImpl;
import com.diagnosisproject.entities.Diagnosis;
import com.diagnosisproject.entities.Hospital;
import com.diagnosisproject.entities.Patient;
import com.diagnosisproject.utils.Utils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class Application {

    public static final String date1 = "2014-10-11";
    public static final String date2 = "2015-10-12";
    public static final String date3 = "2015-11-12";
    public static final String xmlFilename = "result.xml";
    public static final String jsonFilename = "result.json";
    public static final String textFilename = "result.txt";


    public static void removeFileIfExist(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void main(String[] args) throws JAXBException,
            ParseException,
            IOException {


        Application.removeFileIfExist(textFilename);
        Patient patientPavel = Patient.create()
                .setId(Long.valueOf("1"))
                .setName("Pavel")
                .setSurName("Lol")
                .setAddress("Chernivci")
                .setBirthDay(Utils.PARSER.parseDateTime(date1))
                .build();

        Patient patientVasia = Patient.create()
                .setId(Long.valueOf("1"))
                .setName("Pavel")
                .setSurName("Lol")
                .setAddress("Chernivci")
                .setBirthDay(Utils.PARSER.parseDateTime(date1))
                .build();

        Diagnosis diagnosis1 = Diagnosis.create()
                .setId(Long.valueOf("10201012"))
                .setSummary("Good")
                .setDate(Utils.PARSER.parseDateTime(date2))
                .build();
        Diagnosis diagnosis2 = Diagnosis.create()
                .setId(Long.valueOf("10201022"))
                .setSummary("Bad")
                .setDate(Utils.PARSER.parseDateTime(date3))
                .build();
        PatientService service = new PatientServiceImpl();
        service.addDiagnosis(patientPavel, diagnosis1, diagnosis2);
        service.addDiagnosis(patientVasia,  diagnosis2);

        Hospital hospital = new Hospital(1, "Lol");
        hospital.addPatients(patientPavel, patientVasia);

        IOContext<Hospital> context = new IOContext<Hospital>(new JsonIO());

        context.write(hospital,jsonFilename);
        hospital = context.read(jsonFilename);
        System.out.println(hospital);

        context.setStrategy(new FileIO());
        context.write(hospital, textFilename);
        hospital = context.readWithPattern(textFilename);

        context.setStrategy(new XmlIO<Hospital>());
        context.write(hospital, xmlFilename);
        hospital = context.read(xmlFilename);


    }
};
