package components;

class Message
{
    private int direction;
    private String destination;
    private String trainName;
    private boolean isValidPath;
    private String departureGroup;

    String getTrainName() { return trainName; }
    void setTrainName(String trainName) { this.trainName = trainName; }

    boolean isValidPath() { return isValidPath; }
    void setValidPath(boolean validPath) { isValidPath = validPath; }

    int getDirection() { return direction; }
    void setDirection(int direction) { this.direction = direction; }

    String getDestination() { return destination; }
    void setDestination(String destination) { this.destination = destination; }

    String getDepartureGroup() { return departureGroup; }
    void setDepartureGroup(String departureGroup) { this.departureGroup = departureGroup; }
}
