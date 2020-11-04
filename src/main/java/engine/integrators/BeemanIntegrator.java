package engine.integrators;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeemanIntegrator extends Integrator {
    private final Map<Particle, Vector> previousAccelerations;

    public BeemanIntegrator(ForcesCalculator forcesCalculator, double timeDelta, List<Particle> particles, boolean forcesDependOnVelocity) {
        super(forcesCalculator);
        previousAccelerations = new HashMap<>();
        for (Particle p : particles) {
            Vector forces = getForces(p, p.getPosition(), p.getVelocity(), particles);
            Vector previousPosition = p.getPosition().subtract(p.getVelocity().multiply(timeDelta)).add(forces.multiply((timeDelta * timeDelta) / (2 * p.getMass())));
            Vector previousVelocity = p.getVelocity().subtract(forces.multiply(timeDelta));
            Vector previousAcceleration = getForces(p, previousPosition, previousVelocity, particles).divide(p.getMass());
            previousAccelerations.put(p, previousAcceleration);
        }
    }

    public void applyIntegrator(double timeDelta, Particle particle, List<Particle> particles) {
        Vector forces = getForces(particle, particle.getPosition(), particle.getVelocity(), particles);
        Vector acceleration = forces.divide(particle.getMass());
        Vector previousAcceleration = previousAccelerations.get(particle);
        particle.setPosition(particle.getPosition().add(particle.getVelocity().multiply(timeDelta)).add(acceleration.multiply((2.0 / 3) * timeDelta * timeDelta)).subtract(previousAcceleration.multiply((1.0 / 6) * timeDelta * timeDelta)));
        //predict velocity with position
        Vector velocityPrediction = particle.getVelocity().add(acceleration.multiply((3.0 / 2) * timeDelta)).subtract(previousAcceleration.multiply((1.0 / 2) * timeDelta));
        Vector nextAcceleration = getForces(particle, particle.getPosition(), velocityPrediction, particles).divide(particle.getMass());
        //correct velocity
        particle.setVelocity(particle.getVelocity().add(nextAcceleration.multiply((1.0 / 3) * timeDelta)).add(acceleration.multiply((5.0 / 6) * timeDelta)).subtract(previousAcceleration.multiply((1.0 / 6) * timeDelta)));
        previousAccelerations.replace(particle, acceleration);
    }
}
