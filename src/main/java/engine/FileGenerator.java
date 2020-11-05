package engine;

import java.util.List;

public interface FileGenerator {
    void addToFile(List<Particle> particles, List<Particle> boundaryParticles, double time);

    void closeFile();
}
