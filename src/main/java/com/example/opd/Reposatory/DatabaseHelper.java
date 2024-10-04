package com.example.opd.Reposatory;

import com.example.opd.Components.DatabaseInfo;
import com.example.opd.Components.LabelCreator;
import com.example.opd.Model.OPDWithPatientData;
import com.example.opd.Model.OPDData;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;

import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.List;
import com.codename1.util.Callback;
import com.example.opd.Components.Colors;
import com.example.opd.Components.ListCollector;
import com.example.opd.Components.OPDListCollector;
import com.example.opd.View.UpdateOpdDataPage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper implements DatabaseInfo, Colors {

    ConnectionRequest connection;

    public void addOPD(OPDData opd, Callback callback) {

        try {

            connection = new ConnectionRequest() {
                @Override
                protected void readResponse(InputStream input) throws IOException {

                    String response = Util.readToString(input);

                    System.out.println("response :- " + response);

                    if (callback != null) {

                        callback.onSucess(response);

                    }

                }

                @Override
                protected void handleErrorResponseCode(int code, String message) {

                    if (callback != null) {

                        callback.onSucess(code + " :- " + message);

                    }

                }

            };

            connection.setPost(true);
            connection.setUrl(API_URL + "/opd");
            connection.setContentType("application/json");

            String jsonInputString = "{"
                    + "\"patientType\": \"" + opd.getPatientType() + "\","
                    + "\"attendanceDate\": \"" + opd.getAttendenceDate() + "\","
                    + "\"diseasesType\": \"" + opd.getDiseasesType() + "\","
                    + "\"diagonisis\": \"" + opd.getDiagonisis() + "\","
                    + "\"diagonisis1\": \"" + opd.getDiagonisis1() + "\","
                    + "\"diagonisis2\": \"" + opd.getDiagonisis2() + "\","
                    + "\"age\": \"" + opd.getAge() + "\","
                    + "\"sex\": \"" + opd.getSex() + "\""
                    + "}";

            connection.setRequestBody(jsonInputString);

            NetworkManager.getInstance().addToQueue(connection);

        } catch (Exception ex) {
            //Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println(ex);

        }

    }

    public void updateOPD(String id, Map<String, Object> updatedFields, Callback callback) {

        String url = API_URL + "/opd/update";

        ConnectionRequest req = new ConnectionRequest() {
            @Override
            protected void readResponse(java.io.InputStream input) throws IOException {

                callback.onSucess("Successfully updated student");

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {
                System.out.println("Error code: " + code + ", message: " + message);

                callback.onError(null, new NullPointerException(), code, message);

            }
        };

        req.setPost(true);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("_id", id);
        requestBody.put("updatedFields", updatedFields);

        System.out.println("request body :- " + requestBody);

        req.setRequestBody(mapToJson(requestBody));

        req.setContentType("application/json");

        req.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");

            // Check the type of value and format it correctly
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else if (entry.getValue() instanceof Number || entry.getValue() instanceof Boolean) {
                json.append(entry.getValue());
            } else if (entry.getValue() instanceof List) {
                json.append(listToJson((List<?>) entry.getValue()));  // Convert List to JSON array
            } else if (entry.getValue() instanceof Map) {
                json.append(mapToJson((Map<String, Object>) entry.getValue()));    // Nested Map
            } else {
                json.append("\"").append(entry.getValue().toString()).append("\"");  // Fallback to string
            }

            json.append(",");
        }
        json.deleteCharAt(json.length() - 1); // Remove trailing comma
        json.append("}");
        return json.toString();
    }

    private String listToJson(List list) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Object item = list.getModel().getItemAt(i);

            if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof List) {
                json.append(listToJson((List) item));
            } else if (item instanceof Map) {
                json.append(mapToJson((Map) item));
            } else {
                json.append("\"").append(item.toString()).append("\"");
            }

            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public void readAllOPD(List list, Callback callback) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (Character.isDigit(currentChar)) {

                        valueBuilder.append(currentChar);
                        continue;

                    }

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("isAvaiable")) {

                                //System.out.println("I am isAvailable");
                                if (responseString.charAt(i + 1) == 't') {

                                    valueBuilder.append("Active");

                                } else {

                                    valueBuilder.append("close");

                                }

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                StringBuilder sb = new StringBuilder();

                                //System.out.println("getted title :- " + map.get("title") + " searching title :- " + title);
                                //if (map.get("title").toString().equalsIgnoreCase(title)) {
                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("title")).append(" ")
                                        .append(map.get("cost")).append(" ")
                                        .append(map.get("isAvaiable")).append(" ")
                                        .append(map.get("plateDimention"));

                                list.addItem(sb.toString().trim());

                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                if (list.size() > 0) {

                    if (callback != null) {

                        callback.onSucess("Can be able to find all the data of radiological test.");

                    }

                } else {

                    if (callback != null) {

                        callback.onSucess("Can not be able to find all the data of radiological test.");

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opd/all";

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void readAllOPD(List list, Callback callback, String _id) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (Character.isDigit(currentChar)) {

                        valueBuilder.append(currentChar);
                        continue;

                    }

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("isAvaiable")) {

                                //System.out.println("I am isAvailable");
                                if (responseString.charAt(i + 1) == 't') {

                                    valueBuilder.append("Active");

                                } else {

                                    valueBuilder.append("close");

                                }

                            } else if (keyBuilder.toString().equals("age")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                StringBuilder sb = new StringBuilder();

                                //System.out.println("getted title :- " + map.get("title") + " searching title :- " + title);
                                //if (map.get("title").toString().equalsIgnoreCase(title)) {
                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("title")).append(" ")
                                        .append(map.get("cost")).append(" ")
                                        .append(map.get("isAvaiable")).append(" ")
                                        .append(map.get("plateDimention"));

                                list.addItem(sb.toString().trim());

                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                if (list.size() > 0) {

                    if (callback != null) {

                        callback.onSucess("Can be able to find all the data of radiological test.");

                    }

                } else {

                    if (callback != null) {

                        callback.onSucess("Can not be able to find all the data of radiological test.");

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opd/search?_id=" + _id;

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void readAllOPD(OPDListCollector list, Callback callback, String _id) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("age")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                Map<String, Object> map_ = new HashMap<>();

                                System.out.println("parsing data...");

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                    System.out.println(j + " :- " + map.get(j));

                                }

                                list.add(map_);

                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                if (list.size() > 0) {

                    if (callback != null) {

                        callback.onSucess("Can be able to find all the data of radiological test.");

                    }

                } else {

                    if (callback != null) {

                        callback.onSucess("Can not be able to find all the data of radiological test.");

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opd/search?_id=" + _id;

        System.out.println("load url :- " + url);

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void readAllOPD(OPDListCollector opdList) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (Character.isDigit(currentChar)) {

                        valueBuilder.append(currentChar);
                        continue;

                    }

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("isAvaiable")) {

                                //System.out.println("I am isAvailable");
                                if (responseString.charAt(i + 1) == 't') {

                                    valueBuilder.append("Active");

                                } else {

                                    valueBuilder.append("close");

                                }

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                StringBuilder sb = new StringBuilder();

                                //System.out.println("getted title :- " + map.get("title") + " searching title :- " + title);
                                //if (map.get("title").toString().equalsIgnoreCase(title)) {
                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("title")).append(" ")
                                        .append(map.get("cost")).append(" ")
                                        .append(map.get("isAvaiable")).append(" ")
                                        .append(map.get("plateDimention"));

                                //list.addItem(sb.toString().trim());
                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                opdList.add(map_);

                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opd/all";

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void deleteOPD(String _id, Callback callback) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);
                System.out.println("Delete Response: " + responseString);

                if (responseString != null && !responseString.isEmpty()) {
                    callback.onSucess("Patient deleted successfully.");
                } else {
                    callback.onError(null, new NullPointerException(), 0, "Failed to delete patient.");
                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

                System.err.println("Error Deleting Patient: " + code + " Message: " + message);
                callback.onError(null, new NullPointerException(), code, message);

            }

        };

        String url = API_URL + "/opd/delete";

        //connection.setHttpMethod("DELETE");
        connection.setPost(true);
        connection.setUrl(url);
        connection.setContentType("application/json");

        System.out.println("all the data :- " + _id);

        String jsonInputString = "{"
                + "\"_id\": \"" + _id + "\""
                + "}";

        connection.setRequestBody(jsonInputString);

        NetworkManager.getInstance().addToQueue(connection);

    }

    public void admitPatient(OPDWithPatientData opd, Callback callback) {

        try {

            connection = new ConnectionRequest() {
                @Override
                protected void readResponse(InputStream input) throws IOException {

                    String response = Util.readToString(input);

                    System.out.println("response :- " + response);

                    if (callback != null) {

                        callback.onSucess(response);

                    }

                }

                @Override
                protected void handleErrorResponseCode(int code, String message) {

                    if (callback != null) {

                        callback.onError(null, new NullPointerException(), code, message);

                    }

                }

            };

            connection.setPost(true);
            connection.setUrl(API_URL + "/opdpatientdata");
            connection.setContentType("application/json");

            String jsonInputString = "{"
                    + "\"patientId\": \"" + opd.getPatientId() + "\","
                    + "\"opdId\": \"" + opd.getOpdId() + "\","
                    + "\"diseases\": \"" + opd.getDiseases() + "\","
                    + "\"diseasesType\": \"" + opd.getDiseasesType() + "\","
                    + "\"admissionDate\": \"" + opd.getAdmissionDate() + "\","
                    + "\"status\": \"" + opd.getStatus() + "\""
                    + "}";

            connection.setRequestBody(jsonInputString);

            NetworkManager.getInstance().addToQueue(connection);

        } catch (Exception ex) {
            //Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println(ex);

        }

    }

    public void readAllOPDPatient(Container rowsContainer, Callback callback, boolean fullTable, ListCollector list, Container otherContainer, Form form) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                int count = 1;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (Character.isDigit(currentChar)) {

                        valueBuilder.append(currentChar);
                        continue;

                    }

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("age")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("patNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("opdNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("index")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                StringBuilder sb = new StringBuilder();

                                //System.out.println("getted title :- " + map.get("title") + " searching title :- " + title);
                                //if (map.get("title").toString().equalsIgnoreCase(title)) {
                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                //sb.append(map.get("title")).append(" ")
                                //      .append(map.get("cost")).append(" ")
                                //    .append(map.get("isAvaiable")).append(" ")
                                //  .append(map.get("plateDimention"));
                                rowsContainer.add(new LabelCreator(count++ + "").getLabel())
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

                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                list.add(map_);

                                addButtons(otherContainer, map_, form);
                                //list.addItem(sb.toString().trim());
                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                if (rowsContainer != null) {

                    if (callback != null) {

                        callback.onSucess("Can be able to find all the data of radiological test.");

                    }

                } else {

                    if (callback != null) {

                        callback.onSucess("Can not be able to find all the data of radiological test.");

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opdpatientdata/all";

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    private void addButtons(Container rowsContainer, Map<String, Object> map, Form form) {

        Button update = new Button("Update");

        update.getAllStyles().setBgColor(glassBlue);
        update.getAllStyles().setFgColor(glassBlack);

        update.addActionListener(e -> {

            UpdateOpdDataPage updatePage = new UpdateOpdDataPage();

            updatePage.opdId = map.get("opdId").toString();
            updatePage.previousForm = form;

            updatePage.start();

        });

        Button delete = new Button("Delete");

        delete.getAllStyles().setBgColor(glassBlue);
        delete.getAllStyles().setFgColor(glassBlack);

        delete.addActionListener(e -> {

            String selectedId = map.get("opdId").toString();

            deleteOPD(selectedId, new Callback() {
                @Override
                public void onSucess(Object t) {

                    Dialog.show("Database Update", "Data is deleted successfully", "OK", "Cancel");

                    int componentCount = rowsContainer.getComponentCount();

                    int componentsToRemove = 7 * 2;
                    for (int i = 0; i < componentsToRemove && componentCount > 0; i++) {
                        rowsContainer.removeComponent(rowsContainer.getComponentAt(componentCount - 1));
                        componentCount--;
                    }

                    rowsContainer.revalidate();

                }

                @Override
                public void onError(Object o, Throwable thrwbl, int i, String string) {

                    Dialog.show("Database Update", i + " " + string, "OK", "Cancel");

                }
            });

        });

        rowsContainer.add(update).add(delete)
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""))
                .add(new Label(""));

    }

    public void readAllOPDPatient(ListCollector list) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {

                    System.err.println("Empty response received.");
                    return;

                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {

                    responseString = responseString.substring(1, responseString.length() - 1);

                }

                System.out.println("after removing [] :- " + responseString);

                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                boolean findIsAvailable = false;

                int count = 1;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    if (Character.isDigit(currentChar)) {

                        valueBuilder.append(currentChar);
                        continue;

                    }

                    if (currentChar == '"') {

                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {

                            keyBuilder.append(currentChar);

                        } else {

                            valueBuilder.append(currentChar);

                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("age")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("patNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("opdNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("index")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {

                                StringBuilder sb = new StringBuilder();

                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                list.add(map_);

                                //list.addItem(sb.toString().trim());
                                //}
                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

            }

        };

        connection.setPost(false);

        String url = API_URL + "/opdpatientdata/all";

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void searchOPDPatient(Container rowsContainer, Callback callback, boolean fullTable, ListCollector list, String codeNo, String opdNo, String patNo, String diseasesType, String diseases, String sex, String dateFrom, String dateTo, String ageFrom, String ageTo, String status, Container otherContainer, Form form) {

        System.out.println("date from :- " + dateFrom + " , date to :- " + dateTo + " age from :- " + dateFrom + " date to :- " + dateTo);

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println("Raw Response: " + responseString);

                if (responseString == null || responseString.isEmpty()) {
                    System.err.println("Empty response received.");
                    return;
                }

                if (responseString.startsWith("[") && responseString.endsWith("]")) {
                    responseString = responseString.substring(1, responseString.length() - 1);
                }

                //System.out.println("after removing [] :- " + responseString);
                Map<String, Object> map = new HashMap<>();
                StringBuilder keyBuilder = new StringBuilder();
                StringBuilder valueBuilder = new StringBuilder();
                boolean isKey = true;
                boolean insideString = false;

                int count = 1;

                for (int i = 0; i < responseString.length(); i++) {

                    char currentChar = responseString.charAt(i);

                    /*if (Character.isDigit(currentChar)) {
                        valueBuilder.append(currentChar);
                        continue;
                    }*/
                    if (currentChar == '"') {
                        insideString = !insideString;

                    } else if (insideString) {

                        if (isKey) {
                            keyBuilder.append(currentChar);
                        } else {
                            valueBuilder.append(currentChar);
                        }

                    } else {

                        if (currentChar == ':') {

                            if (keyBuilder.toString().equals("index")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("age")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("patNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            } else if (keyBuilder.toString().equals("opdNo")) {

                                ++i;

                                while (i < responseString.length() && Character.isDigit(responseString.charAt(i))) {

                                    valueBuilder.append(responseString.charAt(i));

                                    ++i;

                                }

                                --i;

                            }

                            isKey = false;

                        } else if (currentChar == ',' || currentChar == '}') {

                            map.put(keyBuilder.toString().trim(), valueBuilder.toString().trim());

                            keyBuilder.setLength(0);
                            valueBuilder.setLength(0);
                            isKey = true;

                            if (currentChar == '}') {
                                StringBuilder sb = new StringBuilder();
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

                                Map<String, Object> map_ = new HashMap<>();
                                for (String j : map.keySet()) {
                                    map_.put(j, map.get(j));
                                }
                                list.add(map_);

                                addButtons(otherContainer, map_, form);
                                map.clear();
                            }
                        }
                    }
                }

                if (rowsContainer != null && callback != null) {
                    callback.onSucess("Search results retrieved successfully.");
                } else if (callback != null) {
                    callback.onSucess("No data found for the search query.");
                }
            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {
                if (callback != null) {
                    callback.onError(null, new NullPointerException(), code, message);
                }
            }
        };

        connection.setPost(false);

        // Construct the query parameters dynamically and only include non-empty values
        StringBuilder urlBuilder = new StringBuilder(API_URL + "/opdpatientdata/search?");

        if (codeNo != null && !codeNo.isEmpty()) {
            urlBuilder.append("codeNo=").append(codeNo).append("&");
        }
        if (opdNo != null && !opdNo.isEmpty()) {
            urlBuilder.append("opdNo=").append(opdNo).append("&");
        }
        if (patNo != null && !patNo.isEmpty()) {
            urlBuilder.append("patNo=").append(patNo).append("&");
        }
        if (diseasesType != null && !diseasesType.isEmpty()) {
            urlBuilder.append("diseasesType=").append(diseasesType).append("&");
        }
        if (diseases != null && !diseases.isEmpty()) {
            urlBuilder.append("diseases=").append(diseases).append("&");
        }
        if (sex != null && !sex.isEmpty()) {
            urlBuilder.append("sex=").append(sex).append("&");
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            urlBuilder.append("dateFrom=").append(dateFrom).append("&");
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            urlBuilder.append("dateTo=").append(dateTo).append("&");
        }
        if (ageFrom != null && !ageFrom.isEmpty()) {
            urlBuilder.append("ageFrom=").append(ageFrom).append("&");
        }
        if (ageTo != null && !ageTo.isEmpty()) {
            urlBuilder.append("ageTo=").append(ageTo).append("&");
        }
        if (status != null && !status.isEmpty()) {
            urlBuilder.append("status=").append(status).append("&");
        }

        // Remove the trailing "&" if present
        String url = urlBuilder.toString();
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }

        System.out.println("Search URL: " + url);

        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);
    }

}
