package com.example.opd.View;

import com.example.opd.Components.OPDListCollector;
import com.example.opd.Components.Colors;
import com.example.opd.Reposatory.DatabaseHelper;
import com.example.opd.Components.CustomFont;
import com.example.opd.Model.OPDWithPatientData;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.codename1.util.Callback;
import com.example.opd.Components.SearchIcon;
import static com.example.opd.Components.CustomFont.font;
import java.util.Date;
import java.util.Map;

public class PatientOPDAdmit extends Lifecycle implements CustomFont, SearchIcon, Colors {

    Map<String, Object> map;

    DatabaseHelper database = new DatabaseHelper();

    OPDListCollector list = new OPDListCollector();

    public Form previousForm;

    @Override
    public void runApp() {

        Form form = new Form("OPD Admission", new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar toolbar = new Toolbar();

        form.setToolbar(toolbar);

        toolbar.addCommandToLeftBar("Back", null, e -> {

            if (previousForm != null) {

                previousForm.show();

            }

        });

        TextField diseasesInput = new TextField("", "Diseases", 34, TextField.ANY);

        diseasesInput.getAllStyles().setMargin(Component.TOP, 5);
        diseasesInput.getAllStyles().setMargin(Component.BOTTOM, 5);
        diseasesInput.getAllStyles().setFont(font);

        ComboBox<String> opdPicker = new ComboBox<>();

        opdPicker.getAllStyles().setMargin(Component.BOTTOM, 5);

        database.readAllOPD(list);

        for (int i = 0; i < list.size(); ++i) {

            opdPicker.addItem("OPD-" + (i + 1));

        }

        TextField statusInput = new TextField("", "Status", 34, TextField.ANY);

        statusInput.getAllStyles().setMargin(Component.BOTTOM, 5);
        statusInput.getAllStyles().setFont(font);

        Button admit = new Button("Admit");

        admit.getAllStyles().setMargin(Component.BOTTOM, 5);

        statusInput.setEditable(false);

        admit.addActionListener(e -> {

            String patientId = map.get("_id").toString();
            String diseases = diseasesInput.getText().trim();

            statusInput.setText(list.get(opdPicker.getSelectedIndex()).get("patientType").toString());
            statusInput.setEditable(false);

            String status = statusInput.getText().trim();
            String opdId = list.get(opdPicker.getSelectedIndex()).get("_id").toString();
            Date date = new Date();
            String diseasesType = list.get(opdPicker.getSelectedIndex()).get("diseasesType").toString();

            OPDWithPatientData opd = new OPDWithPatientData(patientId, opdId, diseases, diseasesType, date.toString(), status);

            Callback callback = new Callback() {
                @Override
                public void onSucess(Object t) {
                    Dialog.show("Database Info", t.toString(), "OK", "Cancel");
                }

                @Override
                public void onError(Object o, Throwable thrwbl, int i, String string) {
                    Dialog.show("Error", string, "OK", "Cancel");
                }
            };

            database.admitPatient(opd, callback);

        });

        admit.getAllStyles().setBgColor(glassBlue);
        admit.getAllStyles().setFgColor(glassBlack);
        admit.getAllStyles().setBgTransparency(255);
        admit.getAllStyles().setFont(font);

        form.addAll(diseasesInput, opdPicker, statusInput, admit);

        form.setScrollable(true);

        form.show();

    }

}
