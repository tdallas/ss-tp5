package system;

import engine.Particle;
import engine.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedestrianSystemGenerator {
    private final double innerRadius;
    private final double outerRadius;
    private final double maxRadius;
    private final double minRadius;
    private final double maxVelocity;
    private final double beta;
    private final Random random;
    private final List<Particle> particles;
    private final int totalQuantity;
    private final double density;
    private int particlesCreated;
    private int idCounter;

    private static final double MAX_DENSITY = 9;


    public PedestrianSystemGenerator(int totalQuantity, Random random, double innerRadius, double outerRadius, double minRadius, double maxRadius, double maxVelocity, double beta) {
        this.idCounter = 0;
        this.particlesCreated = 0;
        this.totalQuantity = totalQuantity;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.maxVelocity = maxVelocity;
        this.beta = beta;
        this.random = random;
        this.density = totalQuantity / (Math.PI * ((outerRadius * outerRadius) - (innerRadius * innerRadius)));
        if (density > MAX_DENSITY) {
            throw new IllegalArgumentException("Could not generate this many particles for this area.");
        }
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
        double xPosition = 0, yPosition = 0, xVelocity, yVelocity, radius = 0, randomAngle, randomPositionRadius;
        int checkedParticles;
        boolean particleOverlaps = true;

        while (particleOverlaps) {
//            radius = generateRandomDouble(minRadius, maxRadius - (density / MAX_DENSITY) * (maxRadius - minRadius));
            radius = minRadius;
            randomAngle = generateRandomDouble(0, 360);
            randomPositionRadius = generateRandomDouble(innerRadius + radius, outerRadius - radius);
            xPosition = randomPositionRadius * Math.sin(Math.toRadians(randomAngle));
            yPosition = randomPositionRadius * Math.cos(Math.toRadians(randomAngle));
            checkedParticles = checkCorrectParticleDistribution(xPosition, yPosition, radius);
            if (checkedParticles == particles.size()) {
                particleOverlaps = false;
            }
        }
        double velocityAngle = Math.atan2(xPosition, yPosition) - Math.PI / 2;
        double velocityModule = maxVelocity * Math.pow((radius - minRadius) / (maxRadius - minRadius), beta);
        xVelocity = velocityModule * Math.sin(velocityAngle);
        yVelocity = velocityModule * Math.cos(velocityAngle);

        return new Particle(idCounter++, new Vector(xPosition, yPosition), new Vector(xVelocity, yVelocity), radius, false);
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
