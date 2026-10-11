package lb1;

public class ComposedColoredPoint {

    private final Point point;
    private final String color;

    public ComposedColoredPoint(Point point, String color) {
        this.point = point;
        this.color = color;
    }

    public Point point() {
        return point;
    }

    public String color() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ComposedColoredPoint other = (ComposedColoredPoint) obj;

        return point.equals(other.point)
                && color.equals(other.color);
    }

    @Override
    public int hashCode() {
        return 31 * point.hashCode() + color.hashCode();
    }
}