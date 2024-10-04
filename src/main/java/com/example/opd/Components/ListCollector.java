package com.example.opd.Components;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ListCollector {

    public List<Map<String, Object>> list = new ArrayList<>();

    public void add(Map<String, Object> map) {

        list.add(map);

    }

    public Map<String, Object> get(int i) {

        return list.get(i);

    }

    public int size() {

        return list.size();

    }

    public void clear() {

        list.clear();
        
    }

}
