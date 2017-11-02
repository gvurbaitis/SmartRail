package components;

public class Train extends Component
{
    private Track currentTrack;
    //private int direction; // -1 is left, 1 is right (not sure if needed just yet)

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
    }

    void update()
    {
        findRoute();
        justWait();

        // temporary, for testing
        if (Thread.currentThread().getName().equals(getMsg().getDestination()))
        {
            System.out.println(Thread.currentThread().getName() + " received confirmation!");
        }
        shutdown(); // temporarily for testing
    }

    private void findRoute()
    {
        Message msg = new Message();
        msg.setDestination("Atlantis");
        msg.setDirection(1);

        currentTrack.accept(msg);
    }
}
