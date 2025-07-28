import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Student {
    // Attributes
    // Unique identifier for each student
    private String rollNumber; // Student's roll number or registration ID
    private String name; // Full name of the student
    private int semester; // Current semester of the student
    private String program; // Program/degree the student is enrolled in
    private List<Course> courses; // List of courses the student is enrolled in
    private Map<Day, Boolean[]> slot;
    // Constructor
    public Student(String rollNumber, String name, int semester, String program, List<Course> courses, Map<Day, Boolean[]> slot) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.semester = semester;
        this.program = program;
        this.courses = courses;

        this.slot = slot;
    }

    // Getters and Setters


    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Override
    public String toString() {
        StringBuilder courseList = new StringBuilder();
        for (Course course : courses) {
            courseList.append(course).append(", ");
        }
        if (courseList.length() > 0) {
            courseList.setLength(courseList.length() - 2); // Remove trailing comma and space
        }
        return ", Roll: " + rollNumber + ", Name: " + name +
                ", Program: " + program + ", Semester: " + semester +", Courses: " + courseList;
    }


/*    public boolean isAvailable(Day day, int timeSlot) {
        // Here, we assume a student is available for the given course and timeSlot
        // unless explicitly constrained. This can be extended with more logic.
        if(slot.containsKey(day)){
            Boolean[] timeSlots = slot.get(day);

        }
        for (String enrolledCourse : courses) {
            if (enrolledCourse.equalsIgnoreCase(course)) {
                return true; // Student is available for this course.
            }
        }
        return false; // Student is not available for this course.
    }*/

public boolean isAvailable(int timeSlot, Day day) {
    if (slot.containsKey(day)) {
        Boolean[] timeSlots = slot.get(day);
        return timeSlot >= 1 && timeSlot <= timeSlots.length && timeSlots[timeSlot - 1];
    }
    return false;
}
    public void updateAvailability(int timeSlot, Day day, boolean status) {
        if (slot.containsKey(day)) {
            Boolean[] timeSlots = slot.get(day);
            if (timeSlot >= 1 && timeSlot <= timeSlots.length) {
                timeSlots[timeSlot - 1] = status; // Update the availability status
            }
        }
    }

}


