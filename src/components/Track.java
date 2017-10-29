package components;

public class Track extends Component implements Runnable
{
    private Component next; // reference to the track to the right
    private Component previous; // reference to the track to the left
    private boolean occupied;
    @Override
    public void run()
    {
        System.out.println("in track");
            if (this.msgQueue.size() == 1)
            {
                if (this.msgQueue.get(0).getDirection() == 1)
                {
                    next.accept(this.msgQueue.get(0));
                    msgQueue.clear();
                }
                if (this.msgQueue.get(0).getDirection() == -1)
                {
                    previous.accept(this.msgQueue.get(0));
                    msgQueue.clear();
                }
            }
    }

    public Component getNeighbor(int direction)
    {
        if (direction == 1) return this.next;
        else return this.previous;
    }

    public boolean getOccupied(){return occupied;}

    public void setOccupied(boolean occupied){this.occupied = occupied;}

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
