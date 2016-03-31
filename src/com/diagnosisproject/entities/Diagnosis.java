package com.diagnosisproject.entities;

import com.diagnosisproject.adapters.DateAdapter;
import com.diagnosisproject.adapters.JsonDateDeserialiser;
import com.diagnosisproject.adapters.JsonDateSerialiser;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by pkuz'tc on 3/29/2016.
 */

public class Diagnosis {
    private Long id;
    private DateTime date;
    private String summary;


    public Diagnosis() {
    }

    public Diagnosis(Long id, DateTime date, String summary) {
        this.id = id;
        this.date = date;
        this.summary = summary;
    }

    public static Diagnosis.Builder create() {
        return new Diagnosis().new Builder();

    }

    public Long getId() {
        return id;
    }

    @XmlAttribute(name = "id", required = true)
    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getDate() {
        return date;
    }

    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    @JsonSerialize(using = JsonDateSerialiser.class)
    @JsonDeserialize(using = JsonDateDeserialiser.class)
    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    @XmlElement
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public class Builder {

        public Builder setId(Long id) {
            Diagnosis.this.setId(id);
            return this;
        }

        public Builder setDate(DateTime date) {
            Diagnosis.this.setDate(date);
            return this;
        }

        public Builder setSummary(String summary) {
            Diagnosis.this.setSummary(summary);
            return this;
        }

        public Diagnosis build() {
            return Diagnosis.this;
        }
    }
}

