package com.example.opd.Reposatory;

import com.example.opd.Components.DatabaseInfo;
import com.example.opd.Model.Patient;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.ui.List;
import com.codename1.util.Callback;
import com.example.opd.Components.ListCollector;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PatientDatabaseHelper implements DatabaseInfo {

    ConnectionRequest connection;

    public void addPatient(Patient patient, Callback callback) {

        String url = API_URL + "/patient";

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                StringBuilder sb = new StringBuilder();

                String responseString = Util.readToString(input);

                sb.append("raw response :- ").append(responseString);

                if (callback != null) {

                    callback.onSucess(sb.toString());

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
        connection.setUrl(url);
        connection.setContentType("application/json");

        String jsonInputString = "{"
                + "\"firstName\": \"" + patient.getFirstName() + "\","
                + "\"lastName\": \"" + patient.getLastName() + "\","
                + "\"adress\": \"" + patient.getAdress() + "\","
                + "\"city\": \"" + patient.getCity() + "\","
                + "\"age\": \"" + patient.getAge() + "\","
                + "\"sex\": \"" + patient.getSex() + "\""
                + "}";

        connection.setRequestBody(jsonInputString);

        NetworkManager.getInstance().addToQueue(connection);

    }

    public void readAllPatient(List list, Callback callback) {

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                list.addItem(sb.toString().trim());

                                System.out.println(map);

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                //System.out.println(list);
                if (callback != null && list.size() > 0) {

                    callback.onSucess("Successfully find Patient data");

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message
            ) {
                System.err.println("Error Response Code: " + code + " Message: " + message);

                if (callback != null && list.size() > 0) {

                    callback.onSucess("Error Response Code: " + code + " Message: " + message);

                }

            }
        };

        String url = API_URL + "/patient/all";

        connection.setPost(false);
        connection.setUrl(url);

        NetworkManager.getInstance().addToQueue(connection);

    }

    public void findStudentByFirstName(String firstName, List list, Callback callback) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println(responseString);

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                list.addItem(sb.toString().trim());

                                System.out.println(map);

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                //System.out.println(list);
                if (callback != null && list.size() > 0) {

                    callback.onSucess("Successfully find Patient data");

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

                callback.onError(null, new NullPointerException(), code, message);

            }

        };

        String url = API_URL + "/patient/search?firstName=" + firstName;

        connection.setUrl(url);
        connection.setPost(false);
        NetworkManager.getInstance().addToQueue(connection);

    }

    public void findStudentByLastName(String firstName, List list, Callback callback) {

        connection = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                String responseString = Util.readToString(input);

                System.out.println(responseString);

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                list.addItem(sb.toString().trim());

                                System.out.println(map);

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

                //System.out.println(list);
                if (callback != null && list.size() > 0) {

                    callback.onSucess("Successfully find Patient data");

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {

                callback.onError(null, new NullPointerException(), code, message);

            }

        };

        String url = API_URL + "/patient/searchByLastName?lastName=" + firstName;

        connection.setUrl(url);
        connection.setPost(false);
        NetworkManager.getInstance().addToQueue(connection);

    }

    public void deletePatient(Patient patient, Callback callback) {

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

        String url = API_URL + "/patient/delete";

        //connection.setHttpMethod("DELETE");
        connection.setPost(true);
        connection.setUrl(url);
        connection.setContentType("application/json");

        System.out.println("all the data :- " + patient.toString());

        String jsonInputString = "{"
                + "\"firstName\": \"" + patient.getFirstName() + "\","
                + "\"lastName\": \"" + patient.getLastName() + "\","
                + "\"adress\": \"" + patient.getAdress() + "\","
                + "\"city\": \"" + patient.getCity() + "\","
                + "\"age\": \"" + patient.getAge() + "\","
                + "\"sex\": \"" + patient.getSex() + "\""
                + "}";

        connection.setRequestBody(jsonInputString);

        NetworkManager.getInstance().addToQueue(connection);

    }

    public void readAllPatient(ListCollector list) {

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                list.add(map_);

                                System.out.println("added :- " + list.get(list.list.size() - 1));

                                System.out.println(map);

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message
            ) {
                System.err.println("Error Response Code: " + code + " Message: " + message);

            }
        };

        String url = API_URL + "/patient/all";

        connection.setPost(false);
        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void readAllPatientByFirstName(String firstName, ListCollector list) {

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                list.add(map_);

                                System.out.println(map);

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message
            ) {
                System.err.println("Error Response Code: " + code + " Message: " + message);

            }
        };

        String url = API_URL + "/patient/search?firstName=" + firstName;

        connection.setPost(false);
        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void readAllPatientByLastName(String lastName, ListCollector list) {

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

                            //System.out.println(keyBuilder.toString());
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

                                /*for (Object val : map.values()) {

                                    sb.append(val).append(" ");

                                }*/
                                sb.append(map.get("firstName")).append(" ")
                                        .append(map.get("lastName")).append(" ")
                                        .append(map.get("adress")).append(" ")
                                        .append(map.get("city")).append(" ")
                                        .append(map.get("age")).append(" ")
                                        .append(map.get("sex"));

                                Map<String, Object> map_ = new HashMap<>();

                                for (String j : map.keySet()) {

                                    map_.put(j, map.get(j));

                                }

                                list.add(map_);

                                System.out.println("added :- " + list.get(list.size() - 1));

                                map.clear();

                                findIsAvailable = false;

                            }

                        }

                    }

                }

            }

            @Override
            protected void handleErrorResponseCode(int code, String message
            ) {
                System.err.println("Error Response Code: " + code + " Message: " + message);

            }
        };

        String url = API_URL + "/patient/search?lastName=" + lastName;

        connection.setPost(false);
        connection.setUrl(url);

        NetworkManager.getInstance().addToQueueAndWait(connection);

    }

    public void updatePatient(String id, Map<String, Object> updatedFields, Callback callback) {
        // Create the URL for the PUT request
        String url = API_URL + "/patient/update";

        // Create a new ConnectionRequest
        ConnectionRequest req = new ConnectionRequest() {
            @Override
            protected void readResponse(java.io.InputStream input) throws IOException {
                // Handle the response here

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

    private String listToJson(List list) {  // Using raw type List
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {  // Using traditional for-loop
            Object item = list.getModel().getItemAt(i);

            if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof List) {
                json.append(listToJson((List) item));  // Recursive call for nested lists
            } else if (item instanceof Map) {
                json.append(mapToJson((Map) item));  // Recursive call for maps
            } else {
                json.append("\"").append(item.toString()).append("\"");  // Fallback to string
            }

            if (i < list.size() - 1) {
                json.append(",");  // Add comma except for the last element
            }
        }
        json.append("]");
        return json.toString();
    }

}
