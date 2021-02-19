package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleMaxXCarRoundaboutIntersection implements Intersection {
    int noLanes;
    int roundaboutPassingTime;
    int maxNoCarsPassingAtOnce;
    // pentru fiecare directie exista un semafor care limiteaza numarul maxim de masini care pot trece
    Semaphore[] semaphores;

    public int getNoLanes() {
        return noLanes;
    }

    public void setNoLanes(int noLanes) {
        this.noLanes = noLanes;
    }

    public int getRoundaboutPassingTime() {
        return roundaboutPassingTime;
    }

    public void setRoundaboutPassingTime(int roundaboutPassingTime) {
        this.roundaboutPassingTime = roundaboutPassingTime;
    }

    public int getMaxNoCarsPassingAtOnce() {
        return maxNoCarsPassingAtOnce;
    }

    public void setMaxNoCarsPassingAtOnce(int maxNoCarsPassingAtOnce) {
        this.maxNoCarsPassingAtOnce = maxNoCarsPassingAtOnce;
    }

    public Semaphore[] getSemaphores() {
        return semaphores;
    }

    public void setSemaphores(Semaphore[] semaphores) {
        this.semaphores = semaphores;
    }
}
