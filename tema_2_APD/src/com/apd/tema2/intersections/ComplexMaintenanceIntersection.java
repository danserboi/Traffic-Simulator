package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.utils.Pair;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ComplexMaintenanceIntersection implements Intersection {
    int freeLanes;
    int totalInitialLanes;
    int passingCarsInOneGo;
    // semafor folosit ca mutex pentru a realiza operatiile legate de sosirea unei masini
    Semaphore arrivingSemaphore;
    // lista cozilor care contin masinile incolonate in ordinea in care au ajuns pe fiecare banda veche
    List<Pair<LinkedBlockingQueue<Integer>, Integer>> initialLanesCarsQueues;
    // pentru fiecare banda noua construim coada cu benzile vechi
    List<List<Pair<LinkedBlockingQueue<Integer>, Integer>>> newLanesQueuesOfOldLanes;
    // bariera folosita pentru a sincroniza toate masinile
    CyclicBarrier carsBarrier;

    public int getFreeLanes() {
        return freeLanes;
    }

    public void setFreeLanes(int freeLanes) {
        this.freeLanes = freeLanes;
    }

    public int getTotalInitialLanes() {
        return totalInitialLanes;
    }

    public void setTotalInitialLanes(int totalInitialLanes) {
        this.totalInitialLanes = totalInitialLanes;
    }

    public int getPassingCarsInOneGo() {
        return passingCarsInOneGo;
    }

    public void setPassingCarsInOneGo(int passingCarsInOneGo) {
        this.passingCarsInOneGo = passingCarsInOneGo;
    }

    public Semaphore getArrivingSemaphore() {
        return arrivingSemaphore;
    }

    public void setArrivingSemaphore(Semaphore arrivingSemaphore) {
        this.arrivingSemaphore = arrivingSemaphore;
    }

    public List<Pair<LinkedBlockingQueue<Integer>, Integer>> getInitialLanesCarsQueues() {
        return initialLanesCarsQueues;
    }

    public void setInitialLanesCarsQueues(List<Pair<LinkedBlockingQueue<Integer>, Integer>> initialLanesCarsQueues) {
        this.initialLanesCarsQueues = initialLanesCarsQueues;
    }

    public List<List<Pair<LinkedBlockingQueue<Integer>, Integer>>> getNewLanesQueuesOfOldLanes() {
        return newLanesQueuesOfOldLanes;
    }

    public void setNewLanesQueuesOfOldLanes(List<List<Pair<LinkedBlockingQueue<Integer>, Integer>>> newLanesQueuesOfOldLanes) {
        this.newLanesQueuesOfOldLanes = newLanesQueuesOfOldLanes;
    }

    public CyclicBarrier getCarsBarrier() {
        return carsBarrier;
    }

    public void setCarsBarrier(CyclicBarrier carsBarrier) {
        this.carsBarrier = carsBarrier;
    }
}
