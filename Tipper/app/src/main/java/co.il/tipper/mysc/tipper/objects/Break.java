package co.il.tipper.mysc.tipper.objects;

/**
 * Created by Mysc on 27.5.2016.
 */
public class Break {
    long startTime;
    long endTime;

    public Break(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
