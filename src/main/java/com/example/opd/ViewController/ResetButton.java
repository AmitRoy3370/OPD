package com.example.opd.ViewController;

import com.example.opd.Components.Colors;
import com.example.opd.Components.ListCollector;
import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.util.Callback;
import static com.example.opd.Components.CustomFont.font;

public class ResetButton implements CustomFont, Colors {

    Button resetButton = new Button("reset");

    ListCollector list;

    Callback callback;

    Container rowsContainer;

    DatabaseHelper database = new DatabaseHelper();

    boolean fulllTable;

    Container otherContainer;

    Form form;

    public void setResetButton() {

        resetButton.addActionListener(e -> {

            list.clear();

            database.readAllOPDPatient(rowsContainer, callback, fulllTable, list, otherContainer, form);

        });

        resetButton.getAllStyles().setBgColor(glassColor);
        resetButton.getAllStyles().setBgTransparency(255);
        resetButton.getAllStyles().setFont(font);

    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public Button getResetButton() {
        return resetButton;
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

    public Container getRowsContainer() {
        return rowsContainer;
    }

    public void setRowsContainer(Container rowsContainer) {
        this.rowsContainer = rowsContainer;
    }

    public boolean isFulllTable() {
        return fulllTable;
    }

    public void setFulllTable(boolean fulllTable) {
        this.fulllTable = fulllTable;
    }

    public Container getOtherContainer() {
        return otherContainer;
    }

    public void setOtherContainer(Container otherContainer) {
        this.otherContainer = otherContainer;
    }

}
