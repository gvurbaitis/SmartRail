package main.components;
//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Switch Class builds upon the
// message passing found in Track, adding
// complexity with regards to the type
// of switch, connecting to a track
// on the left or right, what position
// it is in and adding another pointer
// to a neighbor when flipped.
//***********************************
public class Switch extends Component
{
  // 0 type means switch connects to the RIGHT on a lane above or below
  // 1 type means switch connects to the LEFT on a lane above or below
  private int type;
  private boolean flipped; // true means switch is flipped up/down
  private Switch flippedNeighbor; // neighbor when switch is flipped
  /**
   * Input: None
   * Returns void
   * As the update method is
   * within a while loop, the
   * thread waits until a message is
   * passed so as not to use resources.
   * Accepting a message notifies the
   * thread and allows the Switch to
   * process whatever was passed.
   */
  void update()
  {
    justWait();
    processMessage();
  }
  /**
   * Input: None
   * Returns void
   * Process Message is based on the same
   * message passing core of the other
   * components, with branching based on whether
   * the message is heading to the train or not.
   * If it is not, the message is sent to both of
   * the neighbors if they are aligned with the
   * direction of passing. The switch is added to
   * a list of taken switches in the message, which
   * are popped off on the way back. If the message
   * is heading to the train and the path is valid,
   * the switch is locked, and the switch is flipped
   * if the departure is in a different lane. Otherwise
   * message is passed normally.
   */
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
        lock(); // lock this switch on the way back

        // if switch is in the same lane as departure
        if (getGroup().equals(getMsg().getDepartureGroup()))
        {
          flipped = false;
          getFlippedNeighbor().setFlipped(false);
          neighbor.accept(getMsg());
        } else // if switch is different lane then departure
        {
          // when message is returning to train only take the switch that you came from
          if (getName().equals(getMsg().getLastTakenSwitch()))
          {
            flipped = true;
            getMsg().removeLastTakenSwitch(); // remove switch from message list
            getFlippedNeighbor().lock(); // lock connected switch
            getFlippedNeighbor().setFlipped(true);
            getFlippedNeighbor().getNeighbor(dir).accept(getMsg());
          } else neighbor.accept(getMsg());
        }
      }
    } else // send message both ways at the switch intersection
    {
      neighbor.accept(getMsg()); // keep sending the message straight

      // only take switch if it is the right type
      if ((type == 0 && dir == 1) || (type == 1 && dir == -1))
      {
        // copy message to new instance and send it up/down a switch
        Message msg = new Message();
        msg.setValidPath(getMsg().isValidPath());
        msg.setDestination(getMsg().getDestination());
        msg.setDirection(getMsg().getDirection());
        msg.setDepartureGroup(getMsg().getDepartureGroup());
        msg.setTrainName(getMsg().getTrainName());
        msg.setTakenSwitches(getMsg().getTakenSwitches());

        // add add the next switch to the list
        msg.addTakenSwitch(getFlippedNeighbor().getName());

        getFlippedNeighbor().accept(msg);
      }
    }
  }
  /**
   * Input: none
   * Returns int type, 0 for a connection
   * on the right, 1 for the left
   * Gets the type of the switch
   * to determine if it is connected
   * on the right or the left
   */
  public int getType()
  {
    return type;
  }
  /**
   * Input: int type, 0 for a connection
   * on the right, 1 for the left
   * Returns void
   * Sets the type of the station
   * to indicate whether it is connected
   * on the right or the left
   */
  public void setType(int type)
  {
    this.type = type;
  }
  /**
   * Input: none
   * Returns boolean flipped,
   * the status of the switch
   * Gets the flipped boolean
   * indicating the switch connects
   * to a different lane if true
   * or false if it is connected to
   * the same lane.
   */
  boolean isFlipped()
  {
    return flipped;
  }
  /**
   * Input: boolean flipped,
   * the status of the switch
   * Returns void
   * Sets the flipped boolean
   * to true to indicate the switch
   * connects to a different lane
   * or false if it is connected to
   * the same lane.
   */
  public void setFlipped(boolean flipped)
  {
    this.flipped = flipped;
  }
  /**
   * Input: none
   * Returns switch flippedNeighbor,
   * the switch connected on the
   * track on the other lane.
   * Gets the switch on the other
   * lane for when a train is
   * about to switch lanes.
   */
  public Switch getFlippedNeighbor()
  {
    return flippedNeighbor;
  }
  /**
   * Input: switch flippedNeighbor,
   * the switch connected on the
   * track on the other lane.
   * Returns void
   * Sets the switch that is connected
   * to the current switch when flipped.
   */
  public void setFlippedNeighbor(Switch flippedNeighbor)
  {
    this.flippedNeighbor = flippedNeighbor;
  }
}
