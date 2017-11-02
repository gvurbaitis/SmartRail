package components;

public class Track extends Component
{
    private boolean occupied;

    void update()
    {
        justWait();
        System.out.println(Thread.currentThread().getName() + " received message and woke up.");
        processMessage();
    }

    boolean getOccupied(){return occupied;}
    void setOccupied(boolean occupied){this.occupied = occupied;}
}
