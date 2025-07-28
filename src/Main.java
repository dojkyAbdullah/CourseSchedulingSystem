import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Faculty> permanentFaculties = new ArrayList<>();
        List<Faculty> visitingFaculties = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        Map<Integer, Room> roomMap = new HashMap<>();
        List<Course> courses = new ArrayList<>();
        List<CourseAllocation> courseAllocations = new ArrayList<>();

        String url = "jdbc:ucanaccess://TimeTableDataBase.accdb";
        String facultyQuery = "SELECT * FROM [Faculty Table]";
        String availabilityQuery = "SELECT Day, Slot FROM [FacultyAvailability Table] WHERE FacultyID = ?";
        String courseQuery = "SELECT CourseName, CourseCode, Semester, Program, Strength FROM [Courses Table] WHERE FacultyID = ?";

        try (Connection connection = DriverManager.getConnection(url);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(facultyQuery)) {

            System.out.println("Connected to the database.");

            while (rs.next()) {
                String facultyID = rs.getString("FacultyID");
                String facultyName = rs.getString("FacultyName");
                boolean isPermanent = rs.getString("IsPermanent").equalsIgnoreCase("yes");

                System.out.print(facultyID + " " + facultyName + " " + (isPermanent ? "Permanent" : "Visiting"));

                // Fetch courses assigned to this faculty
                List<Course> facultyCourses = new ArrayList<>();
                try (PreparedStatement courseStmt = connection.prepareStatement(courseQuery)) {
                    courseStmt.setString(1, facultyID);
                    try (ResultSet courseRS = courseStmt.executeQuery()) {
                        while (courseRS.next()) {
                            String courseName = courseRS.getString("CourseName");
                            String courseCode = courseRS.getString("CourseCode");
                            int semester = courseRS.getInt("Semester");
                            String program = courseRS.getString("Program");
                            int strength = courseRS.getInt("Strength");

                            Course course = new Course(courseName, courseCode);
                            CourseAllocation courseAllocation = new CourseAllocation(course, null, semester, program, 2, new ArrayList<>());
                            facultyCourses.add(course);
                            courses.add(course);
                            courseAllocations.add(courseAllocation);
                        }
                    }
                }

                Faculty faculty;
                if (isPermanent) {
                    faculty = new PermanentFaculty(facultyName, facultyID, facultyCourses);
                    permanentFaculties.add(faculty);
                } else {
                    // Visiting Faculty Availability Map
                    Map<Day, Boolean[]> VF = new HashMap<>();
                    for (Day d : Day.values()) {
                        VF.put(d, new Boolean[]{false, false, false, false, false});
                    }

                    try (PreparedStatement pstmt = connection.prepareStatement(availabilityQuery)) {
                        pstmt.setString(1, facultyID);
                        try (ResultSet availabilityRS = pstmt.executeQuery()) {
                            while (availabilityRS.next()) {
                                Day day = Day.valueOf(availabilityRS.getString("Day").toUpperCase());
                                int slot = availabilityRS.getInt("Slot");
                                if (slot >= 1 && slot <= 5) {
                                    VF.get(day)[slot - 1] = true;
                                }
                            }
                        }
                    }

                    faculty = new VisitingFaculty(facultyName, facultyID, VF, facultyCourses);
                    visitingFaculties.add(faculty);
                }

                // Assign faculty to each course
                for (CourseAllocation courseAllocation : courseAllocations) {
                    if (facultyCourses.contains(courseAllocation.getCourse())) {
                        courseAllocation.setFaculty(faculty);
                    }
                }

                System.out.println(" - Courses: " + facultyCourses);
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        // Print Permanent Faculties
        System.out.println("\nPrinting Permanent Faculties:");
        for (Faculty f : permanentFaculties) {
            System.out.println(f.getName() + " " + f.getId());
        }

        System.out.println("\nPrinting Visiting Faculties:");
        for (Faculty f : visitingFaculties) {
            System.out.println(f.getName() + " " + f.getId());
        }



        // Putting room objects into the map with id as the key
        String query = "SELECT * FROM [Rooms Table]";
        try (Connection connection = DriverManager.getConnection(url);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            System.out.println("Connected to the database.");
            while (rs.next()) {
                Map<Day, Boolean[]> r = new HashMap<>();
                Boolean[] array5 = {true, true, true, true, true};
                r.put(Day.MONDAY, array5.clone());
                r.put(Day.TUESDAY, array5.clone());
                r.put(Day.WEDNESDAY, array5.clone());
                r.put(Day.THURSDAY, array5.clone());
                r.put(Day.FRIDAY, array5.clone());
                Room room = new Room(rs.getInt("RoomID"), rs.getString("RoomName"), rs.getInt("Capacity"), r);
                roomMap.put(rs.getInt("RoomID"), room);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        System.out.println("Printing all rooms");
        for (Room r : rooms) {
            System.out.print(r.getRoomId() + " " + r.getRoomName() + " " + r.getCapacity());
            System.out.println();
        }

        List<Student> students = new ArrayList<>();

        String studentQuery = "SELECT * FROM [Students Table]";
        String registrationQuery = "SELECT c.CourseName, c.CourseCode FROM [StudentRegistrations Table] sr " +
                "JOIN [Courses Table] c ON sr.CourseID = c.CourseID " +
                "WHERE sr.StudentID = ?";

        try (Connection connection = DriverManager.getConnection(url);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(studentQuery)) {

            System.out.println("Fetching student data...");

            while (rs.next()) {
                // Get the numeric StudentID (matches the one in StudentRegistrations table)
                int studentID = rs.getInt("StudentID");
                String rollNumber = rs.getString("RollNumber");
                String studentName = rs.getString("StudentName");
                int semester = rs.getInt("Semester");
                String program = rs.getString("Program");

                // Initialize availability slots (all true for students)
                Map<Day, Boolean[]> studentSlots = new HashMap<>();
                for (Day d : Day.values()) {
                    Boolean[] slots = new Boolean[5];
                    Arrays.fill(slots, true);  // All slots available initially
                    studentSlots.put(d, slots);
                }

                // Fetch registered courses for this student
                List<Course> registeredCourses = new ArrayList<>();
                try (PreparedStatement courseStmt = connection.prepareStatement(registrationQuery)) {
                    courseStmt.setInt(1, studentID);  // Use setInt instead of setString since StudentID is numeric
                    try (ResultSet courseRS = courseStmt.executeQuery()) {
                        while (courseRS.next()) {
                            String courseName = courseRS.getString("CourseName");
                            String courseCode = courseRS.getString("CourseCode");
                            Course course = new Course(courseName, courseCode);
                            registeredCourses.add(course);
                        }
                    }
                }

                // Create student object
                Student student = new Student(rollNumber, studentName, semester, program, registeredCourses, studentSlots);
                students.add(student);

                // Update CourseAllocation objects with registered students
                for (Course course : registeredCourses) {
                    for (CourseAllocation ca : courseAllocations) {
                        if (ca.getCourse().getCourseCode().equals(course.getCourseCode())) {
                            ca.getRegisteredStudents().add(student);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student data: " + e.getMessage());
            e.printStackTrace();
        }




        PriorityQueue<CourseAllocation> priorities = new PriorityQueue<>(
                Comparator.comparingInt(courseAllocation -> courseAllocation.getFaculty().getTotalSlots())
        );

        priorities.addAll(courseAllocations); // Assuming `courses` is now a list of CourseAllocations

        CourseAllocation[][][] schedule = new CourseAllocation[5][5][8];
        List<Room> sortedRooms = new ArrayList<>(roomMap.values());
        sortedRooms.sort((room1, room2) -> Integer.compare(room1.getCapacity(), room2.getCapacity())); // Sort rooms from smallest to largest

        Queue<CourseAllocation> unassignedCourses = new LinkedList<>();

        while (!priorities.isEmpty()) {
            CourseAllocation courseAllocation = priorities.poll();
            int timeslotsNeeded = courseAllocation.getTimeSlotsToBeAllotted();
            int slotsAssigned = 0;

            while (slotsAssigned < timeslotsNeeded) {
                boolean slotFound = false;

                // Sort rooms by capacity before checking availability
                outerLoop:
                for (int i = 0; i < schedule.length ; i++) { // Loop over Days
                    for (int j = 0; j < schedule[i].length ; j++) { // Loop over Time Slots
                        for (int roomIndex = 0; roomIndex < sortedRooms.size(); roomIndex++) { // Iterate over sorted rooms
                            Room room = sortedRooms.get(roomIndex); // Get room with smallest capacity first
                            Day day = Day.values()[i]; // Get the current day

                            // Check if students are available
                            boolean studentsAvailable = true;
                            for (Student student : courseAllocation.getRegisteredStudents()) {
                                if (!student.isAvailable(j + 1, day)) {
                                    studentsAvailable = false;
                                    break;
                                }
                            }

                            // Check if faculty and room are available
                            boolean facultyAvailable = courseAllocation.getFaculty().checkAvailability(j + 1, day);
                            boolean roomAvailable = room.checkAvailability(j + 1, day);
                            boolean roomHasCapacity = room.getCapacity() >= courseAllocation.getRegisteredStudents().size();

                            // Only mark a slot as possible if all constraints are met
                            boolean isPossibleSlot = studentsAvailable && facultyAvailable && roomAvailable && roomHasCapacity;
                            courseAllocation.updatePossibilities(i, j, roomIndex, isPossibleSlot);

                            if (isPossibleSlot && schedule[i][j][roomIndex] == null) {
                                if (slotsAssigned<timeslotsNeeded){
                                // Assign course to this slot
                                schedule[i][j][roomIndex] = courseAllocation;
                                slotsAssigned++;
                                courseAllocation.setSlotsAllotted();
                                slotFound = true;

                                // Update availability for faculty, students, and room
                                courseAllocation.getFaculty().updateAvailability(j + 1, day, false);
                                for (Student student : courseAllocation.getRegisteredStudents()) {
                                    student.updateAvailability(j + 1, day, false);
                                }
                                room.updateAvailability(j + 1, day, false);

                                // Add course allocation schedule
                                courseAllocation.addSchedule(new Schedule(j + 1, day));

                             /*   if (slotsAssigned == timeslotsNeeded) {
                                    break outerLoop; // Exit outer loop when done
                                }*/
                            }
                                else{
                                    courseAllocation.updatePossibilities(i, j, roomIndex, isPossibleSlot);
                                }
                            }
                        }
                    }
                }

                // If no slots were found, exit to prevent infinite loops
                if (!slotFound) {
                    System.out.println("Warning: Could not allocate all slots for course " + courseAllocation.getName());
                    unassignedCourses.add(courseAllocation);
                    break;
                }
            }
        }

        System.out.println("----------------------------------------------------------------------------------------");
        for (int i = 0; i < schedule.length; i++) {
            for (int j = 0; j < schedule[i].length; j++) {
                for (int k = 0; k < schedule[i][j].length; k++) {
                    if (schedule[i][j][k] != null) {
                        System.out.println("Day: " + Day.values()[i] +
                                ", Slot: " + (j + 1) +
                                ", Room: " + sortedRooms.get(k).getRoomId() +  // Use sorted room list
                                " (Capacity: " + sortedRooms.get(k).getCapacity() + ")" +
                                ", Course: " + schedule[i][j][k].getName());
                    } else {
                        System.out.println("Index is null ");
                    }
                }
            }
        }



        // Find null indexes in the schedule
        boolean[][][] nullIndexes = new boolean[5][5][8];
        for (int i = 0; i < schedule.length; i++) {
            for (int j = 0; j < schedule[i].length; j++) {
                for (int k = 0; k < schedule[i][j].length; k++) {

                  if(schedule[i][j][k] == null){
                      nullIndexes[i][j][k]= true;
                  }
                }
            }
        }

        // Swap logic to ensure none of the courses remain unscheduled
        while (!unassignedCourses.isEmpty()) {
            CourseAllocation unassignedCourse = unassignedCourses.poll();
            int slotsNeeded = unassignedCourse.getTimeSlotsToBeAllotted() - unassignedCourse.getSlotsAllotted();

            for (int o = 0; o < slotsNeeded; o++) {
                boolean slotAssigned = false;
                for (int i = 0; i < schedule.length && !slotAssigned; i++) {
                    for (int j = 0; j < schedule[i].length && !slotAssigned; j++) {
                        for (int k = 0; k < schedule[i][j].length && !slotAssigned; k++) {
                            CourseAllocation currentCourse = schedule[i][j][k];

                            // Only check if the slot was marked possible before (no need to recheck students)
                            if (unassignedCourse.getPossibilities()[i][j][k]) {
                                for (int l = 0; l < nullIndexes.length && !slotAssigned; l++) {
                                    for (int m = 0; m < nullIndexes[l].length && !slotAssigned; m++) {
                                        for (int n = 0; n < nullIndexes[l][m].length; n++) {
                                            if (nullIndexes[l][m][n]) { // Found an empty slot
                                                if (currentCourse != null && currentCourse.getPossibilities()[l][m][n]) {
                                                    // Swap the courses
                                                    schedule[i][j][k] = unassignedCourse;
                                                    schedule[l][m][n] = currentCourse;
                                                    unassignedCourse.setSlotsAllotted();
                                                    nullIndexes[l][m][n] = false;
                                                    slotAssigned = true;

                                                    // Update availability for the swapped courses
                                                    Day day = Day.values()[i];
                                                    unassignedCourse.getFaculty().updateAvailability(j + 1, day, false);
                                                    for (Student student : unassignedCourse.getRegisteredStudents()) {
                                                        student.updateAvailability(j + 1, day, false);
                                                    }
                                                    sortedRooms.get(k).updateAvailability(j + 1, day, false);

                                                    // Make the current course available for the slot it is leaving
                                                    currentCourse.getFaculty().updateAvailability(j + 1, day, true);
                                                    for (Student student : currentCourse.getRegisteredStudents()) {
                                                        student.updateAvailability(j + 1, day, true);
                                                    }
                                                    sortedRooms.get(k).updateAvailability(j + 1, day, true);

                                                    // Update availability for the new slot of the current course
                                                    Day newDay = Day.values()[l];
                                                    currentCourse.getFaculty().updateAvailability(m + 1, newDay, false);
                                                    for (Student student : currentCourse.getRegisteredStudents()) {
                                                        student.updateAvailability(m + 1, newDay, false);
                                                    }
                                                    sortedRooms.get(n).updateAvailability(m + 1, newDay, false);

                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("----------------------------------------------------------------------------------------");
        // Print the schedule
        for (int i = 0; i < schedule.length; i++) {
            for (int j = 0; j < schedule[i].length; j++) {
                for (int k = 0; k < schedule[i][j].length; k++) {
                    if (schedule[i][j][k] != null) {
                        System.out.println("Day: " + Day.values()[i] +
                                ", Slot: " + (j + 1) +
                                ", Room: " + sortedRooms.get(k).getRoomId() +  // Use sorted room list
                                " (Capacity: " + sortedRooms.get(k).getCapacity() + ")" +
                                ", Course: " + schedule[i][j][k].getName());
                    } else {
                        System.out.println("Index is null ");
                    }
                }
            }
        }

    }}

