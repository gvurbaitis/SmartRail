package components;

public class Track extends Component
{
    private boolean occupied;

    /**
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
    }*/

    void update()
    {
        justWait();
    }

    public boolean getOccupied(){return occupied;}
    public void setOccupied(boolean occupied){this.occupied = occupied;}
}
