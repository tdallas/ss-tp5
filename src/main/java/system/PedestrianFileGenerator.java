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
    private static final double WALLS_RADIUS = 0.01;

    private final BufferedWriter bw;
    private FileWriter fw;

    public PedestrianFileGenerator(String filename, double innerRadius, double outerRadius, boolean writeWalls) {
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
        if (writeWalls) {
            writeWall(filename, innerRadius, outerRadius);
        }
    }

    public void addToFile(List<Particle> particles, double timePassed) {
        try {
            bw.write(particles.size() + "\n");
            bw.write("id xPosition yPosition xVelocity yVelocity radius overlapped timePassed\n");
            for (Particle particle : particles) {
                bw.write(particle.getId() + " " +
                        particle.getPosition().getX() + " " +
                        particle.getPosition().getY() + " " +
                        particle.getVelocity().getX() + " " +
                        particle.getVelocity().getY() + " " +
                        particle.getRadius() + " " +
                        particle.isOverlapped() + " " +
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

    private void writeWall(String filename, double innerRadius, double outerRadius) {
        int n = 0;
        try {
            FileWriter pw = new FileWriter("out/walls-" + filename + ".xyz");
            pw.close();
            pw = new FileWriter("out/walls-" + filename + ".xyz", true);
            BufferedWriter bw = new BufferedWriter(pw);

            bw.write("xPosition yPosition radius\n");
            double xPosition, yPosition;
            for (double angle = 0.0; angle < 360.0; angle += 0.1) {
                xPosition = innerRadius * Math.sin(Math.toRadians(angle));
                yPosition = innerRadius * Math.cos(Math.toRadians(angle));
                bw.write(xPosition + " " + yPosition + " " + WALLS_RADIUS + "\n");
                n++;
            }
            for (double angle = 0; angle < 360; angle += 0.1) {
                xPosition = outerRadius * Math.sin(Math.toRadians(angle));
                yPosition = outerRadius * Math.cos(Math.toRadians(angle));
                bw.write(xPosition + " " + yPosition + " " + WALLS_RADIUS + "\n");
                n++;
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
