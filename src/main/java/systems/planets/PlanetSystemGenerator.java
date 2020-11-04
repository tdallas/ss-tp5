package systems.planets;

import engine.Particle;
import engine.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlanetSystemGenerator {
    private static final double GRAVITATIONAL_CONSTANT = 6.67408 * Math.pow(10, -11);    // m^3 kg^-1 s^-2

    private static final double SUN_MASS = 1988500 * Math.pow(10, 24);                   // kg
    private static final double SUN_RADIUS = 696000 * Math.pow(10, 3);                   // m
    private static final double SUN_X_POSITION = 0;                                      // m
    private static final double SUN_Y_POSITION = 0;                                      // m
    private static final double SUN_X_VELOCITY = 0;                                      // m/s
    private static final double SUN_Y_VELOCITY = 0;                                      // m/s
    private static final double SUN_ANIMATION_RADIUS = SUN_RADIUS * 50;

    private static final double EARTH_MASS = 5.97219 * Math.pow(10, 24);                 // kg
    private static final double EARTH_RADIUS = 6378.137 * Math.pow(10, 3);               // m
    private static final double EARTH_X_POSITION = 1.493188929636662 * Math.pow(10, 11); // m
    private static final double EARTH_Y_POSITION = 1.318936357931255 * Math.pow(10, 10); // m
    private static final double EARTH_X_VELOCITY = -3.113279917782445 * Math.pow(10, 3); // m/s
    private static final double EARTH_Y_VELOCITY = 2.955205189256462 * Math.pow(10, 4);  // m/s
    private static final double EARTH_ANIMATION_RADIUS = EARTH_RADIUS * 2000;

    private static final double MARS_MASS = 6.4171 * Math.pow(10, 23);                   // kg
    private static final double MARS_RADIUS = 3389.92 * Math.pow(10, 3);                 // m
    private static final double MARS_X_POSITION = 2.059448551842169 * Math.pow(10, 11);  // m
    private static final double MARS_Y_POSITION = 4.023977946528339 * Math.pow(10, 10);  // m
    private static final double MARS_X_VELOCITY = -3.717406842095575 * Math.pow(10, 3);  // m/s
    private static final double MARS_Y_VELOCITY = 2.584914078301731 * Math.pow(10, 4);   // m/s
    private static final double MARS_ANIMATION_RADIUS = MARS_RADIUS * 2000;

    private static final double JUPITER_MASS = 1898.13 * Math.pow(10, 23);                   // kg
    private static final double JUPITER_RADIUS = 71492 * Math.pow(10, 3);                 // m
    private static final double JUPITER_X_POSITION = 3.658822741863045 * Math.pow(10, 11);  // m
    private static final double JUPITER_Y_POSITION = -6.743682483255942 * Math.pow(10, 11);  // m
    private static final double JUPITER_X_VELOCITY = 1.133947439887606 * Math.pow(10, 4);  // m/s
    private static final double JUPITER_Y_VELOCITY = 6.854346745596702 * Math.pow(10, 3);   // m/s
    private static final double JUPITER_ANIMATION_RADIUS = JUPITER_RADIUS * 300;

    private final List<Particle> particles;

    public PlanetSystemGenerator(boolean withJupiter) {
        this.particles = new ArrayList<>();
        particles.add(new Particle(0,
                new Vector(SUN_X_POSITION, SUN_Y_POSITION),
                new Vector(SUN_X_VELOCITY, SUN_Y_VELOCITY),
                SUN_MASS, SUN_RADIUS,
                1, 1, 0,
                SUN_ANIMATION_RADIUS,
                false));
        particles.add(new Particle(1,
                new Vector(EARTH_X_POSITION, EARTH_Y_POSITION),
                new Vector(EARTH_X_VELOCITY, EARTH_Y_VELOCITY),
                EARTH_MASS, EARTH_RADIUS,
                0, 1, 0,
                EARTH_ANIMATION_RADIUS,
                true));
        particles.add(new Particle(2,
                new Vector(MARS_X_POSITION, MARS_Y_POSITION),
                new Vector(MARS_X_VELOCITY, MARS_Y_VELOCITY),
                MARS_MASS, MARS_RADIUS,
                1, 0, 0,
                MARS_ANIMATION_RADIUS,
                true));
        if (withJupiter) {
            particles.add(new Particle(3,
                    new Vector(JUPITER_X_POSITION, JUPITER_Y_POSITION),
                    new Vector(JUPITER_X_VELOCITY, JUPITER_Y_VELOCITY),
                    JUPITER_MASS, JUPITER_RADIUS,
                    1, 0, 1,
                    JUPITER_ANIMATION_RADIUS,
                    true));
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getGravitationalConstant() {
        return GRAVITATIONAL_CONSTANT;
    }
}
