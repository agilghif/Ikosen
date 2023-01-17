package com.ikosen.geneticAlgorithm;

import java.util.ArrayList;

public class LocationList extends ArrayList<Location> {
    public boolean contains(Location location) {
        for (int i=0; i < size(); i++) {
            if (get(i).intersect(location)){
                return true;
            }
        }
        return false;
    }
}
