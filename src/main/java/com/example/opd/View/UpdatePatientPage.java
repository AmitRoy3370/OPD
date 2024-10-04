package com.example.opd.View;

import com.example.opd.Components.Colors;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Components.CustomFont;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.example.opd.Components.SearchIcon;
import com.example.opd.ViewController.UpdateCustomPatient;
import static com.example.opd.Components.CustomFont.font;
import java.util.Map;

public class UpdatePatientPage extends Lifecycle implements CustomFont, SearchIcon, Colors {

    TextField firstName, lastName, adress, city, age;

    ComboBox<String> sexPicker = new ComboBox<>("Male", "Female", "Other");

    public int selectedIndex = -1, previousAge;

    public String previousFirstName, previousLastName, previousSex, previousAdress, previousCity;

    public String updatedPatientId;

    public Map<String, Object> maping;

    public Form previousForm;

    @Override
    public void runApp() {

        Form form = new Form("Update Pateint", new BoxLayout(BoxLayout.Y_AXIS));

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

        firstName.setText(previousFirstName);

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

        lastName.setText(previousLastName);

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

        adress.setText(previousAdress);

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

        city.setText(previousCity);

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

        age.setText(previousAge + "");

        //city.getAllStyles().setMargin(Component.TOP, 5);
        age.getAllStyles().setMargin(Component.BOTTOM, 4);
        age.getAllStyles().setFont(font);
        //age.getAllStyles().setFgColor(glassColor);
        age.getAllStyles().setBgTransparency(255);
        age.setConstraint(TextField.DECIMAL);

        ageContainer.add(age);

        form.add(ageContainer);

        String secondSex = previousSex.equals("Male") ? "Female" : previousSex.equals("Female") ? "Male" : previousSex.equals("Other") ? "Male" : "Female";

        String thirdSex = secondSex.equals("Male") ? "Other" : secondSex.equals("Female") ? "Other" : "Male";

        sexPicker = new ComboBox<>(previousSex, secondSex, thirdSex);

        form.add(sexPicker);

        UpdateCustomPatient addCustomPatient = new UpdateCustomPatient();

        addCustomPatient.setAdress(adress);
        addCustomPatient.setAge(age);
        addCustomPatient.setCity(city);
        addCustomPatient.setFirstName(firstName);
        addCustomPatient.setLastName(lastName);
        addCustomPatient.setSexPicker(sexPicker);
        addCustomPatient._id = maping.get("_id").toString();

        addCustomPatient.createButton();

        form.add(addCustomPatient.getAddPatient());

        Button addOPD = new Button("Admit OPD");

        addOPD.addActionListener(e -> {

            PatientOPDAdmit admit = new PatientOPDAdmit();

            admit.previousForm = form;

            admit.map = maping;

            admit.start();

        });

        addOPD.getAllStyles().setBgColor(glassBlue);
        addOPD.getAllStyles().setFgColor(glassBlack);
        addOPD.getAllStyles().setBgTransparency(255);
        addOPD.getAllStyles().setFont(font);

        form.add(addOPD);

        form.setScrollable(true);

        form.show();

    }

}
