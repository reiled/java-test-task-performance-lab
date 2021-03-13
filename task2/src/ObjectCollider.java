import java.util.ArrayList;
import java.util.List;

public class ObjectCollider {

    private ObjectCollider() {

    }

    public static ArrayList<Point3D> findCollisionPoints(Figure first, Figure second) {
        ArrayList<Point3D> collisionPoints = new ArrayList<>();
        if (first instanceof Line && second instanceof Sphere) {
            return findSphereAndLineIntersectionPoints((Line) first, (Sphere) second);
        }
        return collisionPoints;
    }

    private static ArrayList<Point3D> findSphereAndLineIntersectionPoints(Line line, Sphere sphere) {
        ArrayList<Point3D> collisionPoints = new ArrayList<>();

        // пересечение прямой и сферы
        // см. http://www.ambrsoft.com/TrigoCalc/Sphere/SpherLineIntersection_.htm
        double cx = sphere.getCenter().getX();
        double cy = sphere.getCenter().getY();
        double cz = sphere.getCenter().getZ();

        double r = sphere.getRadius();

        double x1 = line.getStart().getX();
        double y1 = line.getStart().getY();
        double z1 = line.getStart().getZ();

        double px = x1;
        double py = y1;
        double pz = z1;

        double x2 = line.getEnd().getX();
        double y2 = line.getEnd().getY();
        double z2 = line.getEnd().getZ();

        double vx = x2 - px;
        double vy = y2 - py;
        double vz = z2 - pz;

        double a = vx * vx + vy * vy + vz * vz;
        double b = 2.0 * (px * vx + py * vy + pz * vz - vx * cx - vy * cy - vz * cz);
        double c = px * px - 2.0 * px * cx + cx * cx + py * py - 2 * py * cy + cy * cy + pz * pz - 2 * pz * cz + cz * cz - r * r;

        // дискриминант квадратного уравнения
        double D = b * b - 4 * a * c;

        double t1 = (-b - Math.sqrt(D)) / (2.0 * a);
        Point3D firstSolution = new Point3D(x1 * (1 - t1) + t1 * x2,
                                            y1 * (1 - t1) + t1 * y2,
                                            z1 * (1 - t1) + t1 * z2);

        double t2 = (-b + Math.sqrt(D)) / (2.0 * a);
        Point3D secondSolution = new Point3D(x1 * (1 - t2) + t2 * x2,
                                             y1 * (1 - t2) + t2 * y2,
                                             z1 * (1 - t2) + t2 * z2);

        if (D < 0 || t1 > 1 || t2 > 1) {
            return collisionPoints;
        } else if (D == 0) {
            collisionPoints.add(firstSolution);
        } else {
            collisionPoints.add(firstSolution);
            collisionPoints.add(secondSolution);
        }

        return collisionPoints;
    }
}
