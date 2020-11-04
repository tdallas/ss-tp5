package engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Vector {
    private final double x;
    private final double y;

    public Vector add(final Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public Vector subtract(final Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y);
    }

    public Vector multiply(final double number) {
        return new Vector(this.x * number, this.y * number);
    }

    public Vector multiply(final Vector vector) {
        return new Vector(this.x * vector.x, this.y * vector.y);
    }

    public Vector divide(final double number) {
        return new Vector(this.x / number, this.y / number);
    }

    public double distance(final Vector vector) {
        return Math.hypot(this.x - vector.x, this.y - vector.y);
    }

    public Vector changeSign() {
        return new Vector(-x, -y);
    }

    public static Vector copy(final Vector vector) {
        return new Vector(vector.x, vector.y);
    }

    public Vector normalize() {
        if (x == 0 && y == 0) {
            return new Vector(0, 0);
        }

        return this.multiply(1 / norm());
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector calculatePerpendicularUnitVector(final Vector v) {
        return v.subtract(this).normalize();
    }
}