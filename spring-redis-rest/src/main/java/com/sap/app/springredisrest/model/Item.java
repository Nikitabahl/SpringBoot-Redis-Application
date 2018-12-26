package com.sap.app.springredisrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class Item implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("time")
    private String time;

    public Item(String id, String time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
