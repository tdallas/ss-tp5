package engine;

import java.util.List;

public interface FileGenerator {
    void addToFile(List<Particle> particles, double time);

    void closeFile();
}
