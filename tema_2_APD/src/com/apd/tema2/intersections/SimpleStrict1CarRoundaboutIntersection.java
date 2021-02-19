package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrict1CarRoundaboutIntersection implements Intersection {
    int noLanes;
    int roundaboutPassingTime;
    // pentru fiecare directie exista un semafor care limiteaza accesul pentru o singura masina
    Semaphore[] semaphores;
    // bariera ca sa sincronizam numarul de masini pentru toate directiile
    CyclicBarrier barrier;

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

    public Semaphore[] getSemaphores() {
        return semaphores;
    }

    public void setSemaphores(Semaphore[] semaphores) {
        this.semaphores = semaphores;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
}
