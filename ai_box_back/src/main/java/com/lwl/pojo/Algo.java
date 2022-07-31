package com.lwl.pojo;

public class Algo {
    private String name;
    private String note;
    private String uri;

    public Algo(String name, String note, String uri){
        this.name = name;
        this.note = note;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
