package systems.planets;

import engine.FileGenerator;
import engine.Particle;
import engine.cutCondition.CutCondition;

import java.util.List;

public class SpaceshipMarsCutCondition implements CutCondition {
    private Double lastDistance;
    private final FileGenerator planetFileGenerator;

    public SpaceshipMarsCutCondition(FileGenerator planetFileGenerator) {
        this.lastDistance = null;
        this.planetFileGenerator = planetFileGenerator;
    }

    @Override
    public boolean isFinished(List<Particle> particles, double time) {
        Particle mars = particles.get(2);
        Particle spaceship = particles.get(3);
        double distance = mars.getPosition().distance(spaceship.getPosition());
        if (lastDistance == null) {
            lastDistance = distance;
            return false;
        }

        if (lastDistance < distance) {
            System.out.println("Best distance: " + (lastDistance - mars.getRadius() - spaceship.getRadius()));
            planetFileGenerator.addToFile(particles, time);
            return true;
        }
        lastDistance = distance;
        return false;
    }
}
