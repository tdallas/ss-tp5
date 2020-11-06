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
    private static final double TIME_DELTA_1 = 0.02;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_1 = TIME_DELTA_1; // s
    private static final int PARTICLES_QUANTITY_1 = 100;
    private static final String FILENAME_1 = "output-p1" ;

    // Parameters 2
    private static final double MIN_RADIUS_2 = 0.10; // m
    private static final double MAX_RADIUS_2 = 0.37; // m
    private static final double MAX_VELOCITY_2 = 0.95; // m/s
    private static final double RADIUS_2 = 4;  // m
    private static final double LENGTH_2 = RADIUS_2 * 2 * Math.PI;  // m
    private static final double WIDTH_2 = 5 * (MAX_RADIUS_2 + MIN_RADIUS_2);  // m
    private static final double BETA_2 = 0.9;
    private static final double TAU_2 = 0.5; // s
    private static final double TIME_DELTA_2 = 0.02;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_2 = TIME_DELTA_2; // s
    private static final int PARTICLES_QUANTITY_2 = 100;
    private static final String FILENAME_2 = "output-p2" ;

    private static final double SIMULATION_TIME = 30; // s

    public static void runSimulation(String fileName, int particlesQuantity, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double length, double width, double beta, double tau, boolean writeBoundaryParticles, boolean writeWalls, Long seed) {
        Random random;
        if (seed == null) {
            random = new Random();
            seed = random.nextLong();
            random.setSeed(seed);
        } else {
            random = new Random(seed);
        }
        System.out.println("Using seed: " + seed);
        System.out.println("Creating particles...");
        long startTime = System.currentTimeMillis();
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(particlesQuantity, random, length, width, maxRadius, minRadius, maxVelocity, beta);
        System.out.println("Particles created in: " + (System.currentTimeMillis() - startTime) + "ms");
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator(fileName, length, width, writeBoundaryParticles, writeWalls);
        TimeCutCondition timeCutCondition = new TimeCutCondition(simulationTime);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(timeDelta, saveTimeDelta, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), length, width, minRadius, maxRadius, maxVelocity, beta, tau);
        System.out.println("Starting simulation...");
        startTime = System.currentTimeMillis();
        timeStepSimulator.simulate();
        System.out.println("Simulation finished in: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static void runSimulationParameters1() {
        runSimulation(FILENAME_1,
                PARTICLES_QUANTITY_1,
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
                true,
                null);
    }

    public static void runSimulationParameters2() {
        runSimulation(FILENAME_2,
                PARTICLES_QUANTITY_2,
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
                true,
                null);
    }

    public static void runSimulationsForFundamentalDiagram(String parameters, int maxParticles, int particlesJump, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double widthMultiplier, double beta, double tau) {
        double length = 4 * 2 * Math.PI;  // m
        double width = widthMultiplier * (maxRadius + minRadius);  // m
        System.out.println("Starting simulations for Fundamental Diagram with width: " + widthMultiplier);
        for(int particlesQuantity = particlesJump; particlesQuantity <= maxParticles; particlesQuantity += particlesJump) {
            System.out.println("Simulating " + particlesQuantity + " particles.");
            runSimulation(parameters + "-width-" + (int)widthMultiplier + "-particles-" + particlesQuantity,
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
                    false,
                    null);
        }
    }

    public static void runFundamentalDiagrams() {
        runSimulationsForFundamentalDiagram("p1", 200, 5, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 3, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 250, 5, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 4, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 400, 10, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 5, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 460, 10, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 6, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 500, 20, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 7, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 560, 20, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 8, BETA_1, TAU_1);
    }
}
