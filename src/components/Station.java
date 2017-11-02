package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station

    void update()
    {
        justWait();
        System.out.println(Thread.currentThread().getName() + " received message and woke up.");
        processMessage();
    }

    public void setOriginator(Track originator)
    {
        this.originator = originator;
    }
}
