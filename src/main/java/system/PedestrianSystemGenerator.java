package system;

import engine.Particle;
import engine.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedestrianSystemGenerator {
    private int quantity;
    private int idCounter;
    private final double xLength;
    private final double yLength;
    private final double maxRadius;
    private final double minRadius;
    private final double maxVelocity;
    private final double beta;
    private final Random random;
    private final List<Particle> particles;

    private static final int ALLOWED_ATTEMPTS = 50;

    public PedestrianSystemGenerator(int quantity, Random random, double xLength, double yLength, double maxRadius, double minRadius, double maxVelocity, double beta) {
        this.idCounter = 0;
        this.quantity = quantity;
        this.xLength = xLength;
        this.yLength = yLength;
        this.maxRadius = maxRadius;
        this.minRadius = minRadius;
        this.maxVelocity = maxVelocity;
        this.beta = beta;
        this.random = random;
        this.particles = new ArrayList<>();
        generateRandomParticles();
    }

    private void generateRandomParticles() {
        while (quantity > 0) {
            particles.add(createParticle());
            quantity--;
        }
    }

    private Particle createParticle() {
        double randomX = 0, randomY = 0, randomAngle, xVelocity, radius = 0;
        int checkedParticles;
        int attempts = 0;
        boolean particleOverlaps = true;

        while (particleOverlaps && attempts < ALLOWED_ATTEMPTS) {
            radius = generateRandomDouble(minRadius, maxRadius);
            randomX = generateRandomDouble(radius, xLength - radius);
            randomY = generateRandomDouble(radius, yLength - radius);
            checkedParticles = checkCorrectParticleDistribution(randomX, randomY, radius);
            if (checkedParticles == particles.size()) {
                particleOverlaps = false;
            }

            attempts++;
        }

        if (particleOverlaps) {
            throw new IllegalArgumentException("Could not generate particle in less attempts than allowed.");
        }

        xVelocity = maxVelocity * Math.pow((radius - minRadius) / (maxRadius - minRadius), beta);

        return new Particle(idCounter++, new Vector(randomX, randomY), new Vector(xVelocity, 0), radius, false);
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

    public List<Particle> getParticles(){
        return particles;
    }
}
