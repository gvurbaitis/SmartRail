package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station
    private boolean trackOnLeft;
    private boolean go;

    /**
    @Override
    public void run()
    {
        for (int i = 0; i < 2; i++)
        {
            while (this.msgQueue.size() == 0)
            {
            }
            System.out.println("In Station");
            if (msgQueue.size() == 1)
            {   //also has a switchback issue
                if (trackOnLeft && this.msgQueue.get(0).getDirection() == 1
                        && this.msgQueue.get(0).getDestination() != null
                        && originator.getOccupied())
                {
                    System.out.println("Go Ahead"+i);
                    go = true;
                }
                if (!trackOnLeft && this.msgQueue.get(0).getDirection() == -1
                        && this.msgQueue.get(0).getDestination() != null
                        && originator.getOccupied())
                {
                    System.out.println("Go Ahead"+i);
                    go = true;
                }
                if (trackOnLeft && this.msgQueue.get(0).getDirection() == 1
                        && this.msgQueue.get(0).getDestination() == null
                        && !originator.getOccupied())
                {
                    System.out.println("found first station");
                    Message destination = new Message();
                    destination.setDestination(city);
                    destination.setDirection(-1);
                    originator.accept(destination);
                    msgQueue.clear();
                }
                if (!trackOnLeft && this.msgQueue.get(0).getDirection() == -1
                        && this.msgQueue.get(0).getDestination() == null
                        && !originator.getOccupied())
                {
                    Message destination = new Message();
                    destination.setDestination(city);
                    destination.setDirection(1);
                    originator.accept(destination);
                    msgQueue.clear();
                }
            }
        }
    }*/

    void update()
    {
        justWait();
    }

    public void setOriginator(Track originator)
    {
        this.originator = originator;
    }
    public void setTrackSide(boolean trackOnLeft){this.trackOnLeft = trackOnLeft;}
    public void setGo(boolean go){this.go = go;}
    public boolean getGo(){return go;}
}
