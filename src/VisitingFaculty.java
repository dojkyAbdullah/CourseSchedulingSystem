import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitingFaculty extends Faculty {
    private Map<Day, Boolean[]> availabilityMap;
    private int totalSlots;

    public VisitingFaculty(String name, String id, Map<Day, Boolean[]> availability, List<Course> courses) {

        super(name, id,courses);
        this.availabilityMap = availability;
        for (Map.Entry<Day, Boolean[]> entry : availabilityMap.entrySet()) {
            Boolean[] slots = entry.getValue();
            // Count available slots (true values)
            for (int  i =0; i<slots.length; i++) {
                if (slots[i]) {  // Avoids null pointer exceptions
                    totalSlots++;
                }
            }
        }
    }

    @Override
    public boolean checkAvailability(int timeSlot, Day day) {
        Boolean[] timeSlots = availabilityMap.get(day);
        return timeSlots != null && timeSlot >= 1 && timeSlot <= timeSlots.length && timeSlots[timeSlot - 1];
    }

    @Override
    public int getTotalSlots() {
        return totalSlots;
    }


    @Override
    public void updateAvailability(int timeSlot, Day day, boolean status) {
        if (availabilityMap.containsKey(day)) {
            Boolean[] timeSlots = availabilityMap.get(day);
            if (timeSlot >= 1 && timeSlot <= timeSlots.length) {
                timeSlots[timeSlot - 1] = status;
            } else {
                throw new IllegalArgumentException("Invalid time slot index");
            }
        } else {
            throw new IllegalArgumentException("Invalid day");
        }
    }

    public void printSchedule() {
        System.out.println("Schedule for Visiting Faculty:");
        for (Map.Entry<Day, Boolean[]> entry : availabilityMap.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            Boolean[] timeSlots = entry.getValue();
            for (int i = 0; i < timeSlots.length; i++) {
                System.out.print((timeSlots[i] ? "Available" : "Unavailable") + " ");
            }
            System.out.println();
        }
    }
}
