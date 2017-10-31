package components;

import sun.jvm.hotspot.gc_implementation.g1.HeapRegion;

public class Track extends Component implements Runnable
{
    public Component next; // reference to the track to the right
    public Component previous; // reference to the track to the left
    private boolean occupied;
    @Override
    public void run()
    {
        for (int i = 0; i < 8; i++)
        {


        while (this.msgQueue.size() == 0)
        {        }
        System.out.println(msgQueue.size());
            if (this.msgQueue.size() == 1)
            {
                    System.out.println("in track, size" + msgQueue.size());
                    //note this goes too slow in relation to the other threads and activates both on a switchback
                    if (this.msgQueue.get(0).getDirection() == -1)
                    {
                        previous.accept(this.msgQueue.get(0));
                        msgQueue.clear();
                        System.out.println("previous" + previous);
                    }
                    if (this.msgQueue.get(0).getDirection() == 1)
                    {
                        next.accept(this.msgQueue.get(0));
                        System.out.println("Here");
                        msgQueue.clear();
                        System.out.println("size after here" + msgQueue.size());
                    }
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
