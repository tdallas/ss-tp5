package system;

import engine.TimeStepSimulator;
import engine.cutCondition.TimeCutCondition;

import java.util.Random;

public class PedestrianSystem {
    // Parameters 1
    private static final double MIN_RADIUS_1 = 0.15; // m
    private static final double MAX_RADIUS_1 = 0.32; // m
    private static final double MAX_VELOCITY_1 = 1.55; // m/s
    private static final double INNER_RADIUS_1 = 2;  // m
    private static final double OUTER_RADIUS_1 = 5;  // m
    private static final double BETA_1 = 0.9;
    private static final double TAU_1 = 0.5; // s
    private static final double TIME_DELTA_1 = 0.05;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_1 = TIME_DELTA_1; // s
    private static final int PARTICLES_QUANTITY_1 = 100;
    private static final String FILENAME_1 = "output-p1";

    // Parameters 2
    private static final double MIN_RADIUS_2 = 0.10; // m
    private static final double MAX_RADIUS_2 = 0.37; // m
    private static final double MAX_VELOCITY_2 = 0.95; // m/s
    private static final double INNER_RADIUS_2 = 2;  // m
    private static final double OUTER_RADIUS_2 = 5;  // m
    private static final double BETA_2 = 0.9;
    private static final double TAU_2 = 0.5; // s
    private static final double TIME_DELTA_2 = 0.05;  // s        r_min/2*v_max
    private static final double SAVE_TIME_DELTA_2 = TIME_DELTA_2; // s
    private static final int PARTICLES_QUANTITY_2 = 100;
    private static final String FILENAME_2 = "output-p2";

    private static final double SIMULATION_TIME = 100; // s

    public static void runSimulation(String fileName, int particlesQuantity, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double innerRadius, double outerRadius, double beta, double tau, boolean writeBoundaryParticles, boolean writeWalls, Long seed) {
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
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(particlesQuantity, random, innerRadius, outerRadius, minRadius, maxRadius, maxVelocity, beta);
        System.out.println("Particles created in: " + (System.currentTimeMillis() - startTime) + "ms");
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator(fileName, innerRadius, outerRadius, writeWalls);
        TimeCutCondition timeCutCondition = new TimeCutCondition(simulationTime);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(timeDelta, saveTimeDelta, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), innerRadius, outerRadius, minRadius, maxRadius, maxVelocity, beta, tau);
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
                INNER_RADIUS_1,
                OUTER_RADIUS_1,
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
                INNER_RADIUS_2,
                OUTER_RADIUS_2,
                BETA_2,
                TAU_2,
                true,
                true,
                null);
    }

    public static void runSimulationsForFundamentalDiagram(String parameters, int maxParticles, int particlesJump, double timeDelta, double saveTimeDelta, double simulationTime, double minRadius, double maxRadius, double maxVelocity, double innerRadius, double outerRadius, double beta, double tau) {
        System.out.println("Starting simulations for Fundamental Diagram with width: " + (outerRadius - innerRadius));
        for (int particlesQuantity = particlesJump; particlesQuantity <= maxParticles; particlesQuantity += particlesJump) {
            System.out.println("Simulating " + particlesQuantity + " particles.");
            runSimulation(parameters + "-width-" + (int) (outerRadius - innerRadius) + "-particles-" + particlesQuantity,
                    particlesQuantity,
                    timeDelta,
                    saveTimeDelta,
                    simulationTime,
                    minRadius,
                    maxRadius,
                    maxVelocity,
                    innerRadius,
                    outerRadius,
                    beta,
                    tau,
                    false,
                    false,
                    null);
        }
    }

    public static void runFundamentalDiagramsForWidthComparison() {
        runSimulationsForFundamentalDiagram("p1", 260, 13, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 4, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 480, 24, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 5, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 700, 35, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 6, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1", 1000, 50, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 7, BETA_1, TAU_1);

        runSimulationsForFundamentalDiagram("p2", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
    }

    public static void runTest() {
        Random random = new Random();
        System.out.println("Creating particles...");
        long startTime = System.currentTimeMillis();
        PedestrianSystemGenerator pedestrianSystemGenerator = new PedestrianSystemGenerator(20, random, INNER_RADIUS_1, OUTER_RADIUS_1, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, BETA_1);
        System.out.println("Particles created in: " + (System.currentTimeMillis() - startTime) + "ms");
        PedestrianFileGenerator pedestrianFileGenerator = new PedestrianFileGenerator("test", INNER_RADIUS_1, OUTER_RADIUS_1, true);
        TimeCutCondition timeCutCondition = new TimeCutCondition(SIMULATION_TIME);
        TimeStepSimulator timeStepSimulator = new TimeStepSimulator(0.05, 0.05, timeCutCondition, pedestrianFileGenerator, pedestrianSystemGenerator.getParticles(), INNER_RADIUS_1, OUTER_RADIUS_1, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, BETA_1, TAU_1);
        System.out.println("Starting simulation...");
        startTime = System.currentTimeMillis();
        timeStepSimulator.simulate();
        System.out.println("Simulation finished in: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
