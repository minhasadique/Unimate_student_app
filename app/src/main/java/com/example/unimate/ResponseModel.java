package com.example.unimate;

import java.util.List;

public class ResponseModel {
    private String status;
    private List<Startup> data;

    public String getStatus() {
        return status;
    }

    public List<Startup> getData() {
        return data;
    }
}
