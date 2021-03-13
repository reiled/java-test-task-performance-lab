public class Line extends Figure {
    private final Point3D start;
    private final Point3D end;

    public Line(Point3D start, Point3D end) {
        this.start = new Point3D(start);
        this.end = new Point3D(end);
    }

    public Point3D getStart() {
        return start;
    }

    public Point3D getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
