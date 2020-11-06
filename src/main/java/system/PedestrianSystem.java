package system;

import engine.TimeStepSimulator;
import engine.cutCondition.TimeCutCondition;

import java.util.Random;

public class PedestrianSystem {
    // Parameters 1
    private static final double MIN_RADIUS_1 = 0.15; // m
    private static final double MAX_RADIUS_1 = 0.32; // m
    private static final double MAX_VELOCITY_1 = 1.55; // m/s
    private static final double RADIUS_1 = 4;  // m
    private static final double LENGTH_1 = RADIUS_1 * 2 * Math.PI;  // m
    private static final double WIDTH_1 = 5 * (MAX_RADIUS_1 + MIN_RADIUS_1);  // m
    private static final double BETA_1 = 0.9;
    private static final double TAU_1 = 0.5; // s

    // Parameters 2
    private static final double MIN_RADIUS_2 = 0.10; // m
    private static final double MAX_RADIUS_2 = 0.37; // m
    private static final double MAX_VELOCITY_2 = 0.95; // m/s
    private static final double RADIUS_2 = 4;  // m
    private static final double LENGTH_2 = RADIUS_2 * 2 * Math.PI;  // m
    private static final double WIDTH_2 = 5 * (MAX_RADIUS_2 + MIN_RADIUS_2);  // m
    private static final double BETA_2 = 0.9;
    private static final double TAU_2 = 0.5; // s

    private static final double TIME_DELTA = 0.01;  // s
    private static final double SAVE_TIME_DELTA = 0.01; // s
    private static final double SIMULATION_TIME = 25; // s

    private static final int PARTICLES_QUANTITY = 100;
    private static final String FILENAME = "output";

    public static void runSimulation() {
        Random random = new Random();
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(PARTICLES_QUANTITY, random, LENGTH_1, WIDTH_1, MAX_RADIUS_1, MIN_RADIUS_1, MAX_VELOCITY_1, BETA_1);
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator(FILENAME, LENGTH_1, WIDTH_1, true, true);
        TimeCutCondition timeCutCondition = new TimeCutCondition(SIMULATION_TIME);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), LENGTH_1, WIDTH_1, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, BETA_1, TAU_1);
        timeStepSimulator.simulate();
    }

    public static void runSimulationsForFundamentalDiagram(int maxParticles, int particlesJump, int repetitions, double minRadius, double maxRadius, double maxVelocity, double widthMultiplier, double beta, double tau) {
        double length = 4 * 2 * Math.PI;  // m
        double width = widthMultiplier * (maxRadius + minRadius);  // m
        Random random = new Random();
        System.out.println("Starting simulations for Fundamental Diagram with width: " + widthMultiplier);
        for(int particlesQuantity = particlesJump; particlesQuantity <= maxParticles; particlesQuantity += particlesJump) {
            System.out.print(particlesQuantity + " particles:");
            for(int repetition = 1; repetition <= repetitions; repetition++) {
                System.out.print(" " + repetition);
                PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(particlesQuantity, random, length, width, maxRadius, minRadius, maxVelocity, beta);
                PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator("width-" + (int)widthMultiplier + "-particles-" + particlesQuantity + "-repetition-" + repetition , length, width, false, false);
                TimeCutCondition timeCutCondition = new TimeCutCondition(SIMULATION_TIME);
                TimeStepSimulator timeStepSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), length, width, minRadius, maxRadius, maxVelocity, beta, tau);
                timeStepSimulator.simulate();
            }
            System.out.println();
        }
    }

    public static void runFundamentalDiagrams() {
        runSimulationsForFundamentalDiagram(100, 5, 5, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 3, BETA_1, TAU_1);
    }
}
