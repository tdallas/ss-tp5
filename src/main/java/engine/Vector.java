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

    public Vector normalize() {
        if (this.x == 0 && this.y == 0) {
            return new Vector(0, 0);
        }
        return this.divide(Math.hypot(this.x, this.y));
    }

    public Vector perpendicularVector(final Vector vector) {
        return vector.subtract(this).normalize();
    }
}