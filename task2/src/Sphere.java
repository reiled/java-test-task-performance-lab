public class Sphere extends Figure {
    private final double radius;
    private final Point3D center;

    public Sphere(double radius, Point3D location) {
        this.radius = radius;
        this.center = new Point3D(location);
    }

    public double getRadius() {
        return radius;
    }

    public Point3D getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "radius=" + radius +
                ", center=" + center +
                '}';
    }
}
