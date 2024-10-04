package com.example.opd.View;

import com.example.opd.ViewController.UpdateOpdButton;
import com.example.opd.Components.CloseButton;
import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Components.CustomFont;
import com.codename1.system.Lifecycle;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
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
import com.codename1.util.Callback;
import com.example.opd.Components.OPDListCollector;
import static com.example.opd.Components.CustomFont.font;
import java.util.Date;
import java.util.Map;

public class UpdateOpdDataPage extends Lifecycle implements CustomFont {

    public Form form, previousForm;

    public String opdId;

    RadioButton newAttendence, refferal, refferedTo, male, female, other;

    TextField diagonisisInput, diagonisisInput2, diagonisisInput3, ageInput;

    ButtonGroup groupOfButtons, sexButtonGroup;

    Picker attendenceDate, attendenceTime;

    TimeSpinner timeSpinner;

    UpdateOpdButton addPatientButton;

    Button visibleCloseButton;

    CloseButton closeButton;

    ComboBox<String> diagonisisSpinner, diagonisisSpinner2, diagonisisSpinner3, diseaseTypeSpinner;

    DatabaseHelper database = new DatabaseHelper();

    OPDListCollector opdList = new OPDListCollector();

    @Override
    public void runApp() {

        database.readAllOPD(opdList,
                new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database update", i + " " + string, "OK", "Cancel");

            }

        }, opdId);

        Map<String, Object> map = opdList.get(0);

        System.out.println("received data :- " + map);

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

        if (map.get("patientType").toString().equals("newAttendece")) {

            newAttendence.setSelected(true);

        } else if (map.get("patientType").toString().equals("refferal")) {

            refferal.setSelected(true);

        } else {

            refferedTo.setSelected(true);

        }

        upperLayerButtons.addAll(newAttendence, refferal, refferedTo);

        form.add(upperLayerButtons);

        form.add(new LabelCreator("Attendence Date").getLabel());

        attendenceDate = new Picker();

        attendenceDate.setType(Display.PICKER_TYPE_DATE);
        attendenceDate.setEnabled(true);

        String receivedDate = map.get("attendanceDate").toString();

        StringBuilder date = new StringBuilder();
        StringBuilder hour = new StringBuilder();
        StringBuilder min = new StringBuilder();

        for (int i = 0, dateAdd = 1, hourAdd = 1; i < receivedDate.length(); ++i) {

            if (receivedDate.charAt(i) == ',') {

                dateAdd = 0;

                continue;

            }

            if (dateAdd == 1) {

                date.append(receivedDate.charAt(i));

            } else {

                if (receivedDate.charAt(i) == ':') {

                    hourAdd = 0;
                    continue;

                }

                if (hourAdd == 1) {

                    hour.append(receivedDate.charAt(i));

                } else {

                    min.append(receivedDate.charAt(i));

                }

            }

        }

        attendenceDate.setText(date.toString());

        Container dateContainer = new Container(new FlowLayout());

        dateContainer.add(attendenceDate);

        attendenceTime = new Picker();

        attendenceTime.setType(Display.PICKER_TYPE_TIME);

        attendenceTime.setTime(Integer.parseInt(hour.toString()), Integer.parseInt(min.toString()));

        dateContainer.add(attendenceTime);

        form.add(dateContainer);

        Container diagonisisContainer = new Container(new FlowLayout());

        TextField diseases = new TextField("Diseases Type", "", 34, TextField.ANY);

        diseases.setEditable(false);

        form.add(diseases);

        diseaseTypeSpinner = new ComboBox<>();

        diseaseTypeSpinner.addItem(map.get("diseasesType").toString());

        diseaseTypeSpinner.addItem("Diseases-1");
        diseaseTypeSpinner.addItem("Diseases-2");
        diseaseTypeSpinner.addItem("Diseases-3");
        diseaseTypeSpinner.addItem("Diseases-4");
        diseaseTypeSpinner.addItem("Diseases-5");
        diseaseTypeSpinner.addItem("Diseases-6");
        diseaseTypeSpinner.addItem("Diseases-7");
        diseaseTypeSpinner.addItem("Diseases-8");
        diseaseTypeSpinner.addItem("Diseases-9");
        diseaseTypeSpinner.addItem("Diseases-10");

        diseaseTypeSpinner.getAllStyles().setMargin(Component.BOTTOM, 2);

        form.add(diseaseTypeSpinner);

        diagonisisContainer.add(new LabelCreator("Diagonisis").getLabel());

        diagonisisInput = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput.getAllStyles().setFont(font);

        diagonisisContainer.add(diagonisisInput);

        diagonisisInput.setEditable(false);

        form.add(diagonisisContainer);

        diagonisisSpinner = new ComboBox<>();

        diagonisisSpinner.addItem(map.get("diagonisis").toString());

        //diagonisisSpinner3.addItem(map.get("diagonisis2").toString());
        diagonisisSpinner.addItem("Diagonisis");
        diagonisisSpinner.addItem("Diagonisis 1");
        diagonisisSpinner.addItem("Diagonisis 2");
        diagonisisSpinner.addItem("Diagonisis 3");
        diagonisisSpinner.addItem("Diagonisis 4");
        diagonisisSpinner.addItem("Diagonisis 5");
        diagonisisSpinner.addItem("Diagonisis 6");
        diagonisisSpinner.addItem("Diagonisis 7");
        diagonisisSpinner.addItem("Diagonisis 8");
        diagonisisSpinner.addItem("Diagonisis 9");
        diagonisisSpinner.addItem("Diagonisis 10");

        form.add(diagonisisSpinner);

        Container diagonisisContainer2 = new Container(new FlowLayout());

        diagonisisContainer2.add(new LabelCreator("Diagonisis 2").getLabel());

        diagonisisInput2 = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput2.getAllStyles().setFont(font);

        diagonisisContainer2.add(diagonisisInput2);

        diagonisisInput2.setEditable(false);

        form.add(diagonisisContainer2);

        diagonisisSpinner2 = new ComboBox<>();

        diagonisisSpinner2.addItem(map.get("diagonisis1").toString());
        //diagonisisSpinner3.addItem(map.get("diagonisis2").toString());
        diagonisisSpinner2.addItem("Diagonisis");
        diagonisisSpinner2.addItem("Diagonisis 1");
        diagonisisSpinner2.addItem("Diagonisis 2");
        diagonisisSpinner2.addItem("Diagonisis 3");
        diagonisisSpinner2.addItem("Diagonisis 4");
        diagonisisSpinner2.addItem("Diagonisis 5");
        diagonisisSpinner2.addItem("Diagonisis 6");
        diagonisisSpinner2.addItem("Diagonisis 7");
        diagonisisSpinner2.addItem("Diagonisis 8");
        diagonisisSpinner2.addItem("Diagonisis 9");
        diagonisisSpinner2.addItem("Diagonisis 10");

        form.add(diagonisisSpinner2);

        Container diagonisisContainer3 = new Container(new FlowLayout());

        diagonisisContainer3.add(new LabelCreator("Diagonisis 3").getLabel());

        diagonisisInput3 = new TextField("", "Diagonisis", 34, TextField.ANY);

        diagonisisInput3.getAllStyles().setFont(font);

        diagonisisContainer3.add(diagonisisInput3);

        diagonisisInput3.setEditable(false);

        form.add(diagonisisContainer3);

        diagonisisSpinner3 = new ComboBox<>();

        diagonisisSpinner3.addItem(map.get("diagonisis2").toString());
        diagonisisSpinner3.addItem("Diagonisis");
        diagonisisSpinner3.addItem("Diagonisis 1");
        diagonisisSpinner3.addItem("Diagonisis 2");
        diagonisisSpinner3.addItem("Diagonisis 3");
        diagonisisSpinner3.addItem("Diagonisis 4");
        diagonisisSpinner3.addItem("Diagonisis 5");
        diagonisisSpinner3.addItem("Diagonisis 6");
        diagonisisSpinner3.addItem("Diagonisis 7");
        diagonisisSpinner3.addItem("Diagonisis 8");
        diagonisisSpinner3.addItem("Diagonisis 9");
        diagonisisSpinner3.addItem("Diagonisis 10");

        form.add(diagonisisSpinner3);

        Container ageContainer = new Container(new FlowLayout());

        ageContainer.add(new LabelCreator("Age").getLabel());

        ageInput = new TextField("", "Age", 34, TextField.DECIMAL);

        ageInput.getAllStyles().setFont(font);

        ageContainer.add(ageInput);

        ageInput.setText(map.get("age").toString());

        form.add(ageContainer);

        Container sexContainer = new Container(new FlowLayout());

        male = new RadioButton("Male");
        female = new RadioButton("Female");
        other = new RadioButton("Other");

        if (map.get("sex").toString().equals("Male")) {

            male.setSelected(true);

        } else if (map.get("sex").toString().equals("Female")) {

            female.setSelected(true);

        } else {

            other.setSelected(true);

        }

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

        addPatientButton = new UpdateOpdButton();

        addPatientButton.opdId = opdId;
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
