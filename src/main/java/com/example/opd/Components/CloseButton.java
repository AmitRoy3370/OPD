package com.example.opd.Components;

import com.example.opd.Components.CustomFont;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.Form;

public class CloseButton implements CustomFont, Colors {

    Button closeButton = new Button("Close");

    public Form form;

    public void setCloseButton() {

        closeButton.addActionListener(e -> {

            if (form != null) {

                form.removeAll(); // Clear the form

                form.showBack();

            } else {

                Display.getInstance().exitApplication();

            }

        });

        closeButton.getAllStyles().setBgColor(glassColor);
        closeButton.getAllStyles().setBgTransparency(255);
        closeButton.getAllStyles().setFont(font);

    }

    public Button getCloseButton() {
        return closeButton;
    }

}
