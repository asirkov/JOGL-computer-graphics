package utils;

public class Complex {
    public double x;
    public double y;

    public Complex() {
        new Complex(0, 0);
    }

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Complex(Complex o) {
        this.x = o.x;
        this.y = o.y;
    }
}
