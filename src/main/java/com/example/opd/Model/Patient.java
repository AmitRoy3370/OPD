package com.example.opd.Model;

public class Patient {

    String firstName, lastName, adress, city, sex;
    int age;

    public Patient(String firstName, String lastName, String adress, String city, String sex, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress = adress;
        this.city = city;
        this.sex = sex;
        this.age = age;
    }

    public Patient() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    @Override
    public String toString() {
        return "Patient{" + "firstName=" + firstName + ", lastName=" + lastName + ", adress=" + adress + ", city=" + city + ", sex=" + sex + ", age=" + age + '}';
    }

}
