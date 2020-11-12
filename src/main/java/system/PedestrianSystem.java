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
    private static final String FILENAME_1 = "output-p1-circle";

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
    private static final String FILENAME_2 = "output-p2-circle";

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
        runSimulation(parameters + "-width-" + (outerRadius - innerRadius) + "-particles-3",
                3,
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
        for (int particlesQuantity = particlesJump; particlesQuantity <= maxParticles; particlesQuantity += particlesJump) {
            System.out.println("Simulating " + particlesQuantity + " particles.");
            runSimulation(parameters + "-width-" + (outerRadius - innerRadius) + "-particles-" + particlesQuantity,
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
        runSimulationsForFundamentalDiagram("p1-circle", 105, 5, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 3, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1-circle", 182, 7, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 3.5, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1-circle", 260, 13, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 4, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1-circle", 480, 24, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 5, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1-circle", 700, 35, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 6, BETA_1, TAU_1);
        runSimulationsForFundamentalDiagram("p1-circle", 1000, 50, TIME_DELTA_1, SAVE_TIME_DELTA_1, SIMULATION_TIME, MIN_RADIUS_1, MAX_RADIUS_1, MAX_VELOCITY_1, 2, 7, BETA_1, TAU_1);

        runSimulationsForFundamentalDiagram("p2-circle", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
    }

    public static void runErrorMinimization() {
        System.out.println("Simulating " + 0.27 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-27", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.10, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.28 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-28", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.09, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.29 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-29", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.08, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.30 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-30", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.07, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.31 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-31", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.06, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.32 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-32", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.05, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.33 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-33", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.04, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.34 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-34", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.03, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.35 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-35", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.02, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.36 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-36", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 - 0.01, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.37 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-37", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.38 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-38", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 + 0.01, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.39 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-39", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 + 0.02, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.40 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-40", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 + 0.03, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.41 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-41", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 + 0.04, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
        System.out.println("Simulating " + 0.42 + " max radius.");
        runSimulationsForFundamentalDiagram("p2-circle-rmax-42", 480, 24, TIME_DELTA_2, SAVE_TIME_DELTA_2, SIMULATION_TIME, MIN_RADIUS_2, MAX_RADIUS_2 + 0.05, MAX_VELOCITY_2, INNER_RADIUS_2, OUTER_RADIUS_2, BETA_2, TAU_2);
    }
}
