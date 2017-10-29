package components;

public class Station extends Component implements Runnable
{
    private String city; // name of the city that the station is located at
    private Track originator; // the initial track connected to the station
    private boolean trackOnLeft;
    private boolean go;

    @Override
    public void run()
    {
        System.out.println("In Station");
        if(msgQueue.size() ==1){
        if (trackOnLeft && this.msgQueue.get(0).getDirection() == 1
                && this.msgQueue.get(0).getDestination() == null
                && !originator.getOccupied())
        {
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

    public Station(String city)
    {
        this.city = city;
    }

    public void setOriginator(Track originator)
    {
        this.originator = originator;
    }
    public void setTrackSide(boolean trackOnLeft){this.trackOnLeft = trackOnLeft;}
    public void setGo(boolean go){this.go = go;}
    public boolean getGo(){return go;}
}
