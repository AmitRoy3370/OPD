package com.example.opd.ViewController;

import com.example.opd.Components.Colors;
import com.example.opd.Reposatory.PatientDatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.List;
import com.codename1.ui.TextField;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.util.Callback;

public class searchPatientButton implements CustomFont, Colors {

    Button search = new Button("Search");

    Form form;

    TextField searchPatient;

    List list;

    DefaultListModel listModel;

    PatientDatabaseHelper database = new PatientDatabaseHelper();

    public void createButton() {

        Callback callback = new Callback() {
            @Override
            public void onSucess(Object t) {

                Dialog.show("Database Update", t.toString(), "OK", "Cancel");

            }

            @Override
            public void onError(Object o, Throwable thrwbl, int i, String string) {

                Dialog.show("Database Update", string, "OK", "Cancel");

            }
        };

        search.addActionListener(e -> {

            listModel.getList().clear();

            if (searchPatient.getText().length() > 0) {

                database.findStudentByFirstName(searchPatient.getText().trim(), list, callback);

                database.findStudentByLastName(searchPatient.getText(), list, callback);

            } else {

                database.readAllPatient(list, callback);

            }

        });

        search.getAllStyles().setFont(font);
        search.getAllStyles().setBgColor(glassBlue);
        search.getAllStyles().setFgColor(glassBlack);
        search.getAllStyles().setBgTransparency(255);

    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Button getSearch() {
        return search;
    }

    public TextField getSearchPatient() {
        return searchPatient;
    }

    public void setSearchPatient(TextField searchPatient) {
        this.searchPatient = searchPatient;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

}
