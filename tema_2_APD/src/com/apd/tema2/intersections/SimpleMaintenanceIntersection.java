package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaintenanceIntersection implements Intersection {
    int noCarsPassingAtOnce;
    // bariera pentru a sincroniza masinile de pe primul sens de mers
    CyclicBarrier firstSideBarrier;
    // bariera pentru a sincroniza masinile de pe al doilea sens de mers
    CyclicBarrier secondSideBarrier;
    // semafor pentru a sincroniza masinile de pe primul sens de mers
    Semaphore firstSideSemaphore;
    // semafor pentru a sincroniza masinile de pe al doilea sens de mers
    Semaphore secondSideSemaphore;

    public int getNoCarsPassingAtOnce() {
        return noCarsPassingAtOnce;
    }

    public void setNoCarsPassingAtOnce(int noCarsPassingAtOnce) {
        this.noCarsPassingAtOnce = noCarsPassingAtOnce;
    }

    public CyclicBarrier getFirstSideBarrier() {
        return firstSideBarrier;
    }

    public void setFirstSideBarrier(CyclicBarrier firstSideBarrier) {
        this.firstSideBarrier = firstSideBarrier;
    }

    public CyclicBarrier getSecondSideBarrier() {
        return secondSideBarrier;
    }

    public void setSecondSideBarrier(CyclicBarrier secondSideBarrier) {
        this.secondSideBarrier = secondSideBarrier;
    }

    public Semaphore getFirstSideSemaphore() {
        return firstSideSemaphore;
    }

    public void setFirstSideSemaphore(Semaphore firstSideSemaphore) {
        this.firstSideSemaphore = firstSideSemaphore;
    }

    public Semaphore getSecondSideSemaphore() {
        return secondSideSemaphore;
    }

    public void setSecondSideSemaphore(Semaphore secondSideSemaphore) {
        this.secondSideSemaphore = secondSideSemaphore;
    }
}
