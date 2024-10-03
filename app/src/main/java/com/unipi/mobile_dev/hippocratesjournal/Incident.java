package com.unipi.mobile_dev.hippocratesjournal;

public class Incident {
    private String name;
    private String dateOfBirth;
    private String dateOfExamination;
    private String gender;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String key; // Firebase key - automatically generated

    public Incident() {

    }

    public Incident(String name, String dateOfBirth, String dateOfExamination, String gender, String symptoms, String diagnosis, String prescription) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfExamination = dateOfExamination;
        this.gender = gender;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfExamination() {
        return dateOfExamination;
    }

    public void setDateOfExamination(String dateOfExamination) {
        this.dateOfExamination = dateOfExamination;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
