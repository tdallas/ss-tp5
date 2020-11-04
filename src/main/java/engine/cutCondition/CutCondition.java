package engine.cutCondition;

import engine.Particle;

import java.util.List;

public interface CutCondition {
    boolean isFinished(List<Particle> particles, double time);
}
