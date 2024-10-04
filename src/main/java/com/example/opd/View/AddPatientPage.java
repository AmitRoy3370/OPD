package com.example.opd.View;

import com.example.opd.Reposatory.PatientDatabaseHelper;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Components.CustomFont;
import com.example.opd.Model.Patient;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.util.Callback;
import com.example.opd.ViewController.AddCustomPatient;
import com.example.opd.Components.Colors;
import com.example.opd.Components.ListCollector;
import com.example.opd.Components.SearchIcon;
import com.example.opd.ViewController.searchPatientButton;
import java.util.Map;

public class AddPatientPage extends Lifecycle implements CustomFont, SearchIcon, Colors {

    TextField firstName, lastName, adress, city, age;

    ComboBox<String> sexPicker = new ComboBox<>("Male", "Female", "Other");

    DefaultListModel<String> listModel;

    public Form previousForm, form;

    List list;

    PatientDatabaseHelper database = new PatientDatabaseHelper();

    TextField searchPatient;

    ListCollector list_ = new ListCollector();

    @Override
    public void runApp() {

        form = new Form("Add Pateint", new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar toolbar = new Toolbar();

        form.setToolbar(toolbar);

        toolbar.addCommandToLeftBar("Back", null, e -> {

            if (previousForm != null) {

                previousForm.show();

            }

        });

        Container firstNameContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        firstNameContainer.add(new LabelCreator("First Name").getLabel());

        firstName = new TextField("", "First Name", 34, TextField.ANY);

        firstName.getAllStyles().setMargin(Component.TOP, 5);
        firstName.getAllStyles().setMargin(Component.BOTTOM, 4);
        firstName.getAllStyles().setFont(font);
        //firstName.getAllStyles().setFgColor(glassColor);
        firstName.getAllStyles().setBgTransparency(255);

        firstNameContainer.add(firstName);

        form.add(firstNameContainer);

        Container lastNameContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        lastNameContainer.add(new LabelCreator("Last Name").getLabel());

        lastName = new TextField("", "Last Name", 34, TextField.ANY);

        //lastName.getAllStyles().setMargin(Component.TOP, 5);
        lastName.getAllStyles().setMargin(Component.BOTTOM, 4);
        lastName.getAllStyles().setFont(font);
        //lastName.getAllStyles().setFgColor(glassColor);
        lastName.getAllStyles().setBgTransparency(255);

        lastNameContainer.add(lastName);

        form.add(lastNameContainer);

        Container adressContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        adressContainer.add(new LabelCreator("Adress").getLabel());

        adress = new TextField("", "Adress", 34, TextField.ANY);

        //adress.getAllStyles().setMargin(Component.TOP, 5);
        adress.getAllStyles().setMargin(Component.BOTTOM, 4);
        adress.getAllStyles().setFont(font);
        //adress.getAllStyles().setFgColor(glassColor);
        adress.getAllStyles().setBgTransparency(255);

        adressContainer.add(adress);

        form.add(adressContainer);

        Container cityContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        cityContainer.add(new LabelCreator("City").getLabel());

        city = new TextField("", "City", 34, TextField.ANY);

        //city.getAllStyles().setMargin(Component.TOP, 5);
        city.getAllStyles().setMargin(Component.BOTTOM, 4);
        city.getAllStyles().setFont(font);
        //city.getAllStyles().setFgColor(glassColor);
        city.getAllStyles().setBgTransparency(255);

        cityContainer.add(city);

        form.add(cityContainer);

        Container ageContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        ageContainer.add(new LabelCreator("Age").getLabel());

        age = new TextField("", "Age", 34, TextField.DECIMAL);

        //city.getAllStyles().setMargin(Component.TOP, 5);
        age.getAllStyles().setMargin(Component.BOTTOM, 4);
        age.getAllStyles().setFont(font);
        //age.getAllStyles().setFgColor(glassColor);
        age.getAllStyles().setBgTransparency(255);
        age.setConstraint(TextField.DECIMAL);

        ageContainer.add(age);

        form.add(ageContainer);

        form.add(sexPicker);

        AddCustomPatient addCustomPatient = new AddCustomPatient();

        addCustomPatient.setAdress(adress);
        addCustomPatient.setAge(age);
        addCustomPatient.setCity(city);
        addCustomPatient.setFirstName(firstName);
        addCustomPatient.setLastName(lastName);
        addCustomPatient.setSexPicker(sexPicker);

        addCustomPatient.createButton();

        form.add(addCustomPatient.getAddPatient());

        listModel = new DefaultListModel<>();

        list = new List(listModel);

        list.getAllStyles().setMargin(Component.TOP, 5);
        list.getAllStyles().setMargin(Component.BOTTOM, 5);

        searchPatient = new TextField("", "Patient First / last Name", 34, TextField.ANY);

        Container container_ = new Container(new BoxLayout(BoxLayout.X_AXIS));

        container_.add(searchPatient);

        searchPatientButton searchPatient_ = new searchPatientButton();

        searchPatient_.setForm(form);

        list.addActionListener(e -> {

            int selectedIndex = list.getSelectedIndex();
            String selectPatient = list.getSelectedItem().toString();

            showOptionsDialog(selectPatient, selectedIndex);

        });

        searchPatient_.setList(list);
        searchPatient_.setSearchPatient(searchPatient);
        searchPatient_.setListModel(listModel);

        searchPatient_.createButton();

        container_.add(searchPatient_.getSearch());

        form.add(container_);

        form.add(list);

        form.setScrollable(true);

        form.show();

    }

    private void showOptionsDialog(String selectedItem, int selectedIndex) {

        Dialog optionsDialog = new Dialog("Options");
        optionsDialog.setLayout(new BorderLayout());

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        updateButton.addActionListener((e) -> {
            optionsDialog.dispose();
            updatePatient(selectedItem, selectedIndex); // Call update with index
        });

        deleteButton.addActionListener((e) -> {
            optionsDialog.dispose();
            deletePatient(selectedItem, selectedIndex); // Call delete with index
        });

        Container buttonContainer = new Container(new FlowLayout());
        buttonContainer.add(updateButton);
        buttonContainer.add(deleteButton);

        optionsDialog.add(BorderLayout.CENTER, buttonContainer);
        optionsDialog.showPacked(BorderLayout.CENTER, true);
    }

    private void deletePatient(String selectedItem, int selectedIndex) {
        System.out.println("Deleting patient: " + selectedItem + " at index: " + selectedIndex);

        // Assuming the selectedItem is formatted as "FirstName LastName Address City Age Sex"
        String userFirstName = "";
        String userLastName = "";
        String userAdress = "";
        String userCity = "";
        String userAge = "";
        String sex = "";

        // Manually parsing the selectedItem string
        if (searchPatient.getText().isEmpty()) {

            database.readAllPatient(list_);

        } else {

            database.readAllPatientByFirstName(searchPatient.getText().trim(), list_);
            database.readAllPatientByLastName(searchPatient.getText().trim(), list_);

        }

        Map<String, Object> map = list_.get(selectedIndex);

        System.out.println("size of the map :- " + map == null);

        if (map == null) {

            System.out.println(list_.size());

            return;

        }

        if (!map.isEmpty()) {

            userFirstName = map.get("firstName").toString();
            userLastName = map.get("lastName").toString();
            userAdress = map.get("adress").toString();
            userCity = map.get("city").toString();
            userAge = map.get("age").toString();
            sex = map.get("sex").toString();

        } else {

            System.out.println("The map is empty");
            return;

        }

        // Convert age to integer
        Patient patient = new Patient(userFirstName, userLastName, userAdress, userCity, sex, Integer.parseInt(userAge));

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

        database.deletePatient(patient, callback);
    }

    public void updatePatient(String selectedItem, int selectedIndex) {

        list_.clear();

        // Manually parsing the selectedItem string
        if (searchPatient.getText().isEmpty()) {

            database.readAllPatient(list_);

        } else {

            database.readAllPatientByFirstName(searchPatient.getText().trim(), list_);
            database.readAllPatientByLastName(searchPatient.getText().trim(), list_);

        }

        Map<String, Object> map = list_.get(selectedIndex);

        String userFirstName = "";
        String userLastName = "";
        String userAdress = "";
        String userCity = "";
        String userAge = "";
        String sex = "";
        String _id = "";

        userFirstName = map.get("firstName").toString();
        userLastName = map.get("lastName").toString();
        userAdress = map.get("adress").toString();
        userCity = map.get("city").toString();
        userAge = map.get("age").toString();
        sex = map.get("sex").toString();
        _id = map.get("_id").toString();

        UpdatePatientPage patientUpdate = new UpdatePatientPage();

        patientUpdate.previousAdress = userAdress;
        patientUpdate.previousAge = Integer.parseInt(userAge);
        patientUpdate.previousSex = sex;
        patientUpdate.previousCity = userCity;
        patientUpdate.previousLastName = userLastName;
        patientUpdate.previousFirstName = userFirstName;
        patientUpdate.updatedPatientId = _id;
        patientUpdate.maping = map;
        patientUpdate.previousForm = form;

        patientUpdate.start();

        this.destroy();

    }

}
