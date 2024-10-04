package com.example.opd.ViewController;

import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.example.opd.Model.OPDData;
import java.text.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Dialog;
import com.codename1.ui.RadioButton;

import com.codename1.ui.TextField;
import com.codename1.ui.spinner.Picker;
import com.codename1.util.Callback;
import com.example.opd.Components.Colors;
import static com.example.opd.Components.CustomFont.font;

public class AddPatientButton implements CustomFont, Colors {

    Button addPatientButton = new Button("Add");

    ComboBox<String> diagonisisSpinner, diagonisisSpinner2, diagonisisSpinner3, diseasesTypeSpinner;

    Picker attendenceDate, attendenceTime;

    TextField ageInput;

    OPDData opd;

    RadioButton male, female, other, newAttendence, refferal, refferedTo;

    DatabaseHelper database = new DatabaseHelper();

    public void setAddPatientButton() {

        Callback callback = new Callback() {
            @Override
            public void onSucess(Object value) {

                Dialog.show("Added data update", "Data added successfully", "OK", "Cancel");

            }

            @Override
            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {

                Dialog.show("Added data update", errorMessage, "OK", "Cancel");

            }
        };

        addPatientButton.addActionListener(e -> {

            setOpd();

            if (opd.getAttendenceDate().equals("")
                    || opd.getDiagonisis().equals("")
                    || opd.getDiagonisis1().equals("")
                    || opd.getDiagonisis2().equals("")
                    || opd.getSex().equals("")) {

                Dialog.show("Added data update", "Please enter all the value correctly", "OK", "Cancel");

            } else {

                database.addOPD(opd, callback);

            }

        });

        addPatientButton.getAllStyles().setBgColor(glassColor);
        addPatientButton.getAllStyles().setBgTransparency(255);
        addPatientButton.getAllStyles().setFont(font);

    }

    public Button getaddPatientButton() {
        return addPatientButton;
    }

    public void setOpd() {

        String typeOfPatient = newAttendence.isSelected() ? "newAttendece" : refferal.isSelected() ? "refferal" : refferedTo.isSelected() ? "refferedTo" : "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String formattedDate = dateFormat.format(attendenceDate.getDate());
        String formattedTime = timeFormat.format(attendenceTime.getTime());

        String selectedDate = (String) (formattedDate + "," + formattedTime);

        String selectedDiagonisis = diagonisisSpinner.getSelectedItem();
        String selectedDiagonisis1 = diagonisisSpinner2.getSelectedItem();
        String selectedDiagonisis2 = diagonisisSpinner3.getSelectedItem();

        String choosenSex = male.isSelected() ? "Male" : female.isSelected() ? "Female" : other.isSelected() ? "Other" : "";

        int inputedAge = perfectAge(ageInput.getText()) ? Integer.parseInt(ageInput.getText()) : 0;

        String diseasesType = diseasesTypeSpinner.getSelectedItem();

        opd = new OPDData(typeOfPatient, selectedDate, diseasesType, selectedDiagonisis, selectedDiagonisis1, selectedDiagonisis2, choosenSex, inputedAge);

    }

    public ComboBox<String> getDiagonisisSpinner() {
        return diagonisisSpinner;
    }

    public void setDiagonisisSpinner(ComboBox<String> diagonisisSpinner) {
        this.diagonisisSpinner = diagonisisSpinner;
    }

    public ComboBox<String> getDiagonisisSpinner1() {
        return diagonisisSpinner2;
    }

    public void setDiagonisisSpinner1(ComboBox<String> diagonisisSpinner1) {
        this.diagonisisSpinner2 = diagonisisSpinner1;
    }

    public ComboBox<String> getDiagonisisSpinner2() {
        return diagonisisSpinner3;
    }

    public void setDiagonisisSpinner2(ComboBox<String> diagonisisSpinner2) {
        this.diagonisisSpinner3 = diagonisisSpinner2;
    }

    public Picker getAttendenceDate() {
        return attendenceDate;
    }

    public void setAttendenceDate(Picker attendenceDate) {
        this.attendenceDate = attendenceDate;
    }

    public Picker getAttendenceTime() {
        return attendenceTime;
    }

    public void setAttendenceTime(Picker attendenceTime) {
        this.attendenceTime = attendenceTime;
    }

    public TextField getAgeInput() {
        return ageInput;
    }

    public void setAgeInput(TextField ageInput) {
        this.ageInput = ageInput;
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

    public RadioButton getNewAttendence() {
        return newAttendence;
    }

    public void setNewAttendence(RadioButton newAttendence) {
        this.newAttendence = newAttendence;
    }

    public RadioButton getRefferal() {
        return refferal;
    }

    public void setRefferal(RadioButton refferal) {
        this.refferal = refferal;
    }

    public RadioButton getRefferedTo() {
        return refferedTo;
    }

    public void setRefferedTo(RadioButton refferedTo) {
        this.refferedTo = refferedTo;
    }

    private boolean perfectAge(String age) {

        try {

            int num = Integer.parseInt(age);

        } catch (Exception e) {

            return false;

        }

        return true;

    }

    public ComboBox<String> getDiagonisisSpinner3() {
        return diagonisisSpinner3;
    }

    public void setDiagonisisSpinner3(ComboBox<String> diagonisisSpinner3) {
        this.diagonisisSpinner3 = diagonisisSpinner3;
    }

    public ComboBox<String> getDiseasesTypeSpinner() {
        return diseasesTypeSpinner;
    }

    public void setDiseasesTypeSpinner(ComboBox<String> diseasesTypeSpinner) {
        this.diseasesTypeSpinner = diseasesTypeSpinner;
    }

}
