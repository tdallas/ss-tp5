package engine.integrators;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;

import java.util.List;

public class EulerIntegrator extends Integrator {
    public EulerIntegrator(ForcesCalculator forcesCalculator) {
        super(forcesCalculator);
    }

    @Override
    public void applyIntegrator(double timeDelta, Particle particle, List<Particle> particles) {
        Vector forces = getForces(particle, particle.getPosition(), particle.getVelocity(), particles);
        particle.setVelocity(particle.getVelocity().add(forces.multiply(timeDelta / particle.getMass())));
        particle.setPosition(particle.getPosition().add(particle.getVelocity().multiply(timeDelta)).add(forces.multiply(timeDelta * timeDelta / (2 * particle.getMass()))));
    }
}
