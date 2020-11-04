package engine;

import engine.cutCondition.CutCondition;
import engine.integrators.Integrator;

import java.util.List;

public class TimeStepSimulator {
    private final double timeDelta;
    private final double saveTimeDelta;
    private final CutCondition cutCondition;
    private final Integrator integrator;
    private final FileGenerator fileGenerator;
    private final List<Particle> particles;
    private double time;
    private double timeToSave;

    public TimeStepSimulator(double timeDelta, double saveTimeDelta, CutCondition cutCondition, Integrator integrator, FileGenerator fileGenerator, List<Particle> particles) {
        this.timeDelta = timeDelta;
        this.saveTimeDelta = saveTimeDelta;
        this.cutCondition = cutCondition;
        this.integrator = integrator;
        this.fileGenerator = fileGenerator;
        this.particles = particles;
        this.timeToSave = saveTimeDelta;
        this.time = 0;
    }

    public void simulate(boolean closeFile) {
        fileGenerator.addToFile(particles, time);
        while (!cutCondition.isFinished(particles, time)) {
            for (Particle particle : particles) {
                if (particle.isMovableParticle()) {
                    integrator.applyIntegrator(timeDelta, particle, particles);
                }
            }
            time += timeDelta;
            if (time >= timeToSave) {
                fileGenerator.addToFile(particles, time);
                timeToSave += saveTimeDelta;
            }
        }
        if (closeFile) {
            fileGenerator.closeFile();
        }
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
        this.timeToSave = time + saveTimeDelta;
    }
}
