package components;

import java.util.ArrayList;
import java.util.List;

class Message
{
    private int direction;
    private String destination;
    private List<String> path = new ArrayList<>();

    void add(String s) { path.add(s); }

    void remove() { path.remove(0); }

    void setPath(List<String> path) { this.path = path; }

    List<String> getPath() { return path; }
    int getDirection() { return direction; }
    void setDirection(int direction) { this.direction = direction; }
    String getDestination() { return destination; }
    void setDestination(String destination) { this.destination = destination; }
}
