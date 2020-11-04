package systems.oscillator;

import engine.Vector;
import engine.*;
import engine.cutCondition.CutCondition;
import engine.cutCondition.TimeCutCondition;
import engine.integrators.BeemanIntegrator;
import engine.integrators.EulerIntegrator;
import engine.integrators.GearIntegrator;
import engine.integrators.Integrator;

import java.util.*;

public class OscillatorSystem {
    private static final double OSCILLATOR_POSITION = 1;
    private static final double OSCILLATOR_VELOCITY = 0;
    private static final double OSCILLATOR_MASS = 70;
    private static final double OSCILLATOR_SPRING_CONSTANT = Math.pow(10, 4);
    private static final double OSCILLATOR_VISCOSITY = 100;
    private static final double OSCILLATOR_AMPLITUDE = 1;
    private static final double OSCILLATOR_CUTOFF_TIME = 5;

    private static final double TIME_DELTA = 0.0001;
    private static final double SAVE_TIME_DELTA = 0.01;

    public static void runOscillators() {
        //Solucion analitica
        Particle oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        ForcesCalculator oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        Integrator oscillatorIntegrator = new OscillatorAnalyticalSolutionIntegrator(oscillatorForcesCalculator, OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY, OSCILLATOR_AMPLITUDE);
        FileGenerator oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-analytic");
        CutCondition oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        TimeStepSimulator oscillatorSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, Collections.singletonList(oscillatorParticle));
        oscillatorSimulator.simulate(true);

        //Integrador EULER MODIFICADO
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        oscillatorIntegrator = new EulerIntegrator(oscillatorForcesCalculator);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-euler");
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, Collections.singletonList(oscillatorParticle));
        oscillatorSimulator.simulate(true);

        //Integrador BEEMAN
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        List<Particle> particles = Collections.singletonList(oscillatorParticle);
        oscillatorIntegrator = new BeemanIntegrator(oscillatorForcesCalculator, TIME_DELTA, particles, true);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-beeman");
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, particles);
        oscillatorSimulator.simulate(true);

        //Integrador GEAR PREDICTOR CORRECTOR
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        particles = Collections.singletonList(oscillatorParticle);
        oscillatorIntegrator = new GearIntegrator(calculatePreviousPredictions(oscillatorParticle, oscillatorForcesCalculator), oscillatorForcesCalculator, particles, true);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-gear");
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(TIME_DELTA, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, particles);
        oscillatorSimulator.simulate(true);
    }

    public static void runOscillators(double timeDelta) {
        //Solucion analitica
        Particle oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        ForcesCalculator oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        Integrator oscillatorIntegrator = new OscillatorAnalyticalSolutionIntegrator(oscillatorForcesCalculator, OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY, OSCILLATOR_AMPLITUDE);
        FileGenerator oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-analytic-" + String.format(Locale.US, "%.4f", timeDelta));
        CutCondition oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        TimeStepSimulator oscillatorSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, Collections.singletonList(oscillatorParticle));
        oscillatorSimulator.simulate(true);

        //Integrador EULER MODIFICADO
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        oscillatorIntegrator = new EulerIntegrator(oscillatorForcesCalculator);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-euler-" + String.format(Locale.US, "%.4f", timeDelta));
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, Collections.singletonList(oscillatorParticle));
        oscillatorSimulator.simulate(true);

        //Integrador BEEMAN
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        List<Particle> particles = Collections.singletonList(oscillatorParticle);
        oscillatorIntegrator = new BeemanIntegrator(oscillatorForcesCalculator, timeDelta, particles, true);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-beeman-" + String.format(Locale.US, "%.4f", timeDelta));
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, particles);
        oscillatorSimulator.simulate(true);

        //Integrador GEAR PREDICTOR CORRECTOR
        oscillatorParticle = new Particle(0, new Vector(OSCILLATOR_POSITION, 0), new Vector(OSCILLATOR_VELOCITY, 0), OSCILLATOR_MASS, 0, 0, 0, 0, 0, true);
        oscillatorForcesCalculator = new OscillatorForcesCalculator(OSCILLATOR_SPRING_CONSTANT, OSCILLATOR_VISCOSITY);
        particles = Collections.singletonList(oscillatorParticle);
        oscillatorIntegrator = new GearIntegrator(calculatePreviousPredictions(oscillatorParticle, oscillatorForcesCalculator), oscillatorForcesCalculator, particles, true);
        oscillatorFileGenerator = new OscillatorFileGenerator("oscillator-gear-" + String.format(Locale.US, "%.4f", timeDelta));
        oscillatorCutCondition = new TimeCutCondition(OSCILLATOR_CUTOFF_TIME);
        oscillatorSimulator = new TimeStepSimulator(timeDelta, SAVE_TIME_DELTA, oscillatorCutCondition, oscillatorIntegrator, oscillatorFileGenerator, particles);
        oscillatorSimulator.simulate(true);
    }

    private static Map<Particle, Vector[]> calculatePreviousPredictions(Particle oscillatorParticle, ForcesCalculator forces) {
        Map<Particle, Vector[]> previousPredictions = new HashMap<>();
        Vector[] previousPrediction = new Vector[6];
        previousPrediction[0] = oscillatorParticle.getPosition();
        previousPrediction[1] = oscillatorParticle.getVelocity();
        Vector km = forces.getForces(oscillatorParticle, oscillatorParticle.getPosition(), oscillatorParticle.getVelocity(), Collections.singletonList(oscillatorParticle)).divide(oscillatorParticle.getMass());
        previousPrediction[2] = km.changeSign().multiply(previousPrediction[0]);
        previousPrediction[3] = km.changeSign().multiply(previousPrediction[1]);
        previousPrediction[4] = km.multiply(km).multiply(previousPrediction[0]);
        previousPrediction[5] = km.multiply(km).multiply(previousPrediction[1]);
        previousPredictions.put(oscillatorParticle, previousPrediction);
        return previousPredictions;
    }
}
