package system;

import engine.Particle;
import engine.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedestrianSystemGenerator {
    private final double length;
    private final double width;
    private final double maxRadius;
    private final double minRadius;
    private final double maxVelocity;
    private final double beta;
    private final Random random;
    private final List<Particle> particles;
    private final int totalQuantity;
    private int particlesCreated;
    private int idCounter;

    public PedestrianSystemGenerator(int totalQuantity, Random random, double length, double width, double maxRadius, double minRadius, double maxVelocity, double beta) {
        this.idCounter = 0;
        this.particlesCreated = 0;
        this.totalQuantity = totalQuantity;
        this.length = length;
        this.width = width;
        this.maxRadius = maxRadius;
        this.minRadius = minRadius;
        this.maxVelocity = maxVelocity;
        this.beta = beta;
        this.random = random;
        this.particles = new ArrayList<>();
        generateRandomParticles();
    }

    private void generateRandomParticles() {
        while (particlesCreated < totalQuantity) {
            particles.add(createParticle());
            particlesCreated++;
        }
    }

    private Particle createParticle() {
        double xPosition = 0, yPosition = 0, xVelocity, radius = 0;
        int checkedParticles;
        boolean particleOverlaps = true;

        while (particleOverlaps) {
            radius = generateRandomDouble(minRadius, maxRadius);
            xPosition = generateRandomDouble(radius, length - radius);
            yPosition = generateRandomDouble(radius, width - radius);
            checkedParticles = checkCorrectParticleDistribution(xPosition, yPosition, radius);
            if (checkedParticles == particles.size()) {
                particleOverlaps = false;
            }
        }

        xVelocity = maxVelocity * Math.pow((radius - minRadius) / (maxRadius - minRadius), beta);

        return new Particle(idCounter++, new Vector(xPosition, yPosition), new Vector(xVelocity, 0), radius, false);
    }

    private double generateRandomDouble(final double min, final double max) {
        return random.nextDouble() * (max - min) + min;
    }

    private double circlesDistance(final double x1, final double y1, final double r1, final double x2, final double y2, final double r2) {
        return Math.hypot(x1 - x2, y1 - y2) - r1 - r2;
    }

    private int checkCorrectParticleDistribution(final double x, final double y, final double radius) {
        Particle curr;
        int checkedParticles = 0;

        while (checkedParticles < particles.size()) {
            curr = particles.get(checkedParticles);

            if (circlesDistance(curr.getPosition().getX(), curr.getPosition().getY(), curr.getRadius(), x, y, radius) < 0) {
                break;
            }

            checkedParticles++;
        }

        return checkedParticles;
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
