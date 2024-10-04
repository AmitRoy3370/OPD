package com.example.opd.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OPDListCollector {

    List<Map<String, Object>> list = new ArrayList<>();

    public void add(Map<String, Object> map) {

        list.add(map);

    }

    public Map<String, Object> get(int i) {

        return list.get(i);

    }

    public int size() {

        return list.size();

    }

}
