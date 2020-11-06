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
    private static final double TIME_DELTA_1 = 0.04;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_1 = TIME_DELTA_1; // s

    // Parameters 2
    private static final double MIN_RADIUS_2 = 0.10; // m
    private static final double MAX_RADIUS_2 = 0.37; // m
    private static final double MAX_VELOCITY_2 = 0.95; // m/s
    private static final double RADIUS_2 = 4;  // m
    private static final double LENGTH_2 = RADIUS_2 * 2 * Math.PI;  // m
    private static final double WIDTH_2 = 5 * (MAX_RADIUS_2 + MIN_RADIUS_2);  // m
    private static final double BETA_2 = 0.9;
    private static final double TAU_2 = 0.5; // s
    private static final double TIME_DELTA_2 = 0.04;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_2 = TIME_DELTA_2; // s

    private static final double SIMULATION_TIME = 50; // s
    private static final int PARTICLES_QUANTITY = 100;
    private static final String FILENAME = "output100" ;

    public static void runSimulation(String fileName, int particlesQuantity, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double length, double width, double beta, double tau, boolean writeBoundaryParticles, boolean writeWalls) {
        Random random = new Random();
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(particlesQuantity, random, length, width, maxRadius, minRadius, maxVelocity, beta);
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator(fileName, length, width, writeBoundaryParticles, writeWalls);
        TimeCutCondition timeCutCondition = new TimeCutCondition(simulationTime);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(timeDelta, saveTimeDelta, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), length, width, minRadius, maxRadius, maxVelocity, beta, tau);
        timeStepSimulator.simulate();
    }

    public static void runSimulationParameters1() {
        runSimulation(FILENAME,
                PARTICLES_QUANTITY,
                TIME_DELTA_1,
                SAVE_TIME_DELTA_1,
                SIMULATION_TIME,
                MIN_RADIUS_1,
                MAX_RADIUS_1,
                MAX_VELOCITY_1,
                LENGTH_1,
                WIDTH_1,
                BETA_1,
                TAU_1,
                true,
                true);
    }

    public static void runSimulationParameters2() {
        runSimulation(FILENAME,
                PARTICLES_QUANTITY,
                TIME_DELTA_2,
                SAVE_TIME_DELTA_2,
                SIMULATION_TIME,
                MIN_RADIUS_2,
                MAX_RADIUS_2,
                MAX_VELOCITY_2,
                LENGTH_2,
                WIDTH_2,
                BETA_2,
                TAU_2,
                true,
                true);
    }

    public static void runSimulationsForFundamentalDiagram(int maxParticles, int particlesJump, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double widthMultiplier, double beta, double tau) {
        double length = 4 * 2 * Math.PI;  // m
        double width = widthMultiplier * (maxRadius + minRadius);  // m
        System.out.println("Starting simulations for Fundamental Diagram with width: " + widthMultiplier);
        for(int particlesQuantity = particlesJump; particlesQuantity <= maxParticles; particlesQuantity += particlesJump) {
            System.out.println("Simulating " + particlesQuantity + " particles.");
            runSimulation("width-" + (int)widthMultiplier + "-particles-" + particlesQuantity,
                    particlesQuantity,
                    timeDelta,
                    saveTimeDelta,
                    simulationTime,
                    minRadius,
                    maxRadius,
                    maxVelocity,
                    length,
                    width,
                    beta,
                    tau,
                    false,
                    false);
        }
    }

    public static void runFundamentalDiagrams() {
        runSimulationsForFundamentalDiagram(160, 5, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 3, BETA_1, TAU_1);
    }
}
