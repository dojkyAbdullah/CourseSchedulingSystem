import java.util.Map;

public class Room {
    private int roomId;
    private String roomName;
    private int capacity;

    private Map<Day, Boolean[]> availabilityMap;

    public Room(int roomId, String roomName, int capacity, Map<Day, Boolean[]> availabilityMap) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;

        this.availabilityMap = availabilityMap;
    }

    public int getRoomId() {

        return roomId;
    }

    public void setRoomId(int roomId) {

        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    // Check room availability
    public boolean checkAvailability(int timeSlot, Day day) {
        Boolean[] timeSlots = availabilityMap.get(day); // Get the time slots for the given day
        return timeSlots != null && timeSlots[timeSlot - 1]; // Check if the time slot is available
    }

    // Update room availability
    public void updateAvailability(int timeSlot, Day day, boolean status) {
        if (availabilityMap.containsKey(day)) {
            Boolean[] timeSlots = availabilityMap.get(day);
            if (timeSlot >= 1 && timeSlot <= timeSlots.length) {
                timeSlots[timeSlot - 1] = status;
            }
        }
    }


}


