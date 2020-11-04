package systems.oscillator;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;
import engine.integrators.Integrator;

import java.util.List;

public class OscillatorAnalyticalSolutionIntegrator extends Integrator {
    private double time;
    private final double springConstant;
    private final double viscosity;
    private final double amplitude;

    public OscillatorAnalyticalSolutionIntegrator(ForcesCalculator forcesCalculator, double springConstant, double viscosity, double amplitude) {
        super(forcesCalculator);
        this.time = 0;
        this.springConstant = springConstant;
        this.viscosity = viscosity;
        this.amplitude = amplitude;
    }

    @Override
    public void applyIntegrator(double timeDelta, Particle particle, List<Particle> particles) {
        time += timeDelta;
        double position = amplitude * Math.exp(-1 * viscosity / (2 * particle.getMass()) * time) * Math.cos(Math.pow(springConstant / particle.getMass() - viscosity * viscosity / (4 * particle.getMass() * particle.getMass()), 0.5) * time);

        particle.setPosition(new Vector(position, 0));
    }
}
