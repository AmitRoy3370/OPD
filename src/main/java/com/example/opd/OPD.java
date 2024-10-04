package com.example.opd;

import com.example.opd.Components.SearchIcon;
import com.example.opd.ViewController.SearchButton;
import com.example.opd.ViewController.NewButton;
import com.example.opd.ViewController.ResetButton;
import com.example.opd.Components.OPDListCollector;
import com.example.opd.ViewController.DeleteButton;
import com.example.opd.Components.Colors;
import com.example.opd.Components.ListCollector;
import com.example.opd.Components.Edit;
import com.example.opd.Components.CloseButton;
import com.example.opd.View.AddPatientPage;
import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Components.CustomFont;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.util.Callback;
import java.util.Map;

public class OPD extends Lifecycle implements CustomFont, SearchIcon, Colors {

    NewButton newButton;
    Edit editButton;
    DeleteButton deleteButton;
    CloseButton closeButton;
    SearchButton searchButton;
    ResetButton resetButton;

    Label code, opdNo, patId, searchByCode, otherFilters, searchDiseases, dateFrom, dateTo, ageFrom, ageTo, patientType, count;

    public Form form;

    boolean expanded = false;

    Container row, rowsContainer;

    TextField codeNo, opdNum, patIdNo, searchDiseasesInput, ageFromInput, ageToInput;

    ComboBox<String> diseaseTypeSpinner, wardSpinner, allDiseaseSpinner;

    RadioButton male, female, other, all, newPatient, reAttendence, myPatients;

    ButtonGroup sexGroup, statusGroup;

    Container rightLayout;

    Container updateDeleteContainer;

    Button addPatientButton, visibleNewButton, visibleEditButton, visibleCloseButton, visibleDeleteButton;

    Picker entryDate, leaveDate;

    Label patientCount;

    ListCollector list = new ListCollector();

    ListCollector list1 = new ListCollector();

    DatabaseHelper database = new DatabaseHelper();

    @Override
    public void runApp() {

        form = new Form("OPD out patient department", new BorderLayout());

// Container for the top 90% height
        Container topContainer = new Container(new BorderLayout());

// Red Layout (20% Width, 90% Height)
        Container leftLayout = new Container();
        leftLayout.getAllStyles().setBgColor(glassWhite); // Red color
        leftLayout.getAllStyles().setBgTransparency(255);
        leftLayout.setPreferredW(60 * Display.getInstance().getDisplayWidth() / 100);

        leftLayout.setScrollableX(true);
        leftLayout.setScrollableY(true);

        leftLayerSetUp(leftLayout);

// Blue Layout (80% Width, 90% Height)
        rightLayout = new Container(new BoxLayout(BoxLayout.X_AXIS));
        rightLayout.getAllStyles().setBgColor(glassWhite); // Blue color
        rightLayout.getAllStyles().setBgTransparency(255);

        setRightLayer(rightLayout);

        rightLayout.setScrollableX(true);
        rightLayout.setScrollableY(true);

// Add red and blue containers to the top container
        topContainer.add(BorderLayout.WEST, leftLayout);  // Red takes 20% width on the left
        topContainer.add(BorderLayout.CENTER, rightLayout); // Blue takes the remaining 80% width

// Add the top container to the form (takes 90% height)
        topContainer.setPreferredH(95 * Display.getInstance().getDisplayHeight() / 100);
        form.add(BorderLayout.CENTER, topContainer);

// Pink Layout (10% Height, Full Width)
        Container bottomLlayout = new Container(new FlowLayout());
        bottomLlayout.getAllStyles().setBgColor(glassWhite); // Pink color
        bottomLlayout.getAllStyles().setBgTransparency(255);
        bottomLlayout.setPreferredH(5 * Display.getInstance().getDisplayHeight() / 100); // 10% of the height

        lowLayerSet(bottomLlayout);

        form.add(BorderLayout.SOUTH, bottomLlayout);

        form.setScrollableX(true);
        form.setScrollableY(true);

        form.show();

    }

    public void lowLayerSet(Container buttonsLayer) {

        newButton = new NewButton();

        newButton.setForm(form);

        editButton = new Edit();
        closeButton = new CloseButton();
        deleteButton = new DeleteButton();
        addPatientButton = new Button("Add Patient");

        addPatientButton.getAllStyles().setBgColor(glassColor);
        addPatientButton.getAllStyles().setBgTransparency(255);
        addPatientButton.getAllStyles().setFont(font);

        addPatientButton.addActionListener(e -> {

            AddPatientPage addPatient = new AddPatientPage();

            addPatient.previousForm = form;

            addPatient.start();

            this.destroy();

        });

        newButton.createButton();
        editButton.setEditButton();
        closeButton.getCloseButton();
        deleteButton.setDeleteButton();

        closeButton.form = form;
        buttonsLayer.setLayout(new FlowLayout());

        visibleNewButton = newButton.getNewButton();
        visibleEditButton = editButton.getEditButton();
        visibleCloseButton = closeButton.getCloseButton();
        visibleDeleteButton = deleteButton.getDeleteButton();

        visibleCloseButton.addActionListener(e -> {

            Display.getInstance().exitApplication();

        });

        buttonsLayer.add(visibleNewButton)
                .add(visibleEditButton)
                .add(visibleDeleteButton)
                .add(visibleCloseButton)
                .add(addPatientButton);

        buttonsLayer.setScrollableX(true);
        buttonsLayer.setScrollableY(true);

    }

    OPDListCollector opdList = new OPDListCollector();
    //PatientDatabaseHelper patientDatabase = new PatientDatabaseHelper();

    boolean fullTable = false;

    //ListCollector patientList = new ListCollector();
    public void leftLayerSetUp(Container container) {

        container.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        LabelCreator searchBy = new LabelCreator("Search by code");

        searchByCode = searchBy.getLabel();

        container.add(searchByCode);

        Container inputText = new Container(new BoxLayout(BoxLayout.X_AXIS));

        Container codeInput = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        LabelCreator codeLabel = new LabelCreator("Code");

        code = codeLabel.getLabel();

        codeNo = new TextField("", "Code", 34, TextField.NUMERIC);

        codeNo.getAllStyles().setFont(font);

        codeNo.setPreferredW(20 * Display.getInstance().getDisplayWidth() / 100);

        codeInput.add(code).add(codeNo);

        inputText.add(codeInput);

        Container opdContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        LabelCreator opdLabel = new LabelCreator("OPD");

        opdNo = opdLabel.getLabel();

        opdNum = new TextField("", "Opd", 34, TextField.NUMERIC);

        opdNum.getAllStyles().setFont(font);

        opdContainer.setPreferredW(20 * Display.getInstance().getDisplayWidth() / 100);

        opdContainer.add(opdNo);
        opdContainer.add(opdNum);

        inputText.add(opdContainer);

        Container patContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        LabelCreator patLabel = new LabelCreator("Pat");

        patId = patLabel.getLabel();

        patIdNo = new TextField("", "Pat", 34, TextField.NUMERIC);

        patIdNo.getAllStyles().setFont(font);

        patContainer.setPreferredW(25 * Display.getInstance().getDisplayWidth() / 100);

        patContainer.add(patId).add(patIdNo);

        inputText.add(patContainer);

        container.add(inputText);

        LabelCreator otherFilterLabel = new LabelCreator("Other Fileters");

        container.add(otherFilterLabel.getLabel());

        wardSpinner = new ComboBox<>("OPD");

        wardSpinner.getAllStyles().setMarginBottom(20);

        container.add(wardSpinner);

        database.readAllOPD(opdList);

        container.add(new LabelCreator("Diseases Type").getLabel());

        diseaseTypeSpinner = new ComboBox<>();

        for (int i = 0; i < opdList.size(); ++i) {

            diseaseTypeSpinner.addItem(opdList.get(i).get("diseasesType").toString());

        }

        container.add(diseaseTypeSpinner);

        Container searchDiseasesContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        LabelCreator searchDiseasesLabel = new LabelCreator("ills");

        Label searchLabel = searchDiseasesLabel.getLabel();

        searchLabel.setPreferredW(20 * Display.getInstance().getDisplayWidth() / 100);

        searchDiseasesInput = new TextField("", "Search", 34, TextField.ANY);

        searchDiseasesInput.setPreferredW(50 * Display.getInstance().getDisplayWidth() / 100);

        searchDiseasesInput.getAllStyles().setFont(font);

        searchDiseasesInput.setEditable(false);

        //searchIconLabel.setPreferredW(20 * Display.getInstance().getDisplayWidth() / 100);
        searchDiseasesContainer.add(searchLabel)
                .add(searchDiseasesInput)
                .add(searchIconLabel);

        container.add(searchDiseasesContainer);

        allDiseaseSpinner = new ComboBox<>();

        list1.clear();

        database.readAllOPDPatient(list1);

        //patientDatabase.readAllPatient(patientList);
        for (int i = 0; i < list1.size(); ++i) {

            try {

                allDiseaseSpinner.addItem(list1.get(i).get("diseases").toString());

            } catch (Exception e) {

                System.out.println(list1.size());

            }
        }

        container.add(allDiseaseSpinner);

        male = new RadioButton("Male");
        female = new RadioButton("Female");
        other = new RadioButton("Other");
        sexGroup = new ButtonGroup(male, female, other);
        Container sexContainer = new Container(new FlowLayout());
        sexContainer.addAll(male, female, other);

        container.add(sexContainer);

        LabelCreator dateFromLabel = new LabelCreator("Date From");

        Label entryLabel = dateFromLabel.getLabel();

        Container entryContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        entryDate = new Picker();
        entryDate.setType(Display.PICKER_TYPE_DATE);
        entryDate.setEnabled(true);

        entryContainer.add(entryLabel).add(entryDate);

        container.add(entryContainer);

        LabelCreator dateToLabel = new LabelCreator("Date To");

        Label leaveLabel = dateToLabel.getLabel();

        Container leaveContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

// Seventh Line: Leave Date Picker
        leaveDate = new Picker();
        leaveDate.setType(Display.PICKER_TYPE_DATE);
        leaveDate.setEnabled(true);

        leaveContainer.add(leaveLabel).add(leaveDate);

        container.add(leaveContainer);

        Container ageFromContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        LabelCreator ageFromLabel = new LabelCreator("Age From");

        Label ageFromLabel1 = ageFromLabel.getLabel();

        ageFromInput = new TextField("", "From", 34, TextField.DECIMAL);

        ageFromContainer.add(ageFromLabel1);
        ageFromContainer.add(ageFromInput);

        container.add(ageFromContainer);

        Container ageToContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));

        LabelCreator ageToLabel = new LabelCreator("Age To");

        Label ageToLabel1 = ageToLabel.getLabel();

        ageToInput = new TextField("", "To", 34, TextField.DECIMAL);

        ageToContainer.add(ageToLabel1);
        ageToContainer.add(ageToInput);

        container.add(ageToContainer);

        all = new RadioButton("All");
        newPatient = new RadioButton("New Attendence");
        reAttendence = new RadioButton("Refferal");
        myPatients = new RadioButton("RefferedTo");
        statusGroup = new ButtonGroup(newPatient, reAttendence, myPatients);
        Container statusContainer = new Container(new FlowLayout());
        statusContainer.addAll(newPatient, reAttendence, myPatients);

        container.add(statusContainer);

        Container searchResetContainer = new Container(new FlowLayout());

        searchButton = new SearchButton();
        resetButton = new ResetButton();

        if (!fullTable) {

            rowsContainer = new Container(new TableLayout(1, 6));

            rowsContainer.add(new LabelCreator("Code").getLabel());
            rowsContainer.add(new LabelCreator("OPD No").getLabel());
            rowsContainer.add(new LabelCreator("Ward").getLabel());
            rowsContainer.add(new LabelCreator("Date").getLabel());
            rowsContainer.add(new LabelCreator("Pat No").getLabel());
            rowsContainer.add(new LabelCreator("Full Name").getLabel());

        } else {

            rowsContainer = new Container(new TableLayout(1, 12));

            rowsContainer.add(new LabelCreator("Code").getLabel());
            rowsContainer.add(new LabelCreator("OPD No").getLabel());
            rowsContainer.add(new LabelCreator("Ward").getLabel());
            rowsContainer.add(new LabelCreator("Date").getLabel());
            rowsContainer.add(new LabelCreator("Pat No").getLabel());
            rowsContainer.add(new LabelCreator("Full Name").getLabel());
            rowsContainer.add(new LabelCreator("Code").getLabel());
            rowsContainer.add(new LabelCreator("OPD No").getLabel());
            rowsContainer.add(new LabelCreator("Ward").getLabel());
            rowsContainer.add(new LabelCreator("Date").getLabel());
            rowsContainer.add(new LabelCreator("Pat No").getLabel());
            rowsContainer.add(new LabelCreator("Full Name").getLabel());
            rowsContainer.add(new LabelCreator("Sex").getLabel());
            rowsContainer.add(new LabelCreator("Age").getLabel());
            rowsContainer.add(new LabelCreator("Diseases").getLabel());
            rowsContainer.add(new LabelCreator("Diseases Type").getLabel());
            rowsContainer.add(new LabelCreator("Status").getLabel());
            rowsContainer.add(new LabelCreator("User").getLabel());

        }

        try {

            updateDeleteContainer.removeAll();

        } catch (Exception ex) {

            updateDeleteContainer = new Container(new TableLayout(1, 2));

        }

        updateDeleteContainer.setLayout(new TableLayout(1, 2));

        Button updateButton = new Button("Update");
        Button opdDeleteButton = new Button("Delete");

        updateButton.setVisible(false);
        opdDeleteButton.setVisible(false);

        updateDeleteContainer.add(updateButton)
                .add(opdDeleteButton)
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""));

        resetButton.setFulllTable(fullTable);
        resetButton.setList(list);
        resetButton.setRowsContainer(rowsContainer);
        resetButton.setOtherContainer(updateDeleteContainer);
        resetButton.setForm(form);
        resetButton.setCallback(new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database Update", i + "" + string, "OK", "Close");

            }
        });

        resetButton.setResetButton();

        searchButton.setSearchButton();

        searchButton.getSearchButton().addActionListener(e -> {

            //rowsContainer.clearClientProperties();
            if (!fullTable) {

                //rowsContainer = new Container(new TableLayout(1, 6));
                rowsContainer.removeAll();

                rowsContainer.setLayout(new TableLayout(1, 6));

                // Initial row with first 6 columns
                row = createRow(new String[]{"Code", "OPD No", "Ward", "Date", "Pat No", "Full Name"});
                //rowsContainer.add(row);

                rowsContainer.add(new LabelCreator("Code").getLabel());
                rowsContainer.add(new LabelCreator("OPD No").getLabel());
                rowsContainer.add(new LabelCreator("Ward").getLabel());
                rowsContainer.add(new LabelCreator("Date").getLabel());
                rowsContainer.add(new LabelCreator("Pat No").getLabel());
                rowsContainer.add(new LabelCreator("Full Name").getLabel());

                rowsContainer.revalidate();

            } else {

                rowsContainer.removeAll();

                rowsContainer.setLayout(new TableLayout(1, 12));

                rowsContainer.add(new LabelCreator("Code").getLabel());
                rowsContainer.add(new LabelCreator("OPD No").getLabel());
                rowsContainer.add(new LabelCreator("Ward").getLabel());
                rowsContainer.add(new LabelCreator("Date").getLabel());
                rowsContainer.add(new LabelCreator("Pat No").getLabel());
                rowsContainer.add(new LabelCreator("Full Name").getLabel());
                rowsContainer.add(new LabelCreator("Sex").getLabel());
                rowsContainer.add(new LabelCreator("Age").getLabel());
                rowsContainer.add(new LabelCreator("Diseases").getLabel());
                rowsContainer.add(new LabelCreator("Diseases Type").getLabel());
                rowsContainer.add(new LabelCreator("Status").getLabel());
                rowsContainer.add(new LabelCreator("User").getLabel());

                rowsContainer.revalidate();

            }

            list.clear();

            System.out.println("date From :- " + entryDate.getDate() + "");
            System.out.println("date To :- " + leaveDate.getDate() + "");

            updateDeleteContainer.removeAll();

            updateDeleteContainer.setLayout(new TableLayout(1, 2));

            Button updateButton1 = new Button("Update");
            Button opdDeleteButton1 = new Button("Delete");

            updateButton1.setVisible(false);
            opdDeleteButton1.setVisible(false);

            updateDeleteContainer.add(updateButton1)
                    .add(opdDeleteButton1)
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""));

            database.searchOPDPatient(rowsContainer,
                    new Callback() {
                @Override
                public void onSucess(Object t) {

                    Dialog.show("Database Update", t.toString(), "OK", "Cancel");

                }

                @Override
                public void onError(Object o, Throwable thrwbl, int i, String string) {

                    Dialog.show("Database Update", i + "" + string, "OK", "Close");

                }
            },
                    fullTable,
                    list,
                    codeNo.getText().trim(),
                    opdNum.getText().trim(),
                    patIdNo.getText().trim(),
                    diseaseTypeSpinner.getSelectedItem(),
                    allDiseaseSpinner.getSelectedItem(),
                    male.isSelected() ? "Male" : female.isSelected() ? "Female" : other.isSelected() ? "Other" : null,
                    entryDate.getDate() != null ? entryDate.getDate() + "" : null,
                    leaveDate.getDate() != null ? leaveDate.getDate() + "" : null,
                    ageFromInput != null ? ageFromInput.getText().trim() : null,
                    ageToInput != null ? ageToInput.getText().trim() : null,
                    reAttendence.isSelected() ? "refferal" : newPatient.isSelected() ? "newAttendece" : myPatients.isSelected() ? "refferedTo" : null,
                    updateDeleteContainer,
                    form);

            rowsContainer.revalidate();
            updateDeleteContainer.revalidate();

        });

        resetButton.getResetButton().addActionListener(e -> {

            updateDeleteContainer.removeAll();

            updateDeleteContainer.setLayout(new TableLayout(1, 2));

            Button updateButton1 = new Button("Update");
            Button opdDeleteButton1 = new Button("Delete");

            updateButton1.setVisible(false);
            opdDeleteButton1.setVisible(false);

            updateDeleteContainer.add(updateButton1)
                    .add(opdDeleteButton1)
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""))
                    .add(new Label(""));

            fullTable = false;

            rowsContainer.removeAll();

            rowsContainer.setLayout(new TableLayout(1, 6));

            if (!fullTable) {

                //rowsContainer = new Container(new TableLayout(1, 6));
                rowsContainer.removeAll();

                rowsContainer.setLayout(new TableLayout(1, 6));

                // Initial row with first 6 columns
                row = createRow(new String[]{"Code", "OPD No", "Ward", "Date", "Pat No", "Full Name"});
                //rowsContainer.add(row);

                rowsContainer.add(new LabelCreator("Code").getLabel());
                rowsContainer.add(new LabelCreator("OPD No").getLabel());
                rowsContainer.add(new LabelCreator("Ward").getLabel());
                rowsContainer.add(new LabelCreator("Date").getLabel());
                rowsContainer.add(new LabelCreator("Pat No").getLabel());
                rowsContainer.add(new LabelCreator("Full Name").getLabel());

                rowsContainer.revalidate();

            } else {

                rowsContainer.removeAll();

                rowsContainer.setLayout(new TableLayout(1, 12));

                rowsContainer.add(new LabelCreator("Code").getLabel());
                rowsContainer.add(new LabelCreator("OPD No").getLabel());
                rowsContainer.add(new LabelCreator("Ward").getLabel());
                rowsContainer.add(new LabelCreator("Date").getLabel());
                rowsContainer.add(new LabelCreator("Pat No").getLabel());
                rowsContainer.add(new LabelCreator("Full Name").getLabel());
                rowsContainer.add(new LabelCreator("Sex").getLabel());
                rowsContainer.add(new LabelCreator("Age").getLabel());
                rowsContainer.add(new LabelCreator("Diseases").getLabel());
                rowsContainer.add(new LabelCreator("Diseases Type").getLabel());
                rowsContainer.add(new LabelCreator("Status").getLabel());
                rowsContainer.add(new LabelCreator("User").getLabel());

                rowsContainer.revalidate();

            }

            list.clear();
            database.readAllOPDPatient(rowsContainer,
                    new Callback() {
                @Override
                public void onSucess(Object t) {

                    Dialog.show("Database Update", t.toString(), "OK", "Cancel");

                }

                @Override
                public void onError(Object o, Throwable thrwbl, int i, String string) {

                    Dialog.show("Database Update", i + "" + string, "OK", "Close");

                }
            },
                    fullTable, list, updateDeleteContainer, form);

        });

        searchResetContainer.addAll(searchButton.getsearchButton(), resetButton.getResetButton());

        container.add(searchResetContainer);

        patientCount = new LabelCreator("Count").getLabel();

        container.add(patientCount);

    }

    public void setRightLayer(Container container) {

        rowsContainer = new Container(new TableLayout(1, 6));

        // Initial row with first 6 columns
        row = createRow(new String[]{"Code", "OPD No", "Ward", "Date", "Pat No", "Full Name"});
        //rowsContainer.add(row);

        rowsContainer.add(new LabelCreator("Code").getLabel());
        rowsContainer.add(new LabelCreator("OPD No").getLabel());
        rowsContainer.add(new LabelCreator("Ward").getLabel());
        rowsContainer.add(new LabelCreator("Date").getLabel());
        rowsContainer.add(new LabelCreator("Pat No").getLabel());
        rowsContainer.add(new LabelCreator("Full Name").getLabel());

        // Add the rows container to the form
        //rowsContainer.add(rowsContainer);
        container.add(rowsContainer);
        // "+" sign button to expand/collapse columns
        Button expandButton = new Button("+");

        expandButton.getAllStyles().setFgColor(glassBlack);
        expandButton.getAllStyles().setBgColor(glassBlue);
        expandButton.getAllStyles().setFont(font);

        expandButton.addActionListener(e -> toggleColumns(expandButton));

        // "Add Row" button to add a completely new row
        Button addRowButton = new Button("Add New Row");
        addRowButton.addActionListener(e -> {
            // Add a new row with custom data
            rowsContainer.add(createRow(new String[]{"003", "67890", "General", "2023-08-29", "12345", "John Smith"}));
            rowsContainer.revalidate(); // Refresh the UI to show the new row
        });

        updateDeleteContainer = new Container(new TableLayout(1, 2));

        Button updateButton = new Button("Update");
        Button opdDeleteButton = new Button("Delete");

        updateButton.setVisible(false);
        opdDeleteButton.setVisible(false);

        updateDeleteContainer.add(updateButton)
                .add(opdDeleteButton)
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""));

        container.add(updateDeleteContainer);

        container.add(expandButton);

        Callback callback = new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database Update", i + " :- " + string, "OK", "Cancel");

            }
        };

        list.clear();

        updateDeleteContainer.removeAll();

        updateButton = new Button("Update");
        opdDeleteButton = new Button("Delete");

        updateButton.setVisible(false);
        opdDeleteButton.setVisible(false);

        updateDeleteContainer.add(updateButton)
                .add(opdDeleteButton)
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""));

        database.readAllOPDPatient(rowsContainer, callback, false, list, updateDeleteContainer, form);

        patientCount.setText(list.size() + "");

    }
    // Method to create a row

    private Container createRow(String[] columnValues) {
        Container newRow = new Container(new BoxLayout(BoxLayout.X_AXIS));

        for (String value : columnValues) {
            newRow.add(new Label(value));
        }

        return newRow;
    }

    // Method to toggle columns
    private void toggleColumns(Button expandButton) {
        if (expanded) {
            // If already expanded, remove the extra columns
            removeExtraColumns();
            expandButton.setText("+");
        } else {
            // If not expanded, add the extra columns
            addExtraColumns();
            expandButton.setText("-");
        }
        expanded = !expanded;

        rightLayout.revalidate();

    }

    // Method to add extra columns
    private void addExtraColumns() {

        fullTable = true;

        rowsContainer.removeAll();

        rowsContainer.setLayout(new TableLayout(1, 12));

        rowsContainer.add(new LabelCreator("Code").getLabel());
        rowsContainer.add(new LabelCreator("OPD No").getLabel());
        rowsContainer.add(new LabelCreator("Ward").getLabel());
        rowsContainer.add(new LabelCreator("Date").getLabel());
        rowsContainer.add(new LabelCreator("Pat No").getLabel());
        rowsContainer.add(new LabelCreator("Full Name").getLabel());
        rowsContainer.add(new LabelCreator("Sex").getLabel());
        rowsContainer.add(new LabelCreator("Age").getLabel());
        rowsContainer.add(new LabelCreator("Diseases").getLabel());
        rowsContainer.add(new LabelCreator("Diseases Type").getLabel());
        rowsContainer.add(new LabelCreator("Status").getLabel());
        rowsContainer.add(new LabelCreator("User").getLabel());

        rowsContainer.revalidate();

        Callback callback = new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database Update", i + " :- " + string, "OK", "Cancel");

            }
        };

        //list.clear();
        //database.readAllOPDPatient(rowsContainer, callback, true, list);
        for (Map<String, Object> map : list.list) {

            rowsContainer.add(new LabelCreator(map.get("index").toString()).getLabel())
                    .add(new LabelCreator(map.get("opdNo") + "").getLabel())
                    .add(new LabelCreator("OPD").getLabel())
                    .add(new LabelCreator(map.get("admissionDate") + "").getLabel())
                    .add(new LabelCreator(map.get("patNo") + "").getLabel())
                    .add(new LabelCreator(map.get("fullName") + "").getLabel());

            if (fullTable) {

                rowsContainer.add(new LabelCreator(map.get("sex") + "").getLabel())
                        .add(new LabelCreator(map.get("age") + "").getLabel())
                        .add(new LabelCreator(map.get("diseases") + "").getLabel())
                        .add(new LabelCreator(map.get("diseasesType") + "").getLabel())
                        .add(new LabelCreator(map.get("status") + "").getLabel())
                        .add(new LabelCreator("Admin").getLabel());

            }

            //updateDeleteContainer = new Container(new TableLayout(1, 2));
            /*Button updateButton = new Button("Update");
            Button opdDeleteButton = new Button("Delete");

            updateButton.getAllStyles().setBgColor(glassBlue);
            opdDeleteButton.getAllStyles().setFgColor(glassBlack);

            updateButton.addActionListener(e -> {

                UpdateOpdDataPage updatePage = new UpdateOpdDataPage();

                updatePage.opdId = map.get("_id").toString();

                updatePage.start();

            });

            opdDeleteButton.addActionListener(e -> {

                String selectedId = map.get("_id").toString();

                database.deleteOPD(selectedId, new Callback() {
                    @Override
                    public void onSucess(Object t) {

                        Dialog.show("Database Update", "Data is deleted successfully", "OK", "Cancel");

                    }

                    @Override
                    public void onError(Object o, Throwable thrwbl, int i, String string) {

                        Dialog.show("Database Update", i + " " + string, "OK", "Cancel");

                    }
                });

            });

            //updateButton.setVisible(false);
            //opdDeleteButton.setVisible(false);
            updateDeleteContainer.add(updateButton)
                    .add(opdDeleteButton);*/
        }

        patientCount.setText(list.size() + "");

        searchButton.setRowsContainer(rowsContainer);

        //searchButton.setCodeNo(codeNo.getText().trim());
        searchButton.setCodeNoInput(codeNo);

    }

    // Method to remove extra columns
    private void removeExtraColumns() {
        // Remove the last 6 components (assuming they are the extra columns)

        fullTable = false;

        rowsContainer.removeAll();

        rowsContainer.setLayout(new TableLayout(1, 6));

        rowsContainer.add(new LabelCreator("Code").getLabel());
        rowsContainer.add(new LabelCreator("OPD No").getLabel());
        rowsContainer.add(new LabelCreator("Ward").getLabel());
        rowsContainer.add(new LabelCreator("Date").getLabel());
        rowsContainer.add(new LabelCreator("Pat No").getLabel());
        rowsContainer.add(new LabelCreator("Full Name").getLabel());

        rowsContainer.revalidate();

        Callback callback = new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database Update", i + " :- " + string, "OK", "Cancel");

            }
        };

        //list.clear();
        //database.readAllOPDPatient(rowsContainer, callback, false, list);
        for (Map<String, Object> map : list.list) {

            rowsContainer.add(new LabelCreator(map.get("index").toString()).getLabel())
                    .add(new LabelCreator(map.get("opdNo") + "").getLabel())
                    .add(new LabelCreator("OPD").getLabel())
                    .add(new LabelCreator(map.get("admissionDate") + "").getLabel())
                    .add(new LabelCreator(map.get("patNo") + "").getLabel())
                    .add(new LabelCreator(map.get("fullName") + "").getLabel());

            if (fullTable) {

                rowsContainer.add(new LabelCreator(map.get("sex") + "").getLabel())
                        .add(new LabelCreator(map.get("age") + "").getLabel())
                        .add(new LabelCreator(map.get("diseases") + "").getLabel())
                        .add(new LabelCreator(map.get("diseasesType") + "").getLabel())
                        .add(new LabelCreator(map.get("status") + "").getLabel())
                        .add(new LabelCreator("Admin").getLabel());

            }

        }

        patientCount.setText(list.size() + "");

    }

}
