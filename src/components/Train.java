package components;

public class Train extends Component
{
    private Track currentTrack;
    private int direction; // -1 is left, 1 is right

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
    }

    public Track getCurrentTrack() { return currentTrack; }
}
