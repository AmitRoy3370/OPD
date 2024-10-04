package com.example.opd.Components;

import com.example.opd.Components.CustomFont;
import com.codename1.ui.Label;

public class LabelCreator  implements CustomFont, Colors {

    Label label;

    public LabelCreator(String title) {

        label = new Label(title);

        label.getAllStyles().setFont(font);
        label.getAllStyles().setFgColor(glassBlack);

    }

    public Label getLabel() {
        return label;
    }

}
