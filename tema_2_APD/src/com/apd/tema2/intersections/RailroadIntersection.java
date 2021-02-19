package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class RailroadIntersection implements Intersection {
    // semafor folosit ca mutex pentru a realiza operatiile legate de sosirea unei masini
    Semaphore arrivingSemaphore;
    // coada folosita pentru a introduce masinile in ordinea in care sosesc de pe primul sens
    LinkedBlockingQueue<Integer> firstSideQueue;
    // coada folosita pentru a introduce masinile in ordinea in care sosesc de pe al doilea sens
    LinkedBlockingQueue<Integer> secondSideQueue;
    // bariera folosita pentru a sincroniza toate masinile
    CyclicBarrier carsBarrier;

    public Semaphore getArrivingSemaphore() {
        return arrivingSemaphore;
    }

    public void setArrivingSemaphore(Semaphore arrivingSemaphore) {
        this.arrivingSemaphore = arrivingSemaphore;
    }

    public LinkedBlockingQueue<Integer> getFirstSideQueue() {
        return firstSideQueue;
    }

    public void setFirstSideQueue(LinkedBlockingQueue<Integer> firstSideQueue) {
        this.firstSideQueue = firstSideQueue;
    }

    public LinkedBlockingQueue<Integer> getSecondSideQueue() {
        return secondSideQueue;
    }

    public void setSecondSideQueue(LinkedBlockingQueue<Integer> secondSideQueue) {
        this.secondSideQueue = secondSideQueue;
    }

    public CyclicBarrier getCarsBarrier() {
        return carsBarrier;
    }

    public void setCarsBarrier(CyclicBarrier carsBarrier) {
        this.carsBarrier = carsBarrier;
    }

}
