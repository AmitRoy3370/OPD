package com.example.opd.ViewController;

import com.example.opd.Components.Colors;
import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;

public class DeleteButton implements CustomFont, Colors {

    Button deleteButton = new Button("Delete");

    public void setDeleteButton() {

        deleteButton.addActionListener(e -> {

        });

        deleteButton.getAllStyles().setBgColor(glassColor);
        deleteButton.getAllStyles().setBgTransparency(255);
        deleteButton.getAllStyles().setFont(font);

    }

    public Button getDeleteButton() {
        return deleteButton;
    }

}
