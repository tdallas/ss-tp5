package engine;

import engine.cutCondition.CutCondition;

import java.util.LinkedList;
import java.util.List;

public class TimeStepSimulator {
    private final double timeDelta;
    private final double saveTimeDelta;
    private final CutCondition cutCondition;
    private final FileGenerator fileGenerator;
    private final List<Particle> particles;
    private final List<Particle> boundaryParticles;
    private final double hallLength;
    private final double hallWidth;
    private final double minRadius;
    private final double maxRadius;
    private final double maxVelocity;
    private final double beta;
    private final double tau;
    private double time;
    private double timeToSave;

    public TimeStepSimulator(double timeDelta, double saveTimeDelta, CutCondition cutCondition, FileGenerator fileGenerator, List<Particle> particles, double hallLength, double hallWidth, double minRadius, double maxRadius, double maxVelocity, double beta, double tau) {
        this.timeDelta = timeDelta;
        this.saveTimeDelta = saveTimeDelta;
        this.cutCondition = cutCondition;
        this.fileGenerator = fileGenerator;
        this.particles = particles;
        this.boundaryParticles = new LinkedList<>();
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
        fileGenerator.addToFile(particles, boundaryParticles, time);
        while (!cutCondition.isFinished(particles, time)) {
            checkCollisionsAndEscapeVelocities(particles);
            changeRadiusAndMoveParticles(particles, boundaryParticles);
            time += timeDelta;
            if (time >= timeToSave) {
                fileGenerator.addToFile(particles, boundaryParticles, time);
                timeToSave += saveTimeDelta;
            }
        }
        fileGenerator.closeFile();
    }

    private void checkCollisionsAndEscapeVelocities(List<Particle> particles) {
        for (Particle p1 : particles) {
            Vector escapeVelocity = new Vector(0, 0);
            boolean overlappedParticle = false;

            for (Particle p2 : particles) {
                // Other particles
                if (!p1.equals(p2)) {
                    if (p1.getPosition().distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                        // Check if it's near another particle
                        escapeVelocity = escapeVelocity.add(p2.getPosition().perpendicularVector(p1.getPosition()));
                        overlappedParticle = true;
                    } else if (p1.getPosition().getX() >= hallLength - maxRadius) {
                        // Check if it's passing the right border
                        Vector auxPosition = new Vector(p1.getPosition().getX() - hallLength, p1.getPosition().getY());
                        if (auxPosition.distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                            escapeVelocity = escapeVelocity.add(p2.getPosition().perpendicularVector(auxPosition));
                            overlappedParticle = true;
                        }
                    } else if (p1.getPosition().getX() <= maxRadius) {
                        // Check if it's passing the left border
                        Vector auxPosition = new Vector(p1.getPosition().getX() + hallLength, p1.getPosition().getY());
                        if (auxPosition.distance(p2.getPosition()) < p1.getRadius() + p2.getRadius()) {
                            escapeVelocity = escapeVelocity.add(p2.getPosition().perpendicularVector(auxPosition));
                            overlappedParticle = true;
                        }
                    }
                }
            }


            if (p1.getPosition().getY() + p1.getRadius() >= hallWidth) {
                // Check if it's near upper wall
                escapeVelocity = escapeVelocity.add(new Vector(0, -1));
                overlappedParticle = true;
            } else if (p1.getPosition().getY() - p1.getRadius() <= 0) {
                // Check if it's near lower wall
                escapeVelocity = escapeVelocity.add(new Vector(0, 1));
                overlappedParticle = true;
            }

            if (overlappedParticle) {
                escapeVelocity = escapeVelocity.normalize().multiply(maxVelocity);
                p1.setVelocity(escapeVelocity);
                p1.setOverlapped(true);
            }
        }
    }

    private void changeRadiusAndMoveParticles(List<Particle> particles, List<Particle> boundaryParticles) {
        boundaryParticles.clear();
        for (Particle particle : particles) {

            // Change radius
            if (particle.isOverlapped()) {
                particle.setRadius(minRadius);
            } else if (particle.getRadius() < maxRadius) {
                double newRadius = particle.getRadius() + (minRadius / (tau / timeDelta));
                particle.setRadius(Math.min(newRadius, maxRadius));
            }

            // Change velocity if not overlapped with new radius
            if (!particle.isOverlapped()) {
                double newXVelocity = maxVelocity * Math.pow((particle.getRadius() - minRadius) / (maxRadius - minRadius), beta);
                particle.setVelocity(new Vector(newXVelocity, 0));
            }

            // Set position with new velocity
            Vector newPosition = particle.getPosition().add(particle.getVelocity().multiply(timeDelta));
            if (newPosition.getX() >= hallLength) {
                newPosition = new Vector(newPosition.getX() - hallLength, newPosition.getY());
            } else if (newPosition.getX() <= 0) {
                newPosition = new Vector(newPosition.getX() + hallLength, newPosition.getY());
            }
            // Check if needs to add copy of particle to boundaryParticles. This is only for animation that's why it's not included on the list particles
            if (newPosition.getX() + particle.getRadius() >= hallLength) {
                Particle copiedParticle = new Particle(particle.getId() + particles.size(), new Vector(newPosition.getX() - hallLength, newPosition.getY()), particle.getVelocity(), particle.getRadius(), false);
                boundaryParticles.add(copiedParticle);
            } else if (newPosition.getX() - particle.getRadius() <= 0) {
                Particle copiedParticle = new Particle(particle.getId() + particles.size(), new Vector(newPosition.getX() + hallLength, newPosition.getY()), particle.getVelocity(), particle.getRadius(), false);
                boundaryParticles.add(copiedParticle);
            }

            particle.setPosition(newPosition);
            particle.setOverlapped(false);
        }
    }
}
