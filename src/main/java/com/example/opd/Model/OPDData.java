package com.example.opd.Model;

public class OPDData {

    private String patientType, attendenceDate, diseasesType, diagonisis, diagonisis1, diagonisis2, sex;
    private int age;

    public OPDData(String patientType, String attendenceDate, String diseasesType, String diagonisis, String diagonisis1, String diagonisis2, String sex, int age) {
        this.patientType = patientType;
        this.attendenceDate = attendenceDate;
        this.diagonisis = diagonisis;
        this.diagonisis1 = diagonisis1;
        this.diagonisis2 = diagonisis2;
        this.sex = sex;
        this.age = age;
        this.diseasesType = diseasesType;
    }

    public OPDData() {
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getAttendenceDate() {
        return attendenceDate;
    }

    public void setAttendenceDate(String attendenceDate) {
        this.attendenceDate = attendenceDate;
    }

    public String getDiagonisis() {
        return diagonisis;
    }

    public void setDiagonisis(String diagonisis) {
        this.diagonisis = diagonisis;
    }

    public String getDiagonisis1() {
        return diagonisis1;
    }

    public void setDiagonisis1(String diagonisis1) {
        this.diagonisis1 = diagonisis1;
    }

    public String getDiagonisis2() {
        return diagonisis2;
    }

    public void setDiagonisis2(String diagonisis2) {
        this.diagonisis2 = diagonisis2;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDiseasesType() {
        return diseasesType;
    }

    public void setDiseasesType(String diseasesType) {
        this.diseasesType = diseasesType;
    }

    @Override
    public String toString() {
        return "OPDData{" + "patientType=" + patientType + ", attendenceDate=" + attendenceDate + ", diseasesType=" + diseasesType + ", diagonisis=" + diagonisis + ", diagonisis1=" + diagonisis1 + ", diagonisis2=" + diagonisis2 + ", sex=" + sex + ", age=" + age + '}';
    }

}
