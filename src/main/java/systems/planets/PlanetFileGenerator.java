package systems.planets;

import engine.FileGenerator;
import engine.Particle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PlanetFileGenerator implements FileGenerator {
    private static final String folder = "out/";

    private final BufferedWriter bw;
    private FileWriter fw;

    public PlanetFileGenerator(String filename) {
        try {
            File directory = new File(folder);
            if (!directory.exists()) {
                directory.mkdir();
            }
            FileWriter pw = new FileWriter(folder + filename + ".xyz");
            pw.close();
            this.fw = new FileWriter(folder + filename + ".xyz", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bw = new BufferedWriter(fw);
    }

    public void addToFile(List<Particle> particles, double timePassed) {
        try {
            bw.write(particles.size() + "\n");
            bw.write("id xPosition yPosition xVelocity yVelocity radius mass animationRadius redColor greenColor blueColor timePassed\n");
            for (Particle particle : particles) {
                bw.write(particle.getId() + " " +
                        particle.getPosition().getX() + " " +
                        particle.getPosition().getY() + " " +
                        particle.getVelocity().getX() + " " +
                        particle.getVelocity().getY() + " " +
                        particle.getRadius() + " " +
                        particle.getMass() + " " +
                        particle.getAnimationRadius() + " " +
                        particle.getRedColor() + " " +
                        particle.getGreenColor() + " " +
                        particle.getBlueColor() + " " +
                        timePassed + "\n");
            }
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
