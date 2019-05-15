package com.example.scotlandyard.map.roadmap;

import java.io.Serializable;
import java.util.ArrayList;

public class RoadMap implements Serializable {
    ArrayList<Entry> entries;

    public RoadMap() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public int getNumberOfEntries() {
        return entries.size();
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
}
