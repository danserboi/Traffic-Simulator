package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;

public class CrosswalkIntersection implements Intersection {
    Pedestrians pedestrians;

    public void setPedestrians(Pedestrians pedestrians) {
        this.pedestrians = pedestrians;
    }

}
