package com.example.opd.ViewController;

import com.example.opd.Components.Colors;
import com.example.opd.Components.ListCollector;
import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextField;
import com.codename1.util.Callback;
import static com.example.opd.Components.CustomFont.font;

public class SearchButton implements CustomFont, Colors {

    Button searchButton = new Button("Search");

    String codeNo, opdNo, patNo, diseasesType, diseases, sex, dateFrom, dateTo, ageFrom, ageTo, status;

    TextField codeNoInput, ageToInput, ageFromInput, patIdNo, opdNum;

    ComboBox<String> diseaseTypeSpinner, allDiseaseSpinner;

    RadioButton male, female, other, reAttendence, newPatient, myPatients;

    Container rowsContainer;

    ListCollector list;

    DatabaseHelper database = new DatabaseHelper();

    Callback callback;

    boolean fullTable;

    public SearchButton() {
    }

    public void setSearchButton() {

        searchButton = new Button("Search");

        /*setCodeNo(codeNoInput.getText().trim());
        setAgeFrom(ageFromInput.getText().trim());
        setOpdNo(opdNum.getText().trim());
        setPatNo(patIdNo.getText().trim());
        setDiseasesType(diseaseTypeSpinner.getSelectedItem());
        setDiseases(allDiseaseSpinner.getSelectedItem());
        setSex(male.isSelected() ? "Male" : female.isSelected() ? "Female" : other.isSelected() ? "Other" : null);
        setStatus(reAttendence.isSelected() ? "referral" : newPatient.isSelected() ? "newAttendence" : myPatients.isSelected() ? "refferedTo" : null);

        searchButton.addActionListener(e -> {

            database.searchOPDPatient(rowsContainer, callback, fullTable, list, codeNo, opdNo, patNo, diseasesType, diseases, sex, dateFrom, dateTo, ageFrom, ageTo, status);

        });

        searchButton.getAllStyles().setBgColor(glassColor);
        searchButton.getAllStyles().setBgTransparency(255);
        searchButton.getAllStyles().setFont(font);*/
    }

    public Button getsearchButton() {
        return searchButton;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public String getOpdNo() {
        return opdNo;
    }

    public void setOpdNo(String opdNo) {
        this.opdNo = opdNo;
    }

    public String getPatNo() {
        return patNo;
    }

    public void setPatNo(String patNo) {
        this.patNo = patNo;
    }

    public String getDiseasesType() {
        return diseasesType;
    }

    public void setDiseasesType(String diseasesType) {
        this.diseasesType = diseasesType;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(String ageFrom) {
        this.ageFrom = ageFrom;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Container getRowsContainer() {
        return rowsContainer;
    }

    public void setRowsContainer(Container rowsContainer) {
        this.rowsContainer = rowsContainer;
    }

    public ListCollector getList() {
        return list;
    }

    public void setList(ListCollector list) {
        this.list = list;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public boolean isFullTable() {
        return fullTable;
    }

    public void setFullTable(boolean fullTable) {
        this.fullTable = fullTable;
    }

    public TextField getCodeNoInput() {
        return codeNoInput;
    }

    public void setCodeNoInput(TextField codeNoInput) {
        this.codeNoInput = codeNoInput;
    }

    public TextField getAgeToInput() {
        return ageToInput;
    }

    public void setAgeToInput(TextField ageToInput) {
        this.ageToInput = ageToInput;
    }

    public TextField getAgeFromInput() {
        return ageFromInput;
    }

    public void setAgeFromInput(TextField ageFromInput) {
        this.ageFromInput = ageFromInput;
    }

    public TextField getPatIdNo() {
        return patIdNo;
    }

    public void setPatIdNo(TextField patIdNo) {
        this.patIdNo = patIdNo;
    }

    public TextField getOpdNum() {
        return opdNum;
    }

    public void setOpdNum(TextField opdNum) {
        this.opdNum = opdNum;
    }

    public ComboBox<String> getDiseaseTypeSpinner() {
        return diseaseTypeSpinner;
    }

    public void setDiseaseTypeSpinner(ComboBox<String> diseaseTypeSpinner) {
        this.diseaseTypeSpinner = diseaseTypeSpinner;
    }

    public ComboBox<String> getAllDiseaseSpinner() {
        return allDiseaseSpinner;
    }

    public void setAllDiseaseSpinner(ComboBox<String> allDiseaseSpinner) {
        this.allDiseaseSpinner = allDiseaseSpinner;
    }

    public RadioButton getMale() {
        return male;
    }

    public void setMale(RadioButton male) {
        this.male = male;
    }

    public RadioButton getFemale() {
        return female;
    }

    public void setFemale(RadioButton female) {
        this.female = female;
    }

    public RadioButton getOther() {
        return other;
    }

    public void setOther(RadioButton other) {
        this.other = other;
    }

    public RadioButton getReAttendence() {
        return reAttendence;
    }

    public void setReAttendence(RadioButton reAttendence) {
        this.reAttendence = reAttendence;
    }

    public RadioButton getNewPatient() {
        return newPatient;
    }

    public void setNewPatient(RadioButton newPatient) {
        this.newPatient = newPatient;
    }

    public RadioButton getMyPatients() {
        return myPatients;
    }

    public void setMyPatients(RadioButton myPatients) {
        this.myPatients = myPatients;
    }

}
