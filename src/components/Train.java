package components;

public class Train extends Component implements Runnable
{
    private Track currentTrack;
    private int direction; // -1 is left, 1 is right

    @Override
    public void run()
    {
       //determine direction
       if(currentTrack.getPrevious() instanceof Station){
           Message first = new Message();
           first.setDirection(1);
           currentTrack.accept(first);
       }
        //determine direction
        if(currentTrack.getNext() instanceof Station){
            Message first = new Message();
            first.setDirection(-1);
            currentTrack.accept(first);
        }

    }

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
    }

    public Track getCurrentTrack() { return currentTrack; }
}
