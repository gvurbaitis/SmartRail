package components;

public class Train extends Component
{
    private Track currentTrack;
    private int direction; // -1 is left, 1 is right

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
        currentTrack.setOccupied(true);
    }

    void update()
    {
        findRoute();
        shutdown(); // temporarily for testing
    }

    private void findRoute()
    {
        Message msg = new Message();
        msg.setDestination("Atlantis");

        currentTrack.accept(msg);
    }
}
