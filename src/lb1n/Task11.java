package lb1n;

import java.util.Objects;

public class Task11 {

    static class BrokenPoint {
        protected final int x;
        protected final int y;
        public BrokenPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BrokenPoint)) {
                return false;
            }
            BrokenPoint other = (BrokenPoint) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class BrokenColoredPoint extends BrokenPoint {
        private final String color;

        public BrokenColoredPoint(int x, int y, String color) {
            super(x, y);
            this.color = color;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BrokenColoredPoint)) {
                return false;
            }

            BrokenColoredPoint other = (BrokenColoredPoint) obj;

            return super.equals(other)
                    && color.equals(other.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), color);
        }
    }

    static class StrictPoint {
        protected final int x;
        protected final int y;

        public StrictPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            StrictPoint other = (StrictPoint) obj;

            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class StrictColoredPoint extends StrictPoint {
        private final String color;

        public StrictColoredPoint(int x, int y, String color) {
            super(x, y);
            this.color = color;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            StrictColoredPoint other = (StrictColoredPoint) obj;

            return super.equals(other)
                    && color.equals(other.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), color);
        }
    }


    static final class PointValue {
        private final int x;
        private final int y;

        public PointValue(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof PointValue)) {
                return false;
            }

            PointValue other = (PointValue) obj;

            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    static final class ColoredPointValue {
        private final PointValue point;
        private final String color;

        public ColoredPointValue(int x, int y, String color) {
            this.point = new PointValue(x, y);
            this.color = Objects.requireNonNull(color);
        }

        public PointValue getPoint() {
            return point;
        }

        public String getColor() {
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

            ColoredPointValue other = (ColoredPointValue) obj;

            return point.equals(other.point)
                    && color.equals(other.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point, color);
        }

        @Override
        public String toString() {
            return point + ", цвет: " + color;
        }
    }

    public static void main(String[] args) {

        System.out.println("1. Нарушение симметричности");

        BrokenPoint point = new BrokenPoint(1, 2);
        BrokenColoredPoint coloredPoint = new BrokenColoredPoint(1, 2, "Красный");
        System.out.println("point.equals(coloredPoint): " + point.equals(coloredPoint));
        System.out.println("coloredPoint.equals(point): " + coloredPoint.equals(point));
        System.out.println("2. Строгая проверка класса");
        StrictPoint strictPoint = new StrictPoint(1, 2);
        StrictColoredPoint strictColoredPoint = new StrictColoredPoint(1, 2, "Красный");
        System.out.println("point.equals(coloredPoint): " + strictPoint.equals(strictColoredPoint));
        System.out.println("coloredPoint.equals(point): " + strictColoredPoint.equals(strictPoint));
        StrictColoredPoint anotherStrictColoredPoint = new StrictColoredPoint(1, 2, "Красный");
        System.out.println("Два одинаковых цветных объекта равны: " + strictColoredPoint.equals(anotherStrictColoredPoint));
        System.out.println("3. Композиция цвета");
        PointValue simplePoint = new PointValue(1, 2);
        ColoredPointValue compositePoint = new ColoredPointValue(1, 2, "Красный");
        System.out.println("point.equals(coloredPoint): " + simplePoint.equals(compositePoint));
        System.out.println("coloredPoint.equals(point): " + compositePoint.equals(simplePoint));
        ColoredPointValue anotherCompositePoint = new ColoredPointValue(1, 2, "Красный");
        System.out.println("Два одинаковых цветных объекта равны: " + compositePoint.equals(anotherCompositePoint));
        System.out.println("Цветная точка: " + compositePoint);
    }
}