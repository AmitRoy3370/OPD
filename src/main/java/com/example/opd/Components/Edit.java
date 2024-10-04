package com.example.opd.Components;

import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;

public class Edit  implements CustomFont, Colors {

    Button editButton = new Button("Edit");

    public void setEditButton() {

        editButton.addActionListener(e -> {

        });

        editButton.getAllStyles().setBgColor(glassColor);
        editButton.getAllStyles().setBgTransparency(255);
        editButton.getAllStyles().setFont(font);

    }

    public Button getEditButton() {
        return editButton;
    }

}
