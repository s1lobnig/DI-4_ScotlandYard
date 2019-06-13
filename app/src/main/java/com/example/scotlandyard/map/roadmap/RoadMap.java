package com.example.scotlandyard.map.roadmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoadMap implements Serializable {
    private List<Entry> entries;

    public RoadMap() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public int getNumberOfEntries() {
        return entries.size();
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
