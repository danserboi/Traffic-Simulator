package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        Main.intersection = IntersectionFactory.getIntersection(handlerType);
        return switch (handlerType) {
            case "simple_semaphore" -> (ReaderHandler) (handlerType1, br) -> {

            };
            case "simple_n_roundabout" -> (ReaderHandler) (handlerType12, br) -> {
                String[] line = br.readLine().split(" ");
                SimpleNRoundaboutIntersection intersection = (SimpleNRoundaboutIntersection) Main.intersection;
                intersection.setMaxNoCarsPassingAtOnce(Integer.parseInt(line[0]));
                intersection.setRoundaboutTime(Integer.parseInt(line[1]));
                intersection.setSemaphore(new Semaphore(Integer.parseInt(line[0])));
            };
            case "simple_strict_1_car_roundabout" -> (ReaderHandler) (handlerType13, br) -> {
                String[] line = br.readLine().split(" ");
                SimpleStrict1CarRoundaboutIntersection intersection = (SimpleStrict1CarRoundaboutIntersection) Main.intersection;
                intersection.setNoLanes(Integer.parseInt(line[0]));
                intersection.setRoundaboutPassingTime(Integer.parseInt(line[1]));
                Semaphore[] semaphores = new Semaphore[Integer.parseInt(line[0])];
                for (int i = 0; i < semaphores.length; i++) {
                    semaphores[i] = new Semaphore(1);
                }
                intersection.setSemaphores(semaphores);
                intersection.setBarrier(new CyclicBarrier(Integer.parseInt(line[0])));
            };
            case "simple_strict_x_car_roundabout" -> (ReaderHandler) (handlerType14, br) -> {
                String[] line = br.readLine().split(" ");
                SimpleStrictXCarRoundaboutIntersection intersection = (SimpleStrictXCarRoundaboutIntersection) Main.intersection;
                intersection.setNoLanes(Integer.parseInt(line[0]));
                intersection.setRoundaboutPassingTime(Integer.parseInt(line[1]));
                intersection.setExactNoCarsPassingAtOnce(Integer.parseInt(line[2]));
                Semaphore[] semaphores = new Semaphore[Integer.parseInt(line[0])];
                for (int i = 0; i < semaphores.length; i++) {
                    semaphores[i] = new Semaphore(Integer.parseInt(line[2]));
                }
                intersection.setSemaphores(semaphores);
                intersection.setAllCarsReachedBarrier(new CyclicBarrier(Main.carsNo));
                intersection.setBarrier(new CyclicBarrier(Integer.parseInt(line[0]) * Integer.parseInt(line[2])));
            };
            case "simple_max_x_car_roundabout" -> (ReaderHandler) (handlerType15, br) -> {
                String[] line = br.readLine().split(" ");
                SimpleMaxXCarRoundaboutIntersection intersection = (SimpleMaxXCarRoundaboutIntersection) Main.intersection;
                intersection.setNoLanes(Integer.parseInt(line[0]));
                intersection.setRoundaboutPassingTime(Integer.parseInt(line[1]));
                intersection.setMaxNoCarsPassingAtOnce(Integer.parseInt(line[2]));
                Semaphore[] semaphores = new Semaphore[Integer.parseInt(line[0])];
                for (int i = 0; i < semaphores.length; i++) {
                    semaphores[i] = new Semaphore(Integer.parseInt(line[2]));
                }
                intersection.setSemaphores(semaphores);
            };
            case "priority_intersection" -> (ReaderHandler) (handlerType16, br) -> {
                String[] line = br.readLine().split(" ");
                PriorityIntersection intersection = (PriorityIntersection) Main.intersection;
                intersection.setNoCarsWithHighPriority(Integer.parseInt(line[0]));
                intersection.setNoCarsWithLowPriority(Integer.parseInt(line[1]));
                intersection.setNoCarsWithHighPriorityInIntersection(new AtomicInteger());
                // este folosita varianta fair a semaforului pentru a pastra ordinea FIFO a masinilor fara prioritate
                intersection.setSemaphoreForCarsWithLowPriority(new Semaphore(1, true));
            };
            case "crosswalk" -> (ReaderHandler) (handlerType17, br) -> {
                String[] line = br.readLine().split(" ");
                Main.pedestrians = new Pedestrians(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                ((CrosswalkIntersection) Main.intersection).setPedestrians(Main.pedestrians);
            };
            case "simple_maintenance" -> (ReaderHandler) (handlerType18, br) -> {
                String[] line = br.readLine().split(" ");
                SimpleMaintenanceIntersection intersection = (SimpleMaintenanceIntersection) Main.intersection;
                intersection.setNoCarsPassingAtOnce(Integer.parseInt(line[0]));
                intersection.setFirstSideBarrier(new CyclicBarrier(Integer.parseInt(line[0])));
                intersection.setSecondSideBarrier(new CyclicBarrier(Integer.parseInt(line[0])));
                intersection.setFirstSideSemaphore(new Semaphore(Integer.parseInt(line[0])));
                intersection.setSecondSideSemaphore(new Semaphore(0));
            };
            case "complex_maintenance" -> (ReaderHandler) (handlerType19, br) -> {
                String[] line = br.readLine().split(" ");
                ComplexMaintenanceIntersection intersection = (ComplexMaintenanceIntersection) Main.intersection;
                intersection.setFreeLanes(Integer.parseInt(line[0]));
                intersection.setTotalInitialLanes(Integer.parseInt(line[1]));
                intersection.setPassingCarsInOneGo(Integer.parseInt(line[2]));
                intersection.setArrivingSemaphore(new Semaphore(1, true));
                List<Pair<LinkedBlockingQueue<Integer>, Integer>> initialLanesCarsQueues = Collections.synchronizedList(new ArrayList<>());
                for (int i = 0; i < Integer.parseInt(line[1]); i++) {
                    initialLanesCarsQueues.add(new Pair<>(new LinkedBlockingQueue<>(), Integer.parseInt(line[2])));
                }
                intersection.setInitialLanesCarsQueues(initialLanesCarsQueues);
                List<List<Pair<LinkedBlockingQueue<Integer>, Integer>>> newLanesQueuesOfOldLanes = Collections.synchronizedList(new ArrayList<>());
                for (int i = 0; i < Integer.parseInt(line[0]); i++) {
                    List<Pair<LinkedBlockingQueue<Integer>, Integer>> queue = Collections.synchronizedList(new ArrayList<>());
                    int start, end;
                    if (i != Integer.parseInt(line[0]) - 1) {
                        start = i * Integer.parseInt(line[1]) / Integer.parseInt(line[0]);
                        end = (i + 1) * Integer.parseInt(line[1]) / Integer.parseInt(line[0]);
                    } else {
                        start = i * Integer.parseInt(line[1]) / Integer.parseInt(line[0]);
                        end = Integer.parseInt(line[1]);
                    }
                    for (int j = start; j < end; j++) {
                        queue.add(initialLanesCarsQueues.get(j));
                    }
                    newLanesQueuesOfOldLanes.add(queue);
                }
                intersection.setNewLanesQueuesOfOldLanes(newLanesQueuesOfOldLanes);
                intersection.setCarsBarrier(new CyclicBarrier(Main.carsNo));
            };
            case "railroad" -> (ReaderHandler) (handlerType110, br) -> {
                RailroadIntersection intersection = (RailroadIntersection) Main.intersection;
                intersection.setArrivingSemaphore(new Semaphore(1, true));
                intersection.setFirstSideQueue(new LinkedBlockingQueue<>());
                intersection.setSecondSideQueue(new LinkedBlockingQueue<>());
                intersection.setCarsBarrier(new CyclicBarrier(Main.carsNo));
            };
            default -> null;
        };
    }

}
