package engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Particle {
    private final int id;
    private Vector position;
    private Vector velocity;
    private final double mass;
    private final double radius;
    private final double redColor;
    private final double greenColor;
    private final double blueColor;
    private final double animationRadius;
    private final boolean movableParticle;

    public static List<Particle> copyParticles(List<Particle> particles) {
        List<Particle> result = new ArrayList<>();
        for (Particle p : particles) {
            result.add(copy(p));
        }
        return result;
    }

    public static boolean removeParticleWithId(List<Particle> particles, int id) {
        Particle p = new Particle(id, null, null, 0, 0, 0, 0, 0, 0, false);
        return particles.remove(p);
    }

    public static Particle copy(Particle particle) {
        return new Particle(particle.id, Vector.copy(particle.position), Vector.copy(particle.velocity), particle.mass, particle.radius, particle.redColor, particle.greenColor, particle.blueColor, particle.animationRadius, particle.movableParticle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
