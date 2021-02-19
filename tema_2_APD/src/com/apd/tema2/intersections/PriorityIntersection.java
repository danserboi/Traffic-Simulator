package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection {
    int noCarsWithHighPriority;
    int noCarsWithLowPriority;
    // variabila atomica folosita pentru a retine numarul de masini cu prioritate aflate in intersectie
    AtomicInteger noCarsWithHighPriorityInIntersection;
    // semafor pentru masinile fara prioritate care pot intra doar cand nu se afla nicio masina cu prioritate in intersectie
    Semaphore semaphoreForCarsWithLowPriority;

    public int getNoCarsWithHighPriority() {
        return noCarsWithHighPriority;
    }

    public void setNoCarsWithHighPriority(int noCarsWithHighPriority) {
        this.noCarsWithHighPriority = noCarsWithHighPriority;
    }

    public int getNoCarsWithLowPriority() {
        return noCarsWithLowPriority;
    }

    public void setNoCarsWithLowPriority(int noCarsWithLowPriority) {
        this.noCarsWithLowPriority = noCarsWithLowPriority;
    }

    public AtomicInteger getNoCarsWithHighPriorityInIntersection() {
        return noCarsWithHighPriorityInIntersection;
    }

    public void setNoCarsWithHighPriorityInIntersection(AtomicInteger noCarsWithHighPriorityInIntersection) {
        this.noCarsWithHighPriorityInIntersection = noCarsWithHighPriorityInIntersection;
    }

    public Semaphore getSemaphoreForCarsWithLowPriority() {
        return semaphoreForCarsWithLowPriority;
    }

    public void setSemaphoreForCarsWithLowPriority(Semaphore semaphoreForCarsWithLowPriority) {
        this.semaphoreForCarsWithLowPriority = semaphoreForCarsWithLowPriority;
    }
}
