package com.diagnosisproject.entities;

import com.diagnosisproject.adapters.DateAdapter;
import com.diagnosisproject.adapters.JsonDateDeserialiser;
import com.diagnosisproject.adapters.JsonDateSerialiser;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Kuz on 3/29/2016.
 */
@XmlType(propOrder = {"id", "name", "surName", "address", "birthDay", "diagnosesList"})
public class Patient {
    private Long id;
    private String name;
    private String surName;
    private String address;
    private DateTime birthDay;
    private List<Diagnosis> diagnosesList = new ArrayList<Diagnosis>();

    public Patient() {
    }

    public static Builder create() {
        return new Patient().new Builder();
    }

    public Long getId() {
        return id;
    }

    @XmlAttribute(required = true, name = "id")
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public DateTime getBirthDay() {
        return birthDay;
    }

    @XmlElement(name = "birthDay")
    @XmlJavaTypeAdapter(DateAdapter.class)
    @JsonSerialize(using = JsonDateSerialiser.class)
    @JsonDeserialize(using = JsonDateDeserialiser.class)
    public void setBirthDay(DateTime birthDay) {
        this.birthDay = birthDay;
    }

    public List<Diagnosis> getDiagnosesList() {
        return diagnosesList;
    }

    @XmlElementWrapper(name = "diagnoses")
    @XmlElement(name = "diagnosis")
    public void setDiagnosesList(List<Diagnosis> diagnosesList) {
        this.diagnosesList = diagnosesList;
    }

    public String getSurName() {
        return surName;
    }

    @XmlElement(name = "surName")
    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getAddress() {
        return address;
    }

    @XmlElement(name = "address")
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return this.name + " " + this.birthDay.toString();
    }

    public Integer tellAge() {
        return new DateTime().getYear() - getBirthDay().getYear();
    }

    public class Builder {

        public Builder() {
        }

        public Builder setId(Long id) {
            Patient.this.setId(id);
            return this;
        }

        public Builder setName(String name) {
            Patient.this.setName(name);
            return this;
        }

        public Builder setSurName(String surName) {
            Patient.this.setSurName(surName);
            return this;
        }

        public Builder setAddress(String address) {
            Patient.this.setAddress(address);
            return this;
        }

        public Builder setBirthDay(DateTime birthDay) {
            Patient.this.setBirthDay(birthDay);
            return this;
        }

        public Builder setDiagnosesList(List<Diagnosis> diagnosesList) {
            Patient.this.setDiagnosesList(diagnosesList);
            return this;
        }

        public Patient build() {
            return Patient.this;
        }
    }
}
