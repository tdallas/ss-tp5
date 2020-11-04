package engine.integrators;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;

import java.util.List;

public abstract class Integrator {
    private final ForcesCalculator forcesCalculator;

    public Integrator(ForcesCalculator forcesCalculator) {
        this.forcesCalculator = forcesCalculator;
    }

    public Vector getForces(Particle particle, Vector position, Vector velocity, List<Particle> particles) {
        return forcesCalculator.getForces(particle, position, velocity, particles);
    }

    public abstract void applyIntegrator(double timeDelta, Particle particle, List<Particle> particles);
}
