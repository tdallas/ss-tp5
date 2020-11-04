package systems.planets;

import engine.FileGenerator;
import engine.Particle;
import engine.cutCondition.CutCondition;

import java.util.List;

public class SpaceshipJupiterCutCondition implements CutCondition {
    private Double lastDistance;
    private final FileGenerator planetFileGenerator;

    public SpaceshipJupiterCutCondition(FileGenerator planetFileGenerator) {
        this.lastDistance = null;
        this.planetFileGenerator = planetFileGenerator;
    }

    @Override
    public boolean isFinished(List<Particle> particles, double time) {
        Particle jupiter = particles.get(3);
        Particle spaceship = particles.get(4);
        double distance = jupiter.getPosition().distance(spaceship.getPosition());
        if (lastDistance == null) {
            lastDistance = distance;
            return false;
        }

        if (lastDistance < distance) {
            System.out.println("Best distance: " + (lastDistance - jupiter.getRadius() - spaceship.getRadius()));
            planetFileGenerator.addToFile(particles, time);
            return true;
        }
        lastDistance = distance;
        return false;
    }
}
