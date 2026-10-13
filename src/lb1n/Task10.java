package lb1n;

public class Task10 {

    interface OnlineCourse {
        default String getInfo() {
            return "Онлайн-курс";
        }
    }

    interface PaidCourse {
        default String getInfo() {
            return "Платный курс";
        }
    }
    static class JavaCourse implements OnlineCourse, PaidCourse {
        @Override
        public String getInfo() {
            return OnlineCourse.super.getInfo() + ", " + PaidCourse.super.getInfo();
        }
    }

    public static void main(String[] args) {
        OnlineCourse course = new JavaCourse();
        System.out.println(course.getInfo());
    }
}