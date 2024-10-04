package com.example.opd.Model;

public class OPDWithPatientData {

    String patientId, opdId, diseases, diseasesType, admissionDate, status;

    public OPDWithPatientData() {
    }

    public OPDWithPatientData(String patientId, String opdId, String diseases, String diseasesType, String admissionDate, String status) {
        this.patientId = patientId;
        this.opdId = opdId;
        this.diseases = diseases;
        this.diseasesType = diseasesType;
        this.admissionDate = admissionDate;
        this.status = status;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getOpdId() {
        return opdId;
    }

    public void setOpdId(String opdId) {
        this.opdId = opdId;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getDiseasesType() {
        return diseasesType;
    }

    public void setDiseasesType(String diseasesType) {
        this.diseasesType = diseasesType;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OPDWithPatientData{" + "patientId=" + patientId + ", opdId=" + opdId + ", diseases=" + diseases + ", diseasesType=" + diseasesType + ", admissionDate=" + admissionDate + ", status=" + status + '}';
    }

}
