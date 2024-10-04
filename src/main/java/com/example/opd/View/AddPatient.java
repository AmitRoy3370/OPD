package com.example.opd.View;

import com.example.opd.Components.CloseButton;
import com.example.opd.ViewController.AddPatientButton;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Components.CustomFont;
import com.codename1.system.Lifecycle;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.spinner.TimeSpinner;

public class AddPatient extends Lifecycle implements CustomFont {

    public Form form, previousForm;

    RadioButton newAttendence, refferal, refferedTo, male, female, other;

    TextField diagonisisInput, diagonisisInput2, diagonisisInput3, ageInput;

    ButtonGroup groupOfButtons, sexButtonGroup;

    Picker attendenceDate, attendenceTime;

    TimeSpinner timeSpinner;

    AddPatientButton addPatientButton;

    Button visibleCloseButton;

    CloseButton closeButton;

    ComboBox<String> diagonisisSpinner, diagonisisSpinner2, diagonisisSpinner3, diseaseTypeSpinner;

    @Override
    public void runApp() {

        form = new Form("New OPD Registration", new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar toolbar = new Toolbar();

        form.setToolbar(toolbar);

        toolbar.addCommandToLeftBar("Back", null, e -> {

            if (previousForm != null) {

                previousForm.show();

            }

        });

        Container upperLayerButtons = new Container(new FlowLayout());

        newAttendence = new RadioButton("New Attendence");
        refferal = new RadioButton("Refferal");
        refferedTo = new RadioButton("RefferedTo");

        groupOfButtons = new ButtonGroup(newAttendence, refferal, refferedTo);

        upperLayerButtons.addAll(newAttendence, refferal, refferedTo);

        form.add(upperLayerButtons);

        form.add(new LabelCreator("Attendence Date").getLabel());

        attendenceDate = new Picker();

        attendenceDate.setType(Display.PICKER_TYPE_DATE);
        attendenceDate.setEnabled(true);

        Container dateContainer = new Container(new FlowLayout());

        dateContainer.add(attendenceDate);

        attendenceTime = new Picker();

        attendenceTime.setType(Display.PICKER_TYPE_TIME);

        dateContainer.add(attendenceTime);

        form.add(dateContainer);

        Container diagonisisContainer = new Container(new FlowLayout());

        TextField diseases = new TextField("Diseases Type", "", 34, TextField.ANY);

        diseases.setEditable(false);

        form.add(diseases);

        diseaseTypeSpinner = new ComboBox<>("Diseases-1", "Diseases-2", "Diseases-3", "Diseases-4", "Diseases-5", "Diseases-6", "Diseases-7", "Diseases-8", "Diseases-9", "Diseases-10");

        diseaseTypeSpinner.getAllStyles().setMargin(Component.BOTTOM, 2);

        form.add(diseaseTypeSpinner);

        diagonisisContainer.add(new LabelCreator("Diagonisis").getLabel());

        diagonisisInput = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput.getAllStyles().setFont(font);

        diagonisisContainer.add(diagonisisInput);

        diagonisisInput.setEditable(false);

        form.add(diagonisisContainer);

        diagonisisSpinner = new ComboBox<>("Diagonisis", "Diagonisis 1", "Diagonisis 2", "Diagonisis 3", "Diagonisis 4", "Diagonisis 5", "Diagonisis 6", "Diagonisis 7", "Diagonisis 8", "Diagonisis 9", "Diagonisis 10");

        form.add(diagonisisSpinner);

        Container diagonisisContainer2 = new Container(new FlowLayout());

        diagonisisContainer2.add(new LabelCreator("Diagonisis 2").getLabel());

        diagonisisInput2 = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput2.getAllStyles().setFont(font);

        diagonisisContainer2.add(diagonisisInput2);

        diagonisisInput2.setEditable(false);

        form.add(diagonisisContainer2);

        diagonisisSpinner2 = new ComboBox<>("Diagonisis", "Diagonisis 1", "Diagonisis 2", "Diagonisis 3", "Diagonisis 4", "Diagonisis 5", "Diagonisis 6", "Diagonisis 7", "Diagonisis 8", "Diagonisis 9", "Diagonisis 10");

        form.add(diagonisisSpinner2);

        Container diagonisisContainer3 = new Container(new FlowLayout());

        diagonisisContainer3.add(new LabelCreator("Diagonisis 3").getLabel());

        diagonisisInput3 = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput3.getAllStyles().setFont(font);

        diagonisisContainer3.add(diagonisisInput3);

        diagonisisInput3.setEditable(false);

        form.add(diagonisisContainer3);

        diagonisisSpinner3 = new ComboBox<>("Diagonisis", "Diagonisis 1", "Diagonisis 2", "Diagonisis 3", "Diagonisis 4", "Diagonisis 5", "Diagonisis 6", "Diagonisis 7", "Diagonisis 8", "Diagonisis 9", "Diagonisis 10");

        form.add(diagonisisSpinner3);

        Container ageContainer = new Container(new FlowLayout());

        ageContainer.add(new LabelCreator("Age").getLabel());

        ageInput = new TextField("", "Age", 34, TextField.DECIMAL);

        ageInput.getAllStyles().setFont(font);

        ageContainer.add(ageInput);

        form.add(ageContainer);

        Container sexContainer = new Container(new FlowLayout());

        male = new RadioButton("Male");
        female = new RadioButton("Female");
        other = new RadioButton("Other");

        sexButtonGroup = new ButtonGroup(male, female, other);

        sexContainer.addAll(male, female, other);

        sexContainer.getAllStyles().setMarginLeft(Display.getInstance().getDisplayWidth() / 2);

        form.add(sexContainer);

        closeButton = new CloseButton();

        closeButton.setCloseButton();

        visibleCloseButton = closeButton.getCloseButton();

        visibleCloseButton.addActionListener(e -> {

            Display.getInstance().exitApplication();

        });

        Container lowerLayerButtonContainer = new Container(new FlowLayout());

        addPatientButton = new AddPatientButton();

        addPatientButton.setAgeInput(ageInput);
        addPatientButton.setDiagonisisSpinner(diagonisisSpinner);
        addPatientButton.setDiagonisisSpinner1(diagonisisSpinner2);
        addPatientButton.setDiagonisisSpinner2(diagonisisSpinner3);
        addPatientButton.setAttendenceDate(attendenceDate);
        addPatientButton.setAttendenceTime(attendenceTime);
        addPatientButton.setMale(male);
        addPatientButton.setFemale(female);
        addPatientButton.setOther(other);
        addPatientButton.setNewAttendence(newAttendence);
        addPatientButton.setRefferal(refferal);
        addPatientButton.setRefferedTo(refferedTo);
        addPatientButton.setDiseasesTypeSpinner(diseaseTypeSpinner);

        addPatientButton.setAddPatientButton();

        lowerLayerButtonContainer.addAll(addPatientButton.getaddPatientButton(),
                visibleCloseButton);

        lowerLayerButtonContainer.getAllStyles().setMarginLeft(Display.getInstance().getDisplayWidth() / 2);

        form.add(lowerLayerButtonContainer);

        form.setScrollableX(true);
        form.setScrollableY(true);

        form.show();

    }

}
