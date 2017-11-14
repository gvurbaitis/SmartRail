package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station
    private int side; // 0 == left side, 1 == right side

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up.");

        // if true then the correct station is found so, send confirmation to the train
        if (processMessage()) sendConfirmation();
    }

    private boolean processMessage()
    {
        String destination = getMsg().getDestination();

        if (getName().equals(destination))
        {
            System.out.println("Found " + destination + "!");
            System.out.println();
            getMsg().setValidPath(true);

            return true;
        }

        return false;
    }

    private void sendConfirmation()
    {
        getMsg().setDestination(getMsg().getTrainName());
        getMsg().setDirection(getMsg().getDirection() * -1);
        originator.accept(getMsg());
    }

    public void setOriginator(Track originator) { this.originator = originator; }

    public int getSide() { return side; }
    public void setSide(int side) { this.side = side; }
}
