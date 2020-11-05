package system;

import engine.FileGenerator;
import engine.Particle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PedestrianFileGenerator implements FileGenerator {
    private static final String folder = "out/";
    private static final double WALLS_RADIUS = 0.02;

    private final BufferedWriter bw;
    private FileWriter fw;

    public PedestrianFileGenerator(String filename, double hallLength, double hallWidth) {
        try {
            File directory = new File(folder);
            if (!directory.exists()) {
                directory.mkdir();
            }
            FileWriter pw1 = new FileWriter(folder + filename + ".xyz");
            pw1.close();
            this.fw = new FileWriter(folder + filename + ".xyz", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bw = new BufferedWriter(fw);
        writeWall(filename, hallLength, hallWidth);
    }

    public void addToFile(List<Particle> particles, List<Particle> boundaryParticles, double timePassed) {
        try {
            bw.write((particles.size() + boundaryParticles.size()) + "\n");
            bw.write("id xPosition yPosition xVelocity yVelocity radius timePassed\n");
            for (Particle particle : particles) {
                bw.write(particle.getId() + " " +
                        particle.getPosition().getX() + " " +
                        particle.getPosition().getY() + " " +
                        particle.getVelocity().getX() + " " +
                        particle.getVelocity().getY() + " " +
                        particle.getRadius() + " " +
                        timePassed + "\n");
            }
            for (Particle particle : boundaryParticles) {
                bw.write(particle.getId() + " " +
                        particle.getPosition().getX() + " " +
                        particle.getPosition().getY() + " " +
                        particle.getVelocity().getX() + " " +
                        particle.getVelocity().getY() + " " +
                        particle.getRadius() + " " +
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

    private void writeWall(String filename, double hallLength, double hallWidth) {
        int n = 0;
        try {
            FileWriter pw = new FileWriter("out/walls-" + filename + ".xyz");
            pw.close();
            pw = new FileWriter("out/walls-" + filename + ".xyz", true);
            BufferedWriter bw = new BufferedWriter(pw);

            bw.write("xPosition yPosition radius\n");
            for (double x = 0; x < hallLength; x += WALLS_RADIUS) {
                bw.write(x + " " + (hallWidth) + " " + WALLS_RADIUS + "\n");
                bw.write(x + " " + (0) + " " + WALLS_RADIUS + "\n");
                n += 2;
            }
            for (double y = 0; y < hallWidth; y += WALLS_RADIUS) {
                bw.write((hallLength) + " " + y + " " + WALLS_RADIUS + "\n");
                bw.write((0) + " " + y + " " + WALLS_RADIUS + "\n");
                n += 2;
            }
            bw.close();

            Path path = Paths.get("out/walls-" + filename + ".xyz");
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.add(0, Integer.toString(n));
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
