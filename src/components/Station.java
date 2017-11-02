package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station

    void update()
    {
        justWait();
        System.out.println(Thread.currentThread().getName() + " received message and woke up.");

        // if true then the correct station is found so, send confirmation to the train
        if (processMessage())
        {
            sendConfirmation();
            System.exit(0);
        }
    }

    private void sendConfirmation()
    {
        Message msg = new Message();
        msg.setDestination("Train"); // temporary, for testing
        msg.setDirection(-1);
        originator.accept(msg);
    }

    public void setOriginator(Track originator)
    {
        this.originator = originator;
    }
}
