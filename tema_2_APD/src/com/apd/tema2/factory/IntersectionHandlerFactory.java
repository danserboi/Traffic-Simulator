package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.IntersectionHandler;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;
import com.apd.tema2.utils.Pair;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale IntersectionHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> (IntersectionHandler) car -> {
                // masina a ajuns la semafor
                System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                // masina asteapta la semafor timpul specificat
                try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina are verde la semafor si pleaca
                System.out.println("Car " + car.getId() + " has waited enough, now driving...");
            };
            case "simple_n_roundabout" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                SimpleNRoundaboutIntersection intersection = (SimpleNRoundaboutIntersection) Main.intersection;
                // masina a ajuns la sensul giratoriu
                System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                // masina asteapta un anumit timp specificat la sensul giratoriu
                try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina va intra in sensul giratoriu doar daca nu sunt mai multe masini decat numarul specificat
                try {
                    intersection.getSemaphore().acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina intra in sensul giratoriu
                System.out.println("Car " + car.getId() + " has entered the roundabout");
                // masina paraseste sensul giratoriu dupa perioada de timp precizata
                try {
                    sleep(intersection.getRoundaboutTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                        intersection.getRoundaboutTime() / 1000 + " seconds");
                // acum o alta masina poate intra in sens
                intersection.getSemaphore().release(1);
            };
            case "simple_strict_1_car_roundabout" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                SimpleStrict1CarRoundaboutIntersection intersection = (SimpleStrict1CarRoundaboutIntersection) Main.intersection;
                // o masina dintr-o anumita directie a ajuns la sensul giratoriu
                System.out.println("Car " + car.getId() + " has reached the roundabout");
                // masina poate intra in sensul giratoriu doar daca:
                // 1) nu exista deja alta masina de pe aceeasi directie care vrea sa intre sau care a intrat in sens
                try {
                    intersection.getSemaphores()[car.getStartDirection()].acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 2) din fiecare directie exista o masina care doreste sa intre
                try {
                    intersection.getBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // masina intra in sensul giratoriu
                System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                // masina paraseste sensul giratoriu dupa un anumit timp specificat
                try {
                    sleep(intersection.getRoundaboutPassingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                        intersection.getRoundaboutPassingTime() / 1000 + " seconds");
                // trebuie ca toate masinile de pe toate directiile sa paraseasca sensul giratoriu inainte de o noua runda
                try {
                    intersection.getBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // o alta masina de pe aceeasi directie are permisiunea sa intre in sensul giratoriu
                intersection.getSemaphores()[car.getStartDirection()].release(1);
            };
            case "simple_strict_x_car_roundabout" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                SimpleStrictXCarRoundaboutIntersection intersection = (SimpleStrictXCarRoundaboutIntersection) Main.intersection;
                // o masina dintr-o directie a ajuns la sensul giratoriu
                System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                // masina poate fi selectata sa intre in sensul giratoriu doar daca:
                // 1) toate masinile au ajuns la sensul giratoriu
                try {
                    intersection.getAllCarsReachedBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // 2) nu a fost atins inca numarul specificat de masini care pot intra dintr-o directie
                try {
                    intersection.getSemaphores()[car.getStartDirection()].acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina este selectata sa intre
                System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());
                // masina poate intra doar daca s-a atins numarul specificat de masini pentru toate directiile
                try {
                    intersection.getBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // masina intra in sensul giratoriu
                System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                // masina paraseste sensul giratoriu dupa un anumit timp
                try {
                    sleep(intersection.getRoundaboutPassingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                        intersection.getRoundaboutPassingTime() / 1000 + " seconds");
                // toate masinile de pe toate directiile trebuie sa paraseasca sensul giratoriu inainte de o noua runda
                try {
                    intersection.getBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // o alta masina de pe aceeasi directie are permisiunea sa fie selectata pentru a intra in sensul giratoriu
                intersection.getSemaphores()[car.getStartDirection()].release(1);
            };
            case "simple_max_x_car_roundabout" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                SimpleMaxXCarRoundaboutIntersection intersection = (SimpleMaxXCarRoundaboutIntersection) Main.intersection;
                // dureaza un timp pana masina ajunge la sensul giratoriu
                try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina ajunge la sensul giratoriu dupa timpul specificat
                System.out.println("Car " + car.getId() + " has reached the roundabout from lane " + car.getStartDirection());
                // masina poate intra in sensul giratoriu doar daca numarul maxim de masini pentru o directie nu este depasit
                try {
                    intersection.getSemaphores()[car.getStartDirection()].acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina intra in sensul giratoriu
                System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());
                // masina paraseste sensul giratoriu dupa timpul specificat
                try {
                    sleep(intersection.getRoundaboutPassingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                        intersection.getRoundaboutPassingTime() / 1000 + " seconds");
                // o alta masina de pe aceeasi directie are permisiunea sa intre in sensul giratoriu
                intersection.getSemaphores()[car.getStartDirection()].release(1);
            };
            case "priority_intersection" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                PriorityIntersection intersection = (PriorityIntersection) Main.intersection;
                // trece un timp pana masina ajunge la intersectie
                try {
                    sleep(car.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masinile cu prioritate vor intra in intersectie in orice moment
                if (car.getPriority() > 1) {
                    // masinile fara prioritate nu pot intra cat timp exista o masina cu prioritate in intersectie
                    if (intersection.getNoCarsWithHighPriorityInIntersection().get() == 0) {
                        try {
                            intersection.getSemaphoreForCarsWithLowPriority().acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // incrementam numarul de masini cu prioritate aflate in intersectie
                    intersection.getNoCarsWithHighPriorityInIntersection().incrementAndGet();
                    // masina cu prioritate a intrat in intersectie
                    System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
                    // masina paraseste sensul giratoriu dupa un timp
                    try {
                        sleep(Constants.PRIORITY_INTERSECTION_PASSING);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
                    // decrementam numarul de masini cu prioritate aflate in intersectie
                    intersection.getNoCarsWithHighPriorityInIntersection().decrementAndGet();
                    // masinile fara prioritate pot intra cat timp nu exista o masina cu prioritate in intersectie
                    if (intersection.getNoCarsWithHighPriorityInIntersection().get() == 0) {
                        intersection.getSemaphoreForCarsWithLowPriority().release();
                    }
                } else {
                    // masina fara prioritate incearca sa intre in intersectie
                    System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
                    // masina fara prioritate va intra cand nu va exista nicio masina cu prioritate in intersectie
                    try {
                        intersection.getSemaphoreForCarsWithLowPriority().acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // masina fara prioritate a intrat in intersectie
                    System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
                    // urmatoarea masina fara prioritate poate intra in intersectie
                    intersection.getSemaphoreForCarsWithLowPriority().release();
                }
            };
            case "crosswalk" -> (IntersectionHandler) car -> {
                // tinem minte ultima valoare pentru trecerea semaforizata
                // initializam cu true care reprezinta rosu pt masini
                // deoarece la start masinile au verde si vrem sa avem alternanta inca de la inceput
                boolean lastPass = true;
                while (!Main.pedestrians.isFinished()) {
                    synchronized (Main.pedestrians) {
                        try {
                            Main.pedestrians.wait();
                            // un thread va afisa un mesaj doar daca difera de mesajul precedent
                            if (lastPass != Main.pedestrians.isPass()) {
                                // afisam culoarea pe care o intalneste masina curenta
                                if (Main.pedestrians.isPass()) {
                                    System.out.println("Car " + car.getId() + " has now red light");
                                    lastPass = !Main.pedestrians.isPass();
                                } else {
                                    System.out.println("Car " + car.getId() + " has now green light");
                                    lastPass = Main.pedestrians.isPass();
                                }
                            }
                            lastPass = Main.pedestrians.isPass();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            case "simple_maintenance" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                SimpleMaintenanceIntersection intersection = (SimpleMaintenanceIntersection) Main.intersection;
                // masina a ajuns in punctul unde se lucreaza
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has reached the bottleneck");
                // masinile circula alternativ un numar exact si trebuie indeplinite 2 conditii:
                // 1) sa se adune numarul de masini specificat pe sensul lor
                // 2) sa existe permisiunea circularii pe sensul lor
                try {
                    if (car.getStartDirection() == 0) {
                        intersection.getFirstSideBarrier().await();
                        intersection.getFirstSideSemaphore().acquire(1);
                    } else {
                        intersection.getSecondSideBarrier().await();
                        intersection.getSecondSideSemaphore().acquire(1);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // masina a trecut de punctul unde se lucreaza
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has passed the bottleneck");
                // pentru a permite masinilor din sensul celalalt sa circule
                // trebuie ca toate masinile din sensul curent sa fi trecut de punctul in lucru
                // pentru fiecare masina trecuta din sensul curent o alta masina din sensul opus va castiga permisiune
                try {
                    if (car.getStartDirection() == 0) {
                        intersection.getFirstSideBarrier().await();
                        intersection.getSecondSideSemaphore().release(1);
                    } else {
                        intersection.getSecondSideBarrier().await();
                        intersection.getFirstSideSemaphore().release(1);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            };
            case "complex_maintenance" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                ComplexMaintenanceIntersection intersection = (ComplexMaintenanceIntersection) Main.intersection;
                // operatiile legate de sosirea unei masini reprezinta o regiune critica
                // deoarece masinile trebuie sa porneasca in ordinea in care au ajuns
                // astfel, folosim un semafor initializat cu 1, care se comporta ca un mutex
                try {
                    intersection.getArrivingSemaphore().acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " has come from the lane number " + car.getStartDirection());
                intersection.getInitialLanesCarsQueues().get(car.getStartDirection()).getFirst().offer(car.getId());
                intersection.getArrivingSemaphore().release(1);
                // repartitia pe noile benzi incepe in momentul in care toate masinile s-au incolonat
                try {
                    intersection.getCarsBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // verificam daca masina se afla in varful vreunei cozi
                verify_queue:
                while (true) {
                    // pe iteratia curenta, ne asiguram ca un singur thread efectueaza operatii pe structurile noastre de date
                    synchronized (intersection.getNewLanesQueuesOfOldLanes()) {
                        // parcurgem cozile pentru benzile noi
                        for (int i = 0; i < intersection.getNewLanesQueuesOfOldLanes().size(); i++) {
                            List<Pair<LinkedBlockingQueue<Integer>, Integer>> newLaneQueueOfOldLanes = intersection.getNewLanesQueuesOfOldLanes().get(i);
                            if (!newLaneQueueOfOldLanes.isEmpty()) {
                                // extragem prima banda veche din coada benzii noi
                                Pair<LinkedBlockingQueue<Integer>, Integer> firstOldLane = newLaneQueueOfOldLanes.get(0);
                                // verificam daca masina curenta se afla prima in coada
                                if (firstOldLane.getFirst().peek() == car.getId()) {
                                    // masina intra pe noua banda
                                    System.out.println("Car " + car.getId() + " from the lane " + car.getStartDirection() + " has entered lane number " + i);
                                    // daca pe banda veche nu mai e alta masina, inseamna ca aceasta s-a golit
                                    if (firstOldLane.getFirst().size() == 1) {
                                        System.out.println("The initial lane " + car.getStartDirection() + " has been emptied and removed from the new lane queue");
                                        newLaneQueueOfOldLanes.remove(firstOldLane);
                                    } else {
                                        int permits = firstOldLane.getSecond();
                                        if (permits == 1) {
                                            // numarul maxim de masini care pot pleca la o trecere a fost atins si vechea banda a fost mutata in coada
                                            firstOldLane.setSecond(intersection.getPassingCarsInOneGo());
                                            System.out.println("The initial lane " + car.getStartDirection() + " has no permits and is moved to the back of the new lane queue");
                                            newLaneQueueOfOldLanes.remove(firstOldLane);
                                            newLaneQueueOfOldLanes.add(firstOldLane);
                                        } else {
                                            // decrementam numarul de permisiuni de trecere pentru vechea banda
                                            firstOldLane.setSecond(permits - 1);
                                        }
                                    }
                                    // scoatem masina din coada
                                    firstOldLane.getFirst().poll();
                                    break verify_queue;
                                }
                            }
                        }
                    }
                }
            };
            case "railroad" -> (IntersectionHandler) car -> {
                // luam referinta la intersectia curenta
                RailroadIntersection intersection = (RailroadIntersection) Main.intersection;
                // operatiile legate de sosirea unei masini reprezinta o regiune critica
                // deoarece vrem sa pastram ordinea masinilor dupa ce trece trenul
                // astfel, folosim un semafor initializat cu 1, care se comporta ca un mutex
                try {
                    intersection.getArrivingSemaphore().acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // masina ajunge la calea ferata
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
                // introducem masina in coada corespunzatoare sensului pe care se deplaseaza
                if (car.getStartDirection() == 0) {
                    intersection.getFirstSideQueue().offer(car.getId());
                } else {
                    intersection.getSecondSideQueue().offer(car.getId());
                }
                intersection.getArrivingSemaphore().release(1);

                // inainte ca trenul sa plece, trebuie ca toate masinile sa ajunga la calea ferata
                try {
                    intersection.getCarsBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                if (car.getId() == 0) {
                    System.out.println("The train has passed, cars can now proceed");
                }
                // resincronizam masinile ca sa ne asiguram ca nu pleaca o masina inainte sa plece trenul
                try {
                    intersection.getCarsBarrier().await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                // masina poate pleaca abia atunci cand ii vine randul
                // avem o abordare de tip busy waiting: masina asteapta pana cand se afla in varful cozii sensului sau de mers
                // apoi afisam mesajul si o scoatem din coada
                if (car.getStartDirection() == 0) {
                    while (intersection.getFirstSideQueue().peek() != car.getId()) {
                    }
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has started driving");
                    intersection.getFirstSideQueue().poll();
                } else {
                    while (intersection.getSecondSideQueue().peek() != car.getId()) {
                    }
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has started driving");
                    intersection.getSecondSideQueue().poll();
                }

            };
            default -> null;
        };
    }
}
