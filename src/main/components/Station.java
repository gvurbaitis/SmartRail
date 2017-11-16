package main.components;
//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Station Class is similar to
// a track with similar message passing,
// however it has one track which is
// connected to it, called the originator
// It also has a type, indicating whether
// it is a normal station or a dead end.
//***********************************
public class Station extends Component
{
  private Track originator; // the initial track connected to the station
  private int type; // 0 == normal station, 1 == dead end
  /**
   * Input: None
   * Returns void
   * As the update method is
   * within a while loop, the
   * thread waits until a message is
   * passed so as not to use resources.
   * Accepting a message notifies the
   * thread and allows the Station to
   * send a confirmation, if
   * it is the correct station, before
   * going into another wait.
   */
  void update()
  {
    justWait();

    // if true then the correct station is found so,
    // send confirmation to the train
    if (processMessage()) sendConfirmation();
  }
  /**
   * Input: None
   * Returns void
   * Process Message is fairly simple if
   * the destination hasn't been found yet, or
   * if the path was found to be invalid,
   * it simply passes it on to the next neighbor.
   * If the message is returning to the station
   * the message is passed, but the component is
   * also locked, and the light is turned on.
   */
  private boolean processMessage()
  {
    // only process message if not a dead end
    if (type == 0)
    {
      String destination = getMsg().getDestination();

      if (getName().equals(destination))
      {
        getMsg().setValidPath(true);

        return true;
      }
    }

    return false;
  }
  /**
   * Input: None
   * Returns void
   * Being called upon finding the
   * correct station, send confirmation
   * alters the passed message to set
   * the train as the new destination
   * and reverse the direction before
   * passing it back to the originator.
   */
  private void sendConfirmation()
  {
    getMsg().setDestination(getMsg().getTrainName());
    getMsg().setDirection(getMsg().getDirection() * -1);
    originator.accept(getMsg());
  }
  /**
   * Input: none
   * Returns Track originator, the
   * track the station is connected to.
   * Gets the track connected to the
   * current station
   */
  public Track getOriginator()
  {
    return originator;
  }
  /**
   * Input: Track originator, the
   * track the station is to be
   * connected to.
   * Returns void
   * Sets the track connected to the
   * current station to the inputted
   * track
   */
  public void setOriginator(Track originator)
  {
    this.originator = originator;
  }
  /**
   * Input: none
   * Returns int type, 0 for a normal
   * station, 1 for a dead end
   * Gets the type of the station
   * to process whether it is
   * a dead end or normal station
   */
  public int getType()
  {
    return type;
  }
  /**
   * Input: int type, 0 for a normal
   * station, 1 for a dead end
   * Returns void
   * Sets the type of the station
   * to indicate whether it is
   * a dead end or normal station
   */
  public void setType(int type)
  {
    this.type = type;
  }
}
