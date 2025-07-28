public class Schedule {
    private int slot;
    private  Day day;
    public Schedule(int slot, Day day){
        this.day=day;
        this.slot=slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
