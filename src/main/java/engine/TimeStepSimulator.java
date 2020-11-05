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
    private final double hallLength;
    private final double hallWidth;
    private final double minRadius;
    private final double maxRadius;
    private final double maxVelocity;
    private final double beta;
    private final double tau;

    public TimeStepSimulator(double timeDelta, double saveTimeDelta, CutCondition cutCondition, FileGenerator fileGenerator, List<Particle> particles, double hallLength, double hallWidth, double minRadius, double maxRadius, double maxVelocity, double beta, double tau) {
        this.timeDelta = timeDelta;
        this.saveTimeDelta = saveTimeDelta;
        this.cutCondition = cutCondition;
        this.fileGenerator = fileGenerator;
        this.particles = particles;
        this.timeToSave = saveTimeDelta;
        this.hallLength = hallLength;
        this.hallWidth = hallWidth;
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
            findContactsAndCalculateVe(particles);
            adjustAllRadius(particles);
            computeVd(particles);
            calculateNewPositions(particles);
            clearParticles(particles);
            time += timeDelta;
            if (time >= timeToSave) {
                fileGenerator.addToFile(particles, time);
                timeToSave += saveTimeDelta;
            }
        }
        fileGenerator.closeFile();
    }

    private void findContactsAndCalculateVe(List<Particle> particles) {
        for (Particle p1 : particles) {
            Vector escapeVector = new Vector(0, 0);
            boolean overlapping = false;

            // Equation number 6
            for (Particle p2 : particles) {
                if (!p1.equals(p2)) {
                    if(p1.getPosition().distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                        escapeVector = escapeVector.add(p2.getPosition().calculatePerpendicularUnitVector(p1.getPosition()));
                        overlapping = true;
                    }
                    else if(p1.getPosition().getX() >= hallLength - maxRadius){
                        Vector auxPosition = new Vector(p1.getPosition().getX() - hallLength, p1.getPosition().getY());
                        if(auxPosition.distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                            escapeVector = escapeVector.add(p2.getPosition().calculatePerpendicularUnitVector(auxPosition));
                            overlapping = true;
                        }
                    }
                    else if(p1.getPosition().getX() <= maxRadius){
                        Vector auxPosition = new Vector(p1.getPosition().getX() + hallLength, p1.getPosition().getY());
                        if(auxPosition.distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                            escapeVector = escapeVector.add(p2.getPosition().calculatePerpendicularUnitVector(auxPosition));
                            overlapping = true;
                        }
                    }
                }
            }

            // Upper wall
            if (p1.getPosition().getY() + p1.getRadius() >= hallWidth) {
                Vector upperWallVirtualPosition = new Vector(p1.getPosition().getX(), hallWidth);
                Vector wallEscapeVector1 = upperWallVirtualPosition.calculatePerpendicularUnitVector(p1.getPosition());

                Vector wallEscapeVector = new Vector(wallEscapeVector1.getX(), -Math.abs(wallEscapeVector1.getY()));

                escapeVector = escapeVector.add(wallEscapeVector);
                overlapping = true;
            }

            // Bottom wall
            if (p1.getPosition().getY() + p1.getRadius() <= 0) {
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

    private void adjustAllRadius(List<Particle> particles) {
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
                double xVelocity = maxVelocity * Math.pow((particle.getRadius() - minRadius) / (maxRadius - minRadius), beta);
                particle.setVelocity(new Vector(xVelocity, 0));
            }
        }
    }

    private void calculateNewPositions(List<Particle> particles) {
        for (Particle particle : particles) {
            // x(t + dt) = x(t) + v(t) * dt
            Vector newPosition = particle.getPosition().add(particle.getVelocity().multiply(timeDelta));

            if (newPosition.getX() >= hallLength) {
                newPosition = new Vector(newPosition.getX() - hallLength, newPosition.getY());
            }

            if (newPosition.getX() <= 0) {
                newPosition = new Vector(newPosition.getX() + hallLength, newPosition.getY());
            }

            particle.setPosition(newPosition);
        }
    }

    private void clearParticles(List<Particle> particles) {
        for (Particle particle : particles) {
            particle.setOverlapped(false);
        }
    }
}
