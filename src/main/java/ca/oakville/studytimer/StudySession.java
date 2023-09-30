package ca.oakville.studytimer;

public class StudySession {
    private String startTime;
    private String endTime;
    private String totalTime;

    public StudySession(String startTime, String endTime, String totalTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
    }

    // Implement getters and setters as needed
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTimeSeconds) {
        this.totalTime = totalTimeSeconds;
    }

    // Implement a toString method to format the session data for writing to the file
    @Override
    public String toString() {
        return startTime + "," + endTime + "," + totalTime + System.lineSeparator();
    }
}

