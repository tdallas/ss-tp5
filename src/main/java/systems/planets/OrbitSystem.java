package systems.planets;

import engine.*;
import engine.cutCondition.CutCondition;
import engine.cutCondition.TimeCutCondition;
import engine.integrators.BeemanIntegrator;
import engine.integrators.Integrator;

import java.util.List;

public class OrbitSystem {
    private static final double SPACESHIP_MASS = 5 * Math.pow(10, 5);                    // kg
    private static final double SPACESHIP_RADIUS = 50;                                   // m
    private static final double SPACESHIP_HEIGHT_FROM_EARTH = 1500 * Math.pow(10, 3);    // m
    private static final double SPACESHIP_ORBITAL_VELOCITY = 7.12 * Math.pow(10, 3);     // m/s
    private static final double SPACESHIP_ANIMATION_RADIUS = SPACESHIP_RADIUS * 10000;

    private static final double TIME_DELTA = 1;                                         // 1 minuto en segundos
    private static final double SAVE_TIME_DELTA = 1;                                 // 1 hora en segundos

    public static void runSimulation() {
        PlanetSystemGenerator planetSystemGenerator = new PlanetSystemGenerator(false);
        List<Particle> particles = planetSystemGenerator.getParticles();
        addSpaceship(particles, 0);
        Particle earth = particles.get(1);
        Particle.removeParticleWithId(particles, 1);
        particles.add(new Particle(earth.getId(), earth.getPosition(), earth.getVelocity(), earth.getMass(), earth.getRadius(), earth.getRedColor(), earth.getGreenColor(), earth.getBlueColor(), earth.getRadius(), true));
        ForcesCalculator planetForcesCalculator = new PlanetForcesCalculator(planetSystemGenerator.getGravitationalConstant());
        Integrator planetIntegrator = new BeemanIntegrator(planetForcesCalculator, TIME_DELTA, particles, false);
        FileGenerator planetFileGenerator = new PlanetFileGenerator("earth-orbit");
        CutCondition planetCutCondition = new TimeCutCondition(86400 * 1);
        TimeStepSimulator planetSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, particles);
        planetSimulator.simulate(true);

    }

    private static void addSpaceship(List<Particle> particles, double launchVelocity) {
        Particle earth = particles.get(1);

        double positionAngle = Math.atan2(earth.getPosition().getY(), earth.getPosition().getX());
        double velocityAngle = Math.atan2(earth.getPosition().getX(), earth.getPosition().getY());

        double xPosition = Math.abs((SPACESHIP_HEIGHT_FROM_EARTH + earth.getRadius()) * Math.cos(positionAngle));
        double yPosition = Math.abs((SPACESHIP_HEIGHT_FROM_EARTH + earth.getRadius()) * Math.sin(positionAngle));
        double xVelocity = Math.abs(SPACESHIP_ORBITAL_VELOCITY * Math.cos(velocityAngle))
                + Math.abs(launchVelocity * Math.cos(velocityAngle));
        double yVelocity = Math.abs(SPACESHIP_ORBITAL_VELOCITY * Math.sin(velocityAngle))
                + Math.abs(launchVelocity * Math.sin(velocityAngle));

        xPosition = earth.getPosition().getX() + Math.signum(earth.getPosition().getX()) * xPosition;
        yPosition = earth.getPosition().getY() + Math.signum(earth.getPosition().getY()) * yPosition;
        xVelocity = earth.getVelocity().getX() + Math.signum(earth.getVelocity().getX()) * xVelocity;
        yVelocity = earth.getVelocity().getY() + Math.signum(earth.getVelocity().getY()) * yVelocity;

        particles.add(new Particle(3,
                new Vector(xPosition, yPosition),
                new Vector(xVelocity, yVelocity),
                SPACESHIP_MASS, SPACESHIP_RADIUS,
                1, 1, 1,
                SPACESHIP_ANIMATION_RADIUS,
                true));
    }
}
