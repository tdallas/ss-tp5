package systems.oscillator;

import engine.ForcesCalculator;
import engine.Particle;
import engine.Vector;

import java.util.List;

public class OscillatorForcesCalculator implements ForcesCalculator {
    private final double springConstant;
    private final double viscosity;

    public OscillatorForcesCalculator(double springConstant, double viscosity) {
        this.springConstant = springConstant;
        this.viscosity = viscosity;
    }

    @Override
    public Vector getForces(Particle particle, Vector position, Vector velocity, List<Particle> particles) {
        double forces = -springConstant * position.getX() - viscosity * velocity.getX();
        return new Vector(forces, 0);
    }
}
