package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static final Map<String, Intersection> cache = new HashMap<>();

    public static Intersection getIntersection(String handlerType) {
        if(!cache.containsKey(handlerType)) {
            switch (handlerType) {
                case "simple_semaphore" -> cache.put(handlerType, new SimpleSemaphoreIntersection());
                case "simple_n_roundabout" -> cache.put(handlerType, new SimpleNRoundaboutIntersection());
                case "simple_strict_1_car_roundabout" -> cache.put(handlerType, new SimpleStrict1CarRoundaboutIntersection());
                case "simple_strict_x_car_roundabout" -> cache.put(handlerType, new SimpleStrictXCarRoundaboutIntersection());
                case "simple_max_x_car_roundabout" -> cache.put(handlerType, new SimpleMaxXCarRoundaboutIntersection());
                case "priority_intersection" -> cache.put(handlerType, new PriorityIntersection());
                case "crosswalk" -> cache.put(handlerType, new CrosswalkIntersection());
                case "simple_maintenance" -> cache.put(handlerType, new SimpleMaintenanceIntersection());
                case "complex_maintenance" -> cache.put(handlerType, new ComplexMaintenanceIntersection());
                case "railroad" -> cache.put(handlerType, new RailroadIntersection());
            }
        }
        return cache.get(handlerType);
    }

}
