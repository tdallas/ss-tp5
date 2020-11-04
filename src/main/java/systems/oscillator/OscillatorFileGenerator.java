package systems.oscillator;

import engine.FileGenerator;
import engine.Particle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OscillatorFileGenerator implements FileGenerator {
    private static final String folder = "out/";

    private BufferedWriter bw;
    private FileWriter fw;

    public OscillatorFileGenerator(String filename) {
        try {
            File directory = new File(folder);
            if (!directory.exists()) {
                directory.mkdir();
            }
            FileWriter pw = new FileWriter(folder + filename + ".csv");
            pw.close();
            this.fw = new FileWriter(folder + filename + ".csv", true);
            this.bw = new BufferedWriter(fw);
            bw.write("position,velocity,time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToFile(List<Particle> particles, double time) {
        try {
            bw.write(particles.get(0).getPosition().getX() + "," + particles.get(0).getVelocity().getX() + "," + time + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
