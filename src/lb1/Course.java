package lb1;

public class Course {
    private long id;
    private String title;
    private int durationHours;
    private int completedHours;

    Course(long id, String title, int durationHours, int completedHours) {
        if (id > 0) this.id = id;
        else System.out.println("ERR");
        
        if (!title.isBlank() && !title.isEmpty()) this.title = title.trim();

        if (durationHours >= 1 && durationHours <= 100) this.durationHours = durationHours;
        
        if (completedHours < durationHours && completedHours >= 0) this.completedHours = completedHours;
    }

    public Course(long id, String title, int durationHours) {
        if (id > 0) this.id = id;
        else System.out.println("ERR");
        
        if (!title.isBlank() && !title.isEmpty()) this.title = title.trim();

        if (durationHours >= 1 && durationHours <= 300) this.durationHours = durationHours;
        
        this.completedHours = 0;
    }

    public long id() {
        return this.id;
    }
    public String title() {
        return this.title;
    }
    public int durationHours() {
        return this.durationHours;
    }
    public int completedHours() {
        return this.completedHours;
    }
    public int remainingHours() {
        return durationHours - completedHours;
    }
    public boolean isCompleted() {
        return remainingHours() == 0;
    }
    public void completeHours(int hours) {
        if (remainingHours() < hours) {
            System.out.println("ERR");
        } else {
            this.completedHours += hours;
        }
    }

    public static void main(String[] args) {
        Course course = new Course(10, "Java Fundamentals", 32); // 0
        course.completeHours(6);

  
        CourseFormatter formatter;
        formatter = new CompactCourseFormatter();
        System.out.println(formatter.format(course));
        System.out.println();
        formatter = new DetailedCourseFormatter();
        System.out.println(formatter.format(course));
        NotificationSender sender1 = new EmailSender();
        NotificationSender sender2 = new ConsoleSender();
        System.out.println("Override");
        sender1.send("Привет");
        sender2.send("Привет");
        System.out.println();
        System.out.println("Overload + Override");
        sender1.send("Email message");
        sender1.send(new Object());
        sender2.send("Console message");
        CourseFormatter[] formatters = { 
            new CompactCourseFormatter(),
            new DetailedCourseFormatter(),
            new StatusCourseFormatter() 
            };
        for (CourseFormatter f : formatters)
        { System.out.println(f.format(course));
            System.out.println(); 
            }   
              AbstractNotificationSender email =
                new EmailNotificationSender();

        AbstractNotificationSender console =
                new ConsoleNotificationSender();

        email.send("user@mail.com", "привет!");
        console.send("admin", "Новый курс создан");    



      
        System.out.println("Вариант 1");
        Notification sender12 = new MetricsNotificationSender( new LoggingNotificationSender( new Email() ) );
        sender12.send("user@mail.com", "привет");
        System.out.println(); 

        System.out.println("Вариант 2"); 
        Notification sender13 = new LoggingNotificationSender( new MetricsNotificationSender( new Email() ) );
        sender13.send("user@mail.com", "привет");
        CourseReader readOnlyRepository = new ReadOnlyCourseRepository();
         readOnlyRepository.find(10);
          CourseRepository repository = new CourseRepository();
           repository.find(10); 
           repository.save(null);
           EnrollmentService ageService =
        new EnrollmentService(
                new AgeEnrollmentPolicy(18)
        );

EnrollmentService prerequisiteService =
        new EnrollmentService(
                new PrerequisitesEnrollmentPolicy(10)
        );

EnrollmentService capacityService =
        new EnrollmentService(
                new CapacityEnrollmentPolicy(20, 30)
        );

        Student student = new Student("Ваня", 20);
 

ageService.enroll(student, course);
prerequisiteService.enroll(student, course);
capacityService.enroll(student, course);
 User user = new User();

        user.log();

           Point point = new Point(10, 20);

        ColoredPoint coloredPoint =
                new ColoredPoint(10, 20, "RED");

        System.out.println("Strict class equality:");
        System.out.println(point.equals(coloredPoint));
        System.out.println(coloredPoint.equals(point));

        System.out.println();

        ComposedColoredPoint composed =
                new ComposedColoredPoint(point, "RED");

        ComposedColoredPoint composed2 =
                new ComposedColoredPoint(
                        new Point(10, 20),
                        "RED"
                );

        System.out.println("Композиция:");
        System.out.println(composed.equals(composed2));
     
     NotificationService emailService =
                new NotificationService(
                        new EmailNotificationSenders()
                );

        NotificationService smsService =
                new NotificationService(
                        new SmsNotificationSender()
                );

        NotificationService consoleService =
                new NotificationService(
                        new Console()
                );

        emailService.notify(
                "user@mail.com",
                "Курс начат"
        );

        smsService.notify(
                "+79990000000",
                 "Курс начат"
        );

        consoleService.notify(
                "admin",
                 "Курс начат"
        );
    }
}
