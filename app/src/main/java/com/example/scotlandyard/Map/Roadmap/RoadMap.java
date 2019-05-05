package com.example.scotlandyard.Map.Roadmap;

import java.io.Serializable;
import java.util.ArrayList;

public class RoadMap implements Serializable {
    private ArrayList<Entry> entries;

    public RoadMap() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(Entry e) {
        entries.add(e);
    }

    public int getNumberOfEntries() {
        return entries.size();
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
}
