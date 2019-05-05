package com.example.scotlandyard.Map.Roadmap;

import java.util.ArrayList;

public class Roadmap {
    private ArrayList<Entry> entries;
    public Roadmap(){
        this.entries = new ArrayList<>();
    }

    public void addEntry(Entry e){
        entries.add(e);
    }

    public int getNumberOfEntries(){
        return entries.size();
    }
}
