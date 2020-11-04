package engine.cutCondition;

import engine.Particle;

import java.util.List;

public class TimeCutCondition implements CutCondition {
    private final double timeToCut;

    public TimeCutCondition(double timeToCut) {
        this.timeToCut = timeToCut;
    }

    @Override
    public boolean isFinished(List<Particle> particles, double time) {
        return time >= timeToCut;
    }
}
