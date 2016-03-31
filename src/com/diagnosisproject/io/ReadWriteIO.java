package com.diagnosisproject.io;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public interface ReadWriteIO<T> {
    public void write(T entity,String path);
    public T read(String filePath);
    default public T readWithPattern(String filePath)throws IOException{
        return null;
    }
}
