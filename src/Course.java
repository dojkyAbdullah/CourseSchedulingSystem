//import java.util.ArrayList;
//import java.util.List;
//
//public class Course {
//
//    private String Name;
//
//    private String CourseCode;
//
//    private Faculty Faculty;
//
//    private int Duration;
//    private int Semester;
//    private String Program;
//    private int Strength;
//    private int timeSlotsToBeAllotted;
//    private List<Student> registeredStudents;
//    private List<Schedule> scheduleOfCourse = new ArrayList<>();
//
//    public Course(String name, String courseCode, Faculty faculty, int duration, int semester, String program, int strength, int timeSlotsToBeAllotted, List<Student> registeredStudents) {
//        this.Name = name;
//        this.CourseCode = courseCode;
//        this.Faculty = faculty;
//        this.Duration = duration;
//
//        this.Semester = semester;
//        this.Program = program;
//        this.Strength = strength;
//        this.timeSlotsToBeAllotted = timeSlotsToBeAllotted;
//        this.registeredStudents = registeredStudents;
//    }
//
//    // Getters and Setters
//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public String getCourseCode() {
//        return CourseCode;
//    }
//
//    public void setCourseCode(String courseCode) {
//        CourseCode = courseCode;
//    }
//
//    public Faculty getFaculty() {
//        return Faculty;
//    }
//
//    public void setFaculty(Faculty faculty) {
//        Faculty = faculty;
//    }
//
//    public int getDuration() {
//        return Duration;
//    }
//
//    public void setDuration(int duration) {
//        Duration = duration;
//    }
//
//    public int getSemester() {
//        return Semester;
//    }
//
//    public void setSemester(int semester) {
//        Semester = semester;
//    }
//
//    public String getProgram() {
//        return Program;
//    }
//
//    public void setProgram(String program) {
//        Program = program;
//    }
//
//    public int getStrength() {
//        return Strength;
//    }
//
//    public void setStrength(int strength) {
//        Strength = strength;
//    }
//
//    public int getTimeSlotsToBeAllotted() {
//        return timeSlotsToBeAllotted;
//    }
//
//    public void setTimeSlotsToBeAllotted(int timeSlotsToBeAllotted) {
//        this.timeSlotsToBeAllotted = timeSlotsToBeAllotted;
//    }
//
//    public List<Student> getRegisteredStudents() {
//        return registeredStudents;
//    }
//
//    public void setRegisteredStudents(List<Student> registeredStudents) {
//        this.registeredStudents = registeredStudents;
//    }
//
//    public List<Schedule> getScheduleOfCourse() {
//        return scheduleOfCourse;
//    }
//
//    public void addSchedule(Schedule schedule) {
//        this.scheduleOfCourse.add(schedule);
//    }
//
//    @Override
//    public String toString() {
//        return "Course{" +
//                "Name='" + Name + '\'' +
//                ", CourseCode='" + CourseCode + '\'' +
//                ", Faculty=" + Faculty.getName() +
//                ", Duration=" + Duration +
//                ", Semester=" + Semester +
//                ", Program='" + Program + '\'' +
//                ", Strength=" + Strength +
//                ", Time Slots Allotted=" + timeSlotsToBeAllotted +
//                ", Registered Students=" + registeredStudents.size() +
//                '}';
//    }
//}



public class Course {
    private String name;
    private String courseCode;

    public Course(String name, String courseCode) {
        this.name = name;
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", courseCode='" + courseCode + '\'' +
                '}';
    }
}

