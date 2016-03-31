package com.diagnosisproject.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pkuz'tc on 3/29/2016.
 */

@XmlRootElement(name = "hospital")
@XmlType(propOrder = {"id", "title", "patients"})
public class Hospital {

    private Integer id;
    private String title;
    private List<Patient> patients = new ArrayList<Patient>();

    public Hospital() {
    }

    public Hospital(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Builder create() {
        return new Hospital().new Builder();
    }

    @XmlAttribute(name = "title", required = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElementWrapper(name = "patients")
    @XmlElement(name = "patient")
    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @XmlAttribute
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO-Me: modeling and make service
    public void addPatients(Patient... patients) {
        this.patients.addAll(Arrays.asList(patients));
    }

    public void addPatient(Patient patients) {
        this.patients.add(patients);
    }

    @Override
    public String toString() {
        return "hospital " + this.hashCode();
    }

    public class Builder {


        public Builder setId(Integer id) {
            Hospital.this.setId(id);
            return this;
        }


        public Builder setTitle(String title) {
            Hospital.this.setTitle(title);
            return this;
        }


        public Builder setPatients(List<Patient> patients) {
            Hospital.this.setPatients(patients);
            return this;
        }

        public Hospital build() {
            return Hospital.this;
        }
    }
}
