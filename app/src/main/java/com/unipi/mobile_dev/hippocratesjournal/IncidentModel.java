package com.unipi.mobile_dev.hippocratesjournal;

public class IncidentModel {
    private String name;
    private String dateOfExamination;
    private String diagnosis;

    public IncidentModel() {}

    public IncidentModel(String name, String dateOfExamination, String diagnosis) {
        this.name = name;
        this.dateOfExamination = dateOfExamination;
        this.diagnosis = diagnosis;
    }

    public String getName() {
        return name;
    }

    public String getDateOfExamination() {
        return dateOfExamination;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}
