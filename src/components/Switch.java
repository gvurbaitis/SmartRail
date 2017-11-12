package components;

public class Switch extends Component
{
    // 0 type means switch connects to the RIGHT on a lane above or below
    // 1 type means switch connects to the LEFT on a lane above or below
    private int type;
    private boolean flipped; // true means switch is flipped up/down
    private Switch flippedNeighbor; // neighbor when switch is flipped

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up." + " (" + isLock() + ")");
        processMessage();
    }

    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();
        int dir = getMsg().getDirection();

        // if the message is going back to the train
        if (destination.equals(getMsg().getTrainName()))
        {
            if (getMsg().isValidPath())  // if valid path
            {
                // flip switch and send message up/down the switch if the connecting switch is flipped
                if (getFlippedNeighbor().isFlipped())
                {
                    flipped = true;
                    getFlippedNeighbor().getNeighbor(dir).accept(getMsg());
                }
                else neighbor.accept(getMsg());
            }
        }
        else // send message both ways at the switch intersection
        {
            lock(); // lock switch
            neighbor.accept(getMsg()); // keep sending the message straight

            // only check switch if it is the right type
            if ((type == 0 && dir == 1) || (type == 1 && dir == -1))
            {
                flipped = true;
                getFlippedNeighbor().accept(getMsg());
            }
        }
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public void setFlipped(boolean flipped) { this.flipped = flipped; }
    boolean isFlipped() { return flipped; }

    Switch getFlippedNeighbor() { return flippedNeighbor; }
    public void setFlippedNeighbor(Switch flippedNeighbor) { this.flippedNeighbor = flippedNeighbor; }
}
