package components;

public class Track extends Component implements Runnable
{
    private Component next; // reference to the track to the right
    private Component previous; // reference to the track to the left

    @Override
    public void run()
    {
         if(this.msgQueue.get(0).getDirection() == 1){
             
         }
    }

    public Component getNeighbor(int direction)
    {
        if (direction == 1) return this.next;
        else return this.previous;
    }

    public Component getNext()
    {
        return next;
    }

    public void setNext(Component next)
    {
        this.next = next;
    }

    public Component getPrevious()
    {
        return previous;
    }

    public void setPrevious(Component previous)
    {
        this.previous = previous;
    }
}
