package systems.planets;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;

import java.util.List;

public class PlanetForcesCalculator implements ForcesCalculator {
    private final double gravitationalConstant;

    public PlanetForcesCalculator(double gravitationalConstant) {
        this.gravitationalConstant = gravitationalConstant;
    }

    @Override
    public Vector getForces(Particle particle, Vector position, Vector velocity, List<Particle> particles) {
        Vector forces = new Vector(0, 0);

        for (Particle p : particles) {
            if (!particle.equals(p)) {
                double distance = position.distance(p.getPosition());
                double gravitationalForce = gravitationalConstant * particle.getMass() * p.getMass() / (distance * distance);
                forces = forces.add(p.getPosition().subtract(position).divide(distance).multiply(gravitationalForce));
            }
        }

        return forces;
    }
}
