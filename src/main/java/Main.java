import systems.oscillator.OscillatorSystem;
import systems.planets.JupiterSystem;
import systems.planets.MarsSystem;
import systems.planets.OrbitSystem;

public class Main {
    public static void main(String[] args) {
        //EJ 1.1
        OscillatorSystem.runOscillators();

        //EJ 1.2 y 1.3
        OscillatorSystem.runOscillators(0.0001);
        OscillatorSystem.runOscillators(0.0002);
        OscillatorSystem.runOscillators(0.0003);
        OscillatorSystem.runOscillators(0.0004);
        OscillatorSystem.runOscillators(0.0005);
        OscillatorSystem.runOscillators(0.0006);
        OscillatorSystem.runOscillators(0.0007);
        OscillatorSystem.runOscillators(0.0008);
        OscillatorSystem.runOscillators(0.0009);
        OscillatorSystem.runOscillators(0.0010);

        //EJ 2.1
        MarsSystem.simulatePlanets(10);
        MarsSystem.simulatePlanets(20);
        MarsSystem.simulatePlanets(30);
        MarsSystem.simulatePlanets(40);
        MarsSystem.simulatePlanets(50);
        MarsSystem.simulatePlanets(60);
        MarsSystem.simulatePlanets(70);
        MarsSystem.simulatePlanets(80);
        MarsSystem.simulatePlanets(90);
        MarsSystem.simulatePlanets(100);
        MarsSystem.simulatePlanets(200);
        MarsSystem.simulatePlanets(300);
        MarsSystem.simulatePlanets(400);
        MarsSystem.simulatePlanets(500);
        MarsSystem.simulatePlanets(600);
        MarsSystem.simulatePlanets(700);
        MarsSystem.simulatePlanets(800);
        MarsSystem.simulatePlanets(900);
        MarsSystem.simulatePlanets(1000);

        //EJ 2.2
        MarsSystem.runSimulationBestDaySearch();
        MarsSystem.runSimulation();

        //EJ 2.3
        JupiterSystem.runSimulationBestDaySearch();
        JupiterSystem.runSimulation();

        //More animations
        OrbitSystem.runSimulation();
    }
}
