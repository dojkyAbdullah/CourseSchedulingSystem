import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermanentFaculty extends Faculty {
    private Map<Day, Boolean[]> availabilityMap;
    private int totalSlots;

    public PermanentFaculty(String name, String id, List<Course> courses) {
        super(name, id,courses);
        this.availabilityMap = new HashMap<>();
        for (Day day : Day.values()) {
            availabilityMap.put(day, new Boolean[5]);
            Arrays.fill(availabilityMap.get(day), true); // All slots are available by default
        }
        this.totalSlots=25;
    }

    @Override
    public boolean checkAvailability(int timeSlot, Day day) {
        Boolean[] timeSlots = availabilityMap.get(day);
        return timeSlots != null && timeSlot >= 1 && timeSlot <= timeSlots.length && timeSlots[timeSlot - 1];
    }
    @Override
    public int getTotalSlots() {
        return this.totalSlots;  // Permanent faculty always has 25 slots
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
        System.out.println("Schedule for Permanent Faculty:");
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
