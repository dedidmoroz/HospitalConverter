package com.diagnosisproject.io;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by pkuz'tc on 3/29/2016.
 */
public class IOContext<T> {
    private ReadWriteIO<T> strategy;

    public IOContext(ReadWriteIO<T> strategy) {
        this.strategy = strategy;
    }

    public ReadWriteIO<T> getStrategy() {
        return strategy;
    }

    public void setStrategy(ReadWriteIO<T> strategy) {
        this.strategy = strategy;
    }

    public void write(T entity, String path) throws JAXBException {
        this.strategy.write(entity, path);
    }

    public T read(String path) throws JAXBException {
        return this.strategy.read(path);
    }

    public T readWithPattern(String path) throws IOException {
        return this.strategy.readWithPattern(path);
    }



}
