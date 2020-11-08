package engine;

import engine.cutCondition.CutCondition;

import java.util.List;

public class TimeStepSimulator {
    private final double timeDelta;
    private final double saveTimeDelta;
    private final CutCondition cutCondition;
    private final FileGenerator fileGenerator;
    private final List<Particle> particles;
    private final double innerRadius;
    private final double outerRadius;
    private final double minRadius;
    private final double maxRadius;
    private final double maxVelocity;
    private final double beta;
    private final double tau;
    private double time;
    private double timeToSave;

    public TimeStepSimulator(double timeDelta, double saveTimeDelta, CutCondition cutCondition, FileGenerator fileGenerator, List<Particle> particles, double innerRadius, double outerRadius, double minRadius, double maxRadius, double maxVelocity, double beta, double tau) {
        this.timeDelta = timeDelta;
        this.saveTimeDelta = saveTimeDelta;
        this.cutCondition = cutCondition;
        this.fileGenerator = fileGenerator;
        this.particles = particles;
        this.timeToSave = saveTimeDelta;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
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
            checkCollisionsAndEscapeVelocities(particles);
            changeRadiusAndMoveParticles(particles);
            time += timeDelta;
            if (time >= timeToSave) {
                fileGenerator.addToFile(particles, time);
                timeToSave += saveTimeDelta;
            }
        }
        fileGenerator.closeFile();
    }

    private void checkCollisionsAndEscapeVelocities(List<Particle> particles) {
        for (Particle p1 : particles) {
            p1.setOverlapped(false);
            Vector escapeVelocity = new Vector(0, 0);
            boolean overlappedParticle = false;

            for (Particle p2 : particles) {
                // Other particles
                if (!p1.equals(p2)) {
                    if (p1.getPosition().distance(p2.getPosition()) <= p1.getRadius() + p2.getRadius()) {
                        // Check if it's overlapping another particle
                        escapeVelocity = escapeVelocity.add(p2.getPosition().perpendicularVector(p1.getPosition()));
                        overlappedParticle = true;
                    }
                }
            }

            if (Math.hypot(p1.getPosition().getX(), p1.getPosition().getY()) >= (outerRadius - p1.getRadius())) {
                // Check if it's overlapping outer wall
                double particlePositionAngle = Math.atan2(p1.getPosition().getY(), p1.getPosition().getX());
                double outerWallXPosition = Math.abs(outerRadius * Math.cos(particlePositionAngle));
                double outerWallYPosition = Math.abs(outerRadius * Math.sin(particlePositionAngle));
                outerWallXPosition = Math.signum(p1.getPosition().getX()) * outerWallXPosition;
                outerWallYPosition = Math.signum(p1.getPosition().getY()) * outerWallYPosition;
                Vector wallPosition = new Vector(outerWallXPosition, outerWallYPosition);
                double xEscape = Math.abs(wallPosition.perpendicularVector(p1.getPosition()).getX());
                double yEscape = Math.abs(wallPosition.perpendicularVector(p1.getPosition()).getY());
                if (p1.getPosition().getX() >= 0 && p1.getPosition().getY() >= 0) {
                    escapeVelocity = escapeVelocity.add(new Vector(-xEscape, -yEscape));
                } else if (p1.getPosition().getX() < 0 && p1.getPosition().getY() >= 0) {
                    escapeVelocity = escapeVelocity.add(new Vector(xEscape, -yEscape));
                } else if (p1.getPosition().getX() >= 0 && p1.getPosition().getY() < 0) {
                    escapeVelocity = escapeVelocity.add(new Vector(-xEscape, yEscape));
                } else {
                    escapeVelocity = escapeVelocity.add(new Vector(xEscape, yEscape));
                }
                overlappedParticle = true;
            } else if (Math.hypot(p1.getPosition().getX(), p1.getPosition().getY()) <= (innerRadius + p1.getRadius())) {
                // Check if it's overlapping inner wall
                double particlePositionAngle = Math.atan2(p1.getPosition().getY(), p1.getPosition().getX());
                double innerWallXPosition = Math.abs(innerRadius * Math.cos(particlePositionAngle));
                double innerWallYPosition = Math.abs(innerRadius * Math.sin(particlePositionAngle));
                innerWallXPosition = Math.signum(p1.getPosition().getX()) * innerWallXPosition;
                innerWallYPosition = Math.signum(p1.getPosition().getY()) * innerWallYPosition;
                Vector wallPosition = new Vector(innerWallXPosition, innerWallYPosition);
                escapeVelocity = escapeVelocity.add(wallPosition.perpendicularVector(p1.getPosition()));
                overlappedParticle = true;
            }

            if (overlappedParticle) {
                escapeVelocity = escapeVelocity.normalize().multiply(maxVelocity);
                p1.setVelocity(escapeVelocity);
                p1.setOverlapped(true);
            }
        }
    }

    private void changeRadiusAndMoveParticles(List<Particle> particles) {
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
                double velocityAngle = Math.atan2(particle.getPosition().getX(), particle.getPosition().getY()) - Math.PI / 2;
                double velocityModule = maxVelocity * Math.pow((particle.getRadius() - minRadius) / (maxRadius - minRadius), beta);
                double xVelocity = velocityModule * Math.sin(velocityAngle);
                double yVelocity = velocityModule * Math.cos(velocityAngle);
                particle.setVelocity(new Vector(xVelocity, yVelocity));
            }

            // Set position with new velocity
            Vector newPosition = particle.getPosition().add(particle.getVelocity().multiply(timeDelta));

            particle.setPosition(newPosition);
        }
    }
}
