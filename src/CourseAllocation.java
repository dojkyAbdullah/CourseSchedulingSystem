import java.util.ArrayList;
import java.util.List;

class CourseAllocation {
    private Course course;
    private Faculty faculty;
    private int semester;
    private String program;
    private Boolean[][][] possibilities = new Boolean[5][5][8];
    private int timeSlotsToBeAllotted;
    private List<Student> registeredStudents;
    private List<Schedule> scheduleOfCourse;
    private int slotsAllotted;

    public CourseAllocation(Course course, Faculty faculty, int semester, String program,
                            int timeSlotsToBeAllotted, List<Student> registeredStudents) {
        this.course = course;
        this.faculty = faculty;
        this.semester = semester;
        this.program = program;
        this.timeSlotsToBeAllotted = timeSlotsToBeAllotted;
        this.registeredStudents = registeredStudents;
        this.scheduleOfCourse = new ArrayList<>();
        initializePossibilities();
        slotsAllotted = 0;
    }

    public int getSlotsAllotted() {
        return slotsAllotted;
    }

    public void setSlotsAllotted() {
        this.slotsAllotted++;
    }

    private void initializePossibilities() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 8; k++) {
                    possibilities[i][j][k] = false;
                }
            }
        }
    }

    public Course getCourse() {
        return course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Boolean[][][] getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(Boolean[][][] possibilities) {
        this.possibilities = possibilities;
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

    public int getTimeSlotsToBeAllotted() {
        return timeSlotsToBeAllotted;
    }

    public void setTimeSlotsToBeAllotted(int timeSlotsToBeAllotted) {
        this.timeSlotsToBeAllotted = timeSlotsToBeAllotted;
    }

    public List<Student> getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(List<Student> registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    public void addRegisteredStudent(Student registeredStudent) {
        this.registeredStudents.add(registeredStudent);
    }

    public List<Schedule> getScheduleOfCourse() {
        return scheduleOfCourse;
    }

    public void addSchedule(Schedule schedule) {
        this.scheduleOfCourse.add(schedule);
    }

    @Override
    public String toString() {
        return "CourseAllocation{" +
                "Course=" + course.getName() +
                ", Faculty=" + faculty.getName() +
                ", Semester=" + semester +
                ", Program='" + program + '\'' +
                ", Time Slots Allotted=" + timeSlotsToBeAllotted +
                ", Registered Students=" + registeredStudents.size() +
                '}';
    }

    public void setScheduleOfCourse(Schedule schedule) {
        this.scheduleOfCourse = scheduleOfCourse;
    }

    public String getName() {
        return course.getName();  // Assuming 'Course' has a getName() method
    }

    public void updatePossibilities(int dayIndex, int slotIndex, int roomIndex, boolean value) {
        possibilities[dayIndex][slotIndex][roomIndex] = value;
    }
}
