package engine;

import java.util.List;

public interface ForcesCalculator {
    Vector getForces(Particle particle, Vector position, Vector velocity, List<Particle> particles);
}
