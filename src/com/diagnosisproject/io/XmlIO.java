package com.diagnosisproject.io;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class XmlIO<Hospital> implements ReadWriteIO<Hospital> {

    @Override
    public void write(Hospital entity, String path) {
        try {
            JAXBContext xmlContext = JAXBContext.newInstance(com.diagnosisproject.entities.Hospital.class);
            Marshaller marshaller = xmlContext.createMarshaller();
            marshaller.marshal(entity, new FileOutputStream(path));
        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Hospital read(String filePath) {
        try {
            JAXBContext xmlContext = JAXBContext.newInstance(com.diagnosisproject.entities.Hospital.class);
            Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
            return (Hospital) unmarshaller.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
