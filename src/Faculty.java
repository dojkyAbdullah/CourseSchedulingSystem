import java.util.ArrayList;
import java.util.List;

public abstract class Faculty {
    private String name;
    private String id;
    private List<Course> courses;

    public Faculty(String name, String id , List<Course> courses) {
        this.name = name;
        this.id = id;
        this.courses = courses;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {

        this.courses.add(course);
    }

    public abstract int getTotalSlots();
    public abstract boolean checkAvailability(int timeSlot, Day day);

    public abstract void updateAvailability(int timeSlot, Day day, boolean status);
}
