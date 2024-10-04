
package com.example.opd.ViewController;

import com.example.opd.Components.Colors;
import com.example.opd.Reposatory.PatientDatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.example.opd.Model.Patient;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextField;
import com.codename1.util.Callback;
import static com.example.opd.Components.Colors.glassBlue;
import static com.example.opd.Components.CustomFont.font;
import java.util.HashMap;
import java.util.Map;

public class UpdateCustomPatient implements Colors, CustomFont {

    Button addPatient = new Button("Update Patient");

    TextField firstName, lastName, adress, city, age;

    ComboBox<String> sexPicker = new ComboBox<>("Male", "Female", "Other");

    Patient patient;

    public String _id;
    
    PatientDatabaseHelper database = new PatientDatabaseHelper();

    public void createButton() {

        addPatient.getAllStyles().setFont(font);

        addPatient.addActionListener(e -> {

            String first_name = firstName.getText().trim();
            String last_name = lastName.getText().trim();
            String patientAdress = adress.getText().trim();
            String patientCity = city.getText().trim();
            String sex = sexPicker.getSelectedItem();

            if (first_name.isEmpty()
                    || last_name.isEmpty()
                    || patientAdress.isEmpty()
                    || patientCity.isEmpty()) {

                Dialog.show("Error", "Please fill all the field", "OK", "Cancel");

                return;

            }

            int ageInput = 0;

            try {

                ageInput += Integer.parseInt(age.getText().trim());

            } catch (NumberFormatException | NullPointerException ex) {

                Dialog.show("Error", "Please enter a perfect number", "OK", "Cancel");

                return;

            }

            patient = new Patient(first_name, last_name, patientAdress, patientCity, sex, ageInput);

            Callback callback = new Callback() {
                @Override
                public void onSucess(Object t) {

                    Dialog.show("Database Info", t.toString(), "OK", "Cancel");

                }

                @Override
                public void onError(Object o, Throwable thrwbl, int i, String string) {

                    Dialog.show("Error", string, "OK", "Cancel");

                }

            };

            Map<String, Object> map = new HashMap<>();
            
            map.put("firstName", patient.getFirstName());
            map.put("lastName", patient.getLastName());
            map.put("adress", patient.getAdress());
            map.put("city", patient.getCity());
            map.put("age", patient.getAge());
            map.put("sex", patient.getSex());
            
            database.updatePatient(_id, map, callback);
            
        });

        addPatient.getAllStyles().setBgColor(glassBlue);
        addPatient.getAllStyles().setFgColor(glassBlack);
        addPatient.getAllStyles().setBgTransparency(255);
        addPatient.getAllStyles().setFont(font);

    }

    public TextField getFirstName() {
        return firstName;
    }

    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    public TextField getAdress() {
        return adress;
    }

    public void setAdress(TextField adress) {
        this.adress = adress;
    }

    public TextField getCity() {
        return city;
    }

    public void setCity(TextField city) {
        this.city = city;
    }

    public TextField getAge() {
        return age;
    }

    public void setAge(TextField age) {
        this.age = age;
    }

    public ComboBox<String> getSexPicker() {
        return sexPicker;
    }

    public void setSexPicker(ComboBox<String> sexPicker) {
        this.sexPicker = sexPicker;
    }

    public Button getAddPatient() {
        return addPatient;
    }

}

