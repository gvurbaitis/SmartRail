package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station
    private int type; // 0 == normal station, 1 == dead end

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up.");

        // if true then the correct station is found so, send confirmation to the train
        if (processMessage()) sendConfirmation();
    }

    private boolean processMessage()
    {
        // only process message if not a dead end
        if (type == 0)
        {
            String destination = getMsg().getDestination();

            if (getName().equals(destination))
            {
                System.out.println("Found " + destination + "!");
                System.out.println();
                getMsg().setValidPath(true);

                return true;
            }
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
    public Track getOriginator() { return originator; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
}
