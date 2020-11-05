package system;

import engine.TimeStepSimulator;
import engine.cutCondition.TimeCutCondition;

import java.util.Random;

public class PedestrianSystem {
    // Parameters 1
    public static final double MIN_RADIUS = 0.15; // m
    public static final double MAX_RADIUS = 0.32; // m
    public static final double MAX_VELOCITY = 1.55; // m/s

    // Parameters 2
//    public static final double MIN_RADIUS = 0.10; // m
//    public static final double MAX_RADIUS = 0.37; // m
//    public static final double MAX_VELOCITY = 0.95; // m/s

    private static final double RADIUS = 4;  // m
    public static final double LENGTH = RADIUS * 2 * Math.PI;  // m
    public static final double WIDTH = 5 * (MAX_RADIUS + MIN_RADIUS);  // m
    public static final double BETA = 0.9;
    public static final double TIME_DELTA = 0.02;  // s
    public static final double SAVE_TIME_DELTA = 0.02; // s
    public static final double TAU = 0.5; // s
    public static final double SIMULATION_TIME = 20; // s

    public static final int PARTICLES_QUANTITY = 100;
    public static final String FILENAME = "output";

    public static void runSimulation() {
        Random random = new Random();
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(PARTICLES_QUANTITY, random, LENGTH, WIDTH, MAX_RADIUS, MIN_RADIUS, MAX_VELOCITY, BETA);
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator(FILENAME);
        TimeCutCondition timeCutCondition = new TimeCutCondition(SIMULATION_TIME);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), LENGTH, WIDTH, MIN_RADIUS, MAX_RADIUS, MAX_VELOCITY, BETA, TAU);
        timeStepSimulator.simulate();
    }
}
