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
            System.out.println(getMsg().isValidPath());
            if (getMsg().isValidPath())  // if valid path
            {
                lock(); // lock this switch on the way back
                // if in the switch is in the same lane as departure
                if (getGroup().equals(getMsg().getDepartureGroup()))
                {
                    flipped = false;
                    getFlippedNeighbor().setFlipped(false);
                    neighbor.accept(getMsg());
                }
                else // if switch is different lane then departure
                {
                    flipped = true;
                    getFlippedNeighbor().lock(); // lock connected switch
                    getFlippedNeighbor().setFlipped(true);
                    getFlippedNeighbor().getNeighbor(dir).accept(getMsg());
                }
            }
        }
        else // send message both ways at the switch intersection
        {
            neighbor.accept(getMsg()); // keep sending the message straight

            // only check switch if it is the right type
            if ((type == 0 && dir == 1) || (type == 1 && dir == -1))
            {
                // copy message to new instance and send it up/down a switch
                Message msg = new Message();
                msg.setValidPath(getMsg().isValidPath());
                msg.setDestination(getMsg().getDestination());
                msg.setDirection(getMsg().getDirection());
                msg.setDepartureGroup(getMsg().getDepartureGroup());
                msg.setTrainName(getMsg().getTrainName());

                getFlippedNeighbor().accept(msg);
            }
        }
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public void setFlipped(boolean flipped) { this.flipped = flipped; }
    boolean isFlipped() { return flipped; }

    public Switch getFlippedNeighbor() { return flippedNeighbor; }
    public void setFlippedNeighbor(Switch flippedNeighbor) { this.flippedNeighbor = flippedNeighbor; }
}
