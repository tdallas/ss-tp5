package systems.planets;

import engine.*;
import engine.cutCondition.CutCondition;
import engine.cutCondition.TimeCutCondition;
import engine.integrators.BeemanIntegrator;
import engine.integrators.EulerIntegrator;
import engine.integrators.Integrator;

import java.util.List;

public class MarsSystem {
    private static final double SPACESHIP_MASS = 5 * Math.pow(10, 5);                    // kg
    private static final double SPACESHIP_RADIUS = 50;                                   // m
    private static final double SPACESHIP_HEIGHT_FROM_EARTH = 1500 * Math.pow(10, 3);    // m
    private static final double SPACESHIP_ORBITAL_VELOCITY = 7.12 * Math.pow(10, 3);     // m/s
    private static final double SPACESHIP_LAUNCH_VELOCITY_0 = 8 * Math.pow(10, 3);       // m/s
    private static final double SPACESHIP_ANIMATION_RADIUS = SPACESHIP_RADIUS * 100000000;

    private static final double TIME_DELTA = 60;                                         // 1 minuto en segundos
    private static final double SAVE_TIME_DELTA = 86400;                                 // 1 dia en segundos

    public static void runSimulationBestDaySearch() {
        //Busqueda de mejor dia y hora
        int bestDayToSendSpaceshipV0 = searchBestDayToSendSpaceship(750, SPACESHIP_LAUNCH_VELOCITY_0, "v0");
        System.out.println("Best day around: " + bestDayToSendSpaceshipV0);
        int bestHourToSendSpaceshipV0 = searchBestHourToSendSpaceship(709, SPACESHIP_LAUNCH_VELOCITY_0, "v0");
        System.out.println("Best hour around: " + bestHourToSendSpaceshipV0);
        int bestMinuteToSendSpaceshipV0 = searchBestMinuteToSendSpaceship(709, 12, SPACESHIP_LAUNCH_VELOCITY_0, "v0");
        System.out.println("Best minute around: " + bestMinuteToSendSpaceshipV0);
    }

    public static void runSimulation() {
        //Mejor dia es 709 a las 12 horas y 47 minutos 6/9/2022
        simulateSpaceShipToMars(709, 12, 47, SPACESHIP_LAUNCH_VELOCITY_0, "0-spaceship-to-mars", 0);
        simulateSpaceShipToMars(709, 12, 47, SPACESHIP_LAUNCH_VELOCITY_0, "0-spaceship-to-mars-after-days-50", 50);
    }

    private static int searchBestDayToSendSpaceship(int days, double launchVelocity, String velocityString) {
        int day;
        double bestDistance = Double.MAX_VALUE;
        int bestDay = 0;
        for (day = 0; day < days + 1; day++) {
            System.out.println("Day " + day);
            List<Particle> particles = simulateSpaceShipToMars(day, 0, 0, launchVelocity, "spaceship-" + day + "-day-" + velocityString, 0);
            Particle mars = particles.get(2);
            Particle spaceship = particles.get(3);
            double distance = mars.getPosition().distance(spaceship.getPosition());
            if (bestDistance > distance) {
                bestDistance = distance;
                bestDay = day;
            }
        }
        return bestDay;
    }

    private static int searchBestHourToSendSpaceship(int day, double launchVelocity, String velocityString) {
        int hour;
        double bestDistance = Double.MAX_VALUE;
        int bestHour = 0;
        for (hour = 0; hour < 24; hour++) {
            System.out.println("Hour " + hour);
            List<Particle> particles = simulateSpaceShipToMars(day, hour, 0, launchVelocity, "spaceship-" + day + "-day-" + hour + "-hour-" + velocityString, 0);
            Particle mars = particles.get(2);
            Particle spaceship = particles.get(3);
            double distance = mars.getPosition().distance(spaceship.getPosition());
            if (bestDistance > distance) {
                bestDistance = distance;
                bestHour = hour;
            }
        }
        return bestHour;
    }

    private static int searchBestMinuteToSendSpaceship(int day, int hour, double launchVelocity, String velocityString) {
        int minute;
        double bestDistance = Double.MAX_VALUE;
        int bestMinute = 0;
        for (minute = 0; minute < 60; minute++) {
            System.out.println("Minute " + minute);
            List<Particle> particles = simulateSpaceShipToMars(day, hour, minute, launchVelocity, "spaceship-" + day + "-day-" + hour + "-hour-" + minute + "-minute-" + velocityString, 0);
            Particle mars = particles.get(2);
            Particle spaceship = particles.get(3);
            double distance = mars.getPosition().distance(spaceship.getPosition());
            if (bestDistance > distance) {
                bestDistance = distance;
                bestMinute = minute;
            }
        }
        return bestMinute;
    }

    private static List<Particle> simulateSpaceShipToMars(int day, int hour, int minute, double launchVelocity, String filename, int daysAfterMarsOrbit) {
        PlanetSystemGenerator planetSystemGenerator = new PlanetSystemGenerator(false);
        List<Particle> particles = planetSystemGenerator.getParticles();
        //uso la misma lista

        ForcesCalculator planetForcesCalculator = new PlanetForcesCalculator(planetSystemGenerator.getGravitationalConstant());
        Integrator planetIntegrator = new BeemanIntegrator(planetForcesCalculator, TIME_DELTA, particles, false);
        FileGenerator planetFileGenerator = new PlanetFileGenerator(filename);
        //dias hasta lanzar nave espacial
        CutCondition planetCutCondition = new TimeCutCondition(86400 * day + 3600 * hour + 60 * minute);
        TimeStepSimulator planetSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, particles);
        planetSimulator.simulate(false);
        double time = planetSimulator.getTime();

        //agrego nave y simulamos hasta condicion de nave espacial
        planetCutCondition = new SpaceshipMarsCutCondition(planetFileGenerator);
        addSpaceship(particles, launchVelocity);
        planetIntegrator = new BeemanIntegrator(planetForcesCalculator, TIME_DELTA, particles, false);
        planetSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, particles);
        planetSimulator.setTime(time);
        if (daysAfterMarsOrbit < 1) {
            planetSimulator.simulate(true);
        } else {
            planetSimulator.simulate(false);
            time = planetSimulator.getTime();
            planetCutCondition = new TimeCutCondition(86400 * daysAfterMarsOrbit + time);
            planetIntegrator = new BeemanIntegrator(planetForcesCalculator, TIME_DELTA, particles, false);
            planetSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, particles);
            planetSimulator.setTime(time);
            planetSimulator.simulate(true);
        }
        return particles;
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

    public static void simulatePlanets(double timeDelta) {
        //Integrador EULER MODIFICADO
        PlanetSystemGenerator planetSystemGenerator = new PlanetSystemGenerator(false);
        ForcesCalculator planetForcesCalculator = new PlanetForcesCalculator(planetSystemGenerator.getGravitationalConstant());
        Integrator planetIntegrator = new EulerIntegrator(planetForcesCalculator);
        FileGenerator planetFileGenerator = new PlanetFileGenerator("planet-euler-" + timeDelta);
        CutCondition planetCutCondition = new TimeCutCondition(86400 * 365);
        TimeStepSimulator planetSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, planetSystemGenerator.getParticles());
        planetSimulator.simulate(true);

        //Integrador BEEMAN
        planetSystemGenerator = new PlanetSystemGenerator(false);
        planetForcesCalculator = new PlanetForcesCalculator(planetSystemGenerator.getGravitationalConstant());
        planetIntegrator = new BeemanIntegrator(planetForcesCalculator, TIME_DELTA, planetSystemGenerator.getParticles(), false);
        planetFileGenerator = new PlanetFileGenerator("planet-beeman-" + timeDelta);
        planetCutCondition = new TimeCutCondition(86400 * 365);
        planetSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, planetCutCondition, planetIntegrator, planetFileGenerator, planetSystemGenerator.getParticles());
        planetSimulator.simulate(true);
    }
}
