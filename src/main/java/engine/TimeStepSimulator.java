package engine;

import engine.cutCondition.CutCondition;

import java.util.List;

public class TimeStepSimulator {
    private final double timeDelta;
    private final double saveTimeDelta;
    private final CutCondition cutCondition;
    private final FileGenerator fileGenerator;
    private final List<Particle> particles;
    private double time;
    private double timeToSave;
    private final double length;
    private final double width;
    private final double minRadius;
    private final double maxRadius;
    private final double maxVelocity;
    private final double beta;
    private final double tau;

    public TimeStepSimulator(double timeDelta, double saveTimeDelta, CutCondition cutCondition, FileGenerator fileGenerator, List<Particle> particles, double length, double width, double minRadius, double maxRadius, double maxVelocity, double beta, double tau) {
        this.timeDelta = timeDelta;
        this.saveTimeDelta = saveTimeDelta;
        this.cutCondition = cutCondition;
        this.fileGenerator = fileGenerator;
        this.particles = particles;
        this.timeToSave = saveTimeDelta;
        this.length = length;
        this.width = width;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.maxVelocity = maxVelocity;
        this.beta = beta;
        this.tau = tau;
        this.time = 0;
    }

    public void simulate() {
        fileGenerator.addToFile(particles, time);
        while (!cutCondition.isFinished(particles, time)) {
            findContactsAndCalculateVe(particles, width);
            adjustAllRadius(particles, minRadius, maxRadius);
            computeVd(particles);
            calculateNewPositions(particles, length);
            clearParticles(particles);
            time += timeDelta;
            if (time >= timeToSave) {
                fileGenerator.addToFile(particles, time);
                timeToSave += saveTimeDelta;
            }
        }
        fileGenerator.closeFile();
    }

    private void findContactsAndCalculateVe(List<Particle> particles, double width) {
        for (Particle p1 : particles) {

            Vector escapeVector = new Vector(0, 0);
            boolean overlapping = false;

            // Equation number 6
            for (Particle p2 : particles) {
                if (!p1.equals(p2) && areParticlesOverlapping(p1, p2)) {
                    escapeVector = escapeVector.add(p2.getPosition().calculatePerpendicularUnitVector(p1.getPosition()));
                    overlapping = true;
                }
            }

            // Upper wall
            if (upperWallOverlapping(p1)) {
                Vector upperWallVirtualPosition = new Vector(p1.getPosition().getX(), width);
                Vector wallEscapeVector1 = upperWallVirtualPosition.calculatePerpendicularUnitVector(p1.getPosition());

                Vector wallEscapeVector = new Vector(wallEscapeVector1.getX(), -Math.abs(wallEscapeVector1.getY()));

                escapeVector = escapeVector.add(wallEscapeVector);
                overlapping = true;
            }

            // Bottom wall
            if (bottomWallOverlapping(p1)) {
                Vector bottomWallVirtualPosition = new Vector(p1.getPosition().getX(), 0);
                Vector wallEscapeVector1 = bottomWallVirtualPosition.calculatePerpendicularUnitVector(p1.getPosition());

                Vector wallEscapeVector = new Vector(wallEscapeVector1.getX(), Math.abs(wallEscapeVector1.getY()));

                escapeVector = escapeVector.add(wallEscapeVector);
                overlapping = true;
            }

            escapeVector = escapeVector.normalize().multiply(maxVelocity);

            if (overlapping) {
                p1.setVelocity(escapeVector);
                p1.setOverlapped(true);
            }
        }
    }

    private void adjustAllRadius(List<Particle> particles, double minRadius, double maxRadius) {
        for (Particle particle : particles) {
            if (particle.isOverlapped()) {
                particle.setRadius(minRadius);
            } else if (particle.getRadius() < maxRadius) {
                double newR = particle.getRadius() + (minRadius / (tau / timeDelta));
                particle.setRadius(Math.min(newR, maxRadius));
            }
        }
    }

    private void computeVd(List<Particle> particles) {
        for (Particle particle : particles) {
            if (!particle.isOverlapped()) {
                particle.setVelocity(getVelocityNoOverlap(particle));
            }
        }
    }

    private void calculateNewPositions(List<Particle> particles, double length) {
        for (Particle particle : particles) {
            // x(t + dt) = x(t) + v(t) * dt
            Vector newPosition = particle.getPosition().add(particle.getVelocity().multiply(timeDelta));

            if (newPosition.getX() >= length) {
                newPosition = new Vector(newPosition.getX() - length, newPosition.getY());
            }

            if (newPosition.getX() <= 0) {
                newPosition = new Vector(newPosition.getX() + length, newPosition.getY());
            }

            particle.setPosition(newPosition);
        }
    }

    private void clearParticles(List<Particle> particles) {
        for (Particle particle : particles) {
            particle.setOverlapped(false);
        }
    }

    private boolean areParticlesOverlapping(final Particle p1, final Particle p2) {
        return p1.getPosition().distance(p2.getPosition()) < p1.getRadius() + p2.getRadius();
    }

    private boolean upperWallOverlapping(final Particle particle) {
        return particle.getPosition().getY() + particle.getRadius() >= width;
    }

    private boolean bottomWallOverlapping(final Particle particle) {
        return particle.getPosition().getY() - particle.getRadius() <= 0;
    }

    /**
     * Returns the velocity for a particle that's not overlapping another.
     * In this case, the `y` coordinate is always `0` as the particle just moves towards the exit point
     * which is located at the right of the corridor.
     *
     * @param particle particle
     *
     * @return Velocity assuming no overlap with other particles
     */
    private Vector getVelocityNoOverlap(Particle particle) {
        double x = maxVelocity * Math.pow((particle.getRadius() - minRadius) / (maxRadius - minRadius), beta);

        return new Vector(x, 0);
    }
}
