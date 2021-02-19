package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXCarRoundaboutIntersection implements Intersection {
    int noLanes;
    int roundaboutPassingTime;
    int exactNoCarsPassingAtOnce;
    // pentru fiecare directie exista un semafor care limiteaza numarul de masini care pot trece
    Semaphore[] semaphores;
    // bariera ca sa sincronizam toate masinile
    CyclicBarrier allCarsReachedBarrier;
    // bariera ca sa sincronizam numarul specificat de masini pentru toate directiile
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

    public int getExactNoCarsPassingAtOnce() {
        return exactNoCarsPassingAtOnce;
    }

    public void setExactNoCarsPassingAtOnce(int exactNoCarsPassingAtOnce) {
        this.exactNoCarsPassingAtOnce = exactNoCarsPassingAtOnce;
    }

    public Semaphore[] getSemaphores() {
        return semaphores;
    }

    public void setSemaphores(Semaphore[] semaphores) {
        this.semaphores = semaphores;
    }

    public CyclicBarrier getAllCarsReachedBarrier() {
        return allCarsReachedBarrier;
    }

    public void setAllCarsReachedBarrier(CyclicBarrier allCarsReachedBarrier) {
        this.allCarsReachedBarrier = allCarsReachedBarrier;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
}
