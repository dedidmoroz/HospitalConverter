package com.diagnosisproject.io;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import com.diagnosisproject.entities.Hospital;

public class JsonIO implements ReadWriteIO<Hospital> {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void write(Hospital entity, String path) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path),entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Hospital read(String filePath) {
		// TODO Auto-generated method stub
		try {
			return mapper.readValue(new File(filePath),Hospital.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
