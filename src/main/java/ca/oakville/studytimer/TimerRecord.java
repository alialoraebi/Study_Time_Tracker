package ca.oakville.studytimer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimerRecord {
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty totalTime;

    public TimerRecord(String startTime, String endTime, String totalTime) {
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.totalTime = new SimpleStringProperty(totalTime);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public String getTotalTime() {
        return totalTime.get();
    }

    public StringProperty totalTimeProperty() {
        return totalTime;
    }
}


