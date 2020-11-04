package engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Vector {
    private final double x;
    private final double y;

    public Vector add(Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public Vector subtract(Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y);
    }

    public Vector multiply(double number) {
        return new Vector(this.x * number, this.y * number);
    }

    public Vector multiply(Vector vector) {
        return new Vector(this.x * vector.x, this.y * vector.y);
    }

    public Vector divide(double number) {
        return new Vector(this.x / number, this.y / number);
    }

    public double distance(Vector vector) {
        return Math.hypot(this.x - vector.x, this.y - vector.y);
    }

    public Vector changeSign() {
        return new Vector(-x, -y);
    }

    public static Vector copy(Vector vector) {
        return new Vector(vector.x, vector.y);
    }
}