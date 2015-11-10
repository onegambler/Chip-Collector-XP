package com.chipcollector.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROPERTIES")
public class Property {

    @Id
    private long id;

    private String key;
    private String value;

    public String getValue() {
        return value;
    }
}
