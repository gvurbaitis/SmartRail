package components;

public class Light extends Component
{
    private boolean on; // true == green light else red light

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up!");
        processMessage();
    }

    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();

        // if the message is going back to the train
        if (destination.equals(getMsg().getTrainName()))
        {
            // if not valid path unlock, else if valid path lock
            if(!getMsg().isValidPath())
            {
                neighbor.accept(getMsg());
            }
            else
            {
                lock(); // lock on the way back, when valid path
                on = true;
                neighbor.accept(getMsg());
            }
        }
        else // if the message is going to the destination station
        {
            neighbor.accept(getMsg());
        }
    }

    public boolean isOn()
    {
        return on;
    }
    public void setOn(boolean on){ this.on = on; }
}
