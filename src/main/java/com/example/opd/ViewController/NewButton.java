package com.example.opd.ViewController;

import com.example.opd.View.AddPatient;
import com.example.opd.Components.Colors;
import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;
import com.codename1.ui.Form;

public class NewButton implements CustomFont, Colors {

    Button newButton = new Button("New");

    public Form form;

    public void createButton() {

        newButton.addActionListener(e -> {

            AddPatient addPatient = new AddPatient();

            addPatient.previousForm = form;

            addPatient.start();

        });

        newButton.getAllStyles().setBgColor(glassColor);
        newButton.getAllStyles().setBgTransparency(255);
        newButton.getAllStyles().setFont(font);

    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Button getNewButton() {
        return newButton;
    }

}
