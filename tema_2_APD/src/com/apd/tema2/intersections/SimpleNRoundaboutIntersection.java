package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundaboutIntersection implements Intersection {
    int maxNoCarsPassingAtOnce;
    int roundaboutTime;
    // semaforul limiteaza numarul de masini care pot intra in sensul giratoriu
    Semaphore semaphore;

    public int getMaxNoCarsPassingAtOnce() {
        return maxNoCarsPassingAtOnce;
    }

    public void setMaxNoCarsPassingAtOnce(int maxNoCarsPassingAtOnce) {
        this.maxNoCarsPassingAtOnce = maxNoCarsPassingAtOnce;
    }

    public int getRoundaboutTime() {
        return roundaboutTime;
    }

    public void setRoundaboutTime(int roundaboutTime) {
        this.roundaboutTime = roundaboutTime;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
}
