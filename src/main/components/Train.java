package main.components;

//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Train is the most unique of all
// of the components with most emphasis
// placed on methods for finding and
// confirming a route, and then moving
// along the route
//***********************************
public class Train extends Component
{
  private Track currentTrack;
  private String departure, destination;
  private boolean isValidPath; // true when route is confirmed (used to spawn new train)
  private int dir; // -1 is left, 1 is right (not sure if needed just yet)

  public Train(Track currentTrack)
  {
    this.currentTrack = currentTrack;
    isValidPath = false; // initially false
  }

  /**
   * Input: None
   * Returns void
   * The train attempts to find a route,
   * which takes substantially less than
   * 2 seconds. If a route has not been
   * found in 2 seconds the train shuts down.
   * Otherwise if the route is confirmed,
   * the train moves along the route.
   */
  void update()
  {
    findRoute();

    // wait for 2 seconds, if the message isn't back then there is no path
    try
    {
      synchronized (this)
      {
        wait(2000);
      }
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }

    // if there is a locked path then move
    if (isRouteConfirmed()) move();
    shutdown(); // shutdown after move is complete
  }
  /**
   * Input: None
   * Returns void
   * As the route validity is
   * determined at the station,
   * the train simply creates a message
   * with its name, destination,
   * departure group and direction and
   * sends it to the track it is on.
   */
  private void findRoute()
  {
    Message msg = new Message();
    msg.setDestination(destination);
    msg.setDirection(dir);
    msg.setValidPath(false); // false by default
    msg.setTrainName(getName());
    msg.setDepartureGroup(getGroup());
    currentTrack.setTrain(this);

    currentTrack.accept(msg);
  }
  /**
   * Input: None
   * Returns boolean true if
   * the path is valid, false
   * if invalid.
   * The current message is
   * evaluated to see if the
   * station was found. If it was
   * found, returns true, if not
   * it returns false and shuts
   * down the train.
   */
  private boolean isRouteConfirmed()
  {
    if (getMsg() != null)
    {
      if (getMsg().isValidPath())
      {
        return true;
      } else
      {
        shutdown();
        return false;
      }
    }
    return false;
  }
  /**
   * Input: None
   * Returns void
   * The Train responds differently based on if
   * the neighbor is a Switch, Track, Light or
   * Station. If it is returning to its original station
   * the method simply breaks. If it is at its destination
   * station, it switches directions and begins unlocking
   * its previously secured path. At a light, it waits until
   * it changes to green to move. At Tracks and switches it
   * unlocks based upon whether the Should unlock field is
   * true.
   */
  private void move()
  {
    Component neighbor;
    boolean shouldUnlock = false;
    isValidPath = true; // when true add train to gui (in main.display class)

    while (true)
    {
      currentTrack.setTrain(null);
      neighbor = currentTrack.getNeighbor(dir);

      if (neighbor instanceof Station)
      {
        // break once round trip is complete
        if (neighbor.getName().equals(departure)) break;

        dir *= -1;
        neighbor = currentTrack.getNeighbor(dir);
        shouldUnlock = true;
      }

      if (neighbor instanceof Track && currentTrack.isLock())
      {
        sleep();
        if (shouldUnlock) currentTrack.unlock();
        currentTrack = (Track) neighbor;
        sleep();
      }

      if (neighbor instanceof Light)
      {
        while (!((Light) neighbor).isOn()) // wait until condition is met

          sleep();
        if (shouldUnlock)
        {
          currentTrack.unlock();
          neighbor.unlock();
          ((Light) neighbor).setOn(false);
        }

        currentTrack = (Track) neighbor.getNeighbor(dir);
        sleep();
      }

      if (neighbor instanceof Switch)
      {
        if (neighbor.isLock()) processSwitches((Switch) neighbor, shouldUnlock);
      }
    }
  }
  /**
   * Input: Switch sw, the current switch,
   * boolean shouldUnlock, whether to unlock
   * the switches
   * Returns void
   * If the switch is flat, the track continues
   * onto its normal neighbor. Otherwise it goes
   * to the neighbor of the flipped neighbor, as
   * the flipped neighbor is another switch. On the way
   * back to the original station, the switch is
   * flipped back to flat.
   */
  private void processSwitches(Switch sw, boolean shouldUnlock)
  {
    sleep();
    if (!sw.isFlipped()) // if switch is flat then don't travel along switch
    {
      if (shouldUnlock)
      {
        // unlock necessary main.components
        currentTrack.unlock();
        sw.unlock();
        sw.getFlippedNeighbor().unlock();
      }

      currentTrack = (Track) sw.getNeighbor(dir);
    } else
    {
      if (shouldUnlock)
      {
        // unlock necessary main.components
        currentTrack.unlock();
        sw.unlock();
        sw.getFlippedNeighbor().unlock();

        // flip off switches on your way back to departure
        sw.setFlipped(false);
        sw.getFlippedNeighbor().setFlipped(false);
      }

      currentTrack = (Track) (sw.getFlippedNeighbor().getNeighbor(dir));
    }
    sleep();
  }
  /**
   * Input: none
   * Returns void
   * Sleep puts the thread to
   * sleep for 150 milliseconds
   * and catches an Interrupted
   * Exception.
   */
  private void sleep()
  {
    try
    {
      Thread.sleep(150);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
  /**
   * Input: none
   * Returns Track currentTrack,
   * the track the train is on
   * Gets the track the train
   * is on.
   */
  public synchronized Track getCurrentTrack()
  {
    return currentTrack;
  }
  /**
   * Input: String Destination, the
   * name of the station the train
   * is attempting to reach
   * Returns void
   * Sets the destination the train is
   * trying to reach.
   */
  public void setDestination(String destination)
  {
    this.destination = destination;
  }
  /**
   * Input: String Departure, the
   * name of the station the train
   * leaving and returning to.
   * Returns void
   * Sets the station the train is
   * departing.
   */
  public void setDeparture(String departure)
  {
    this.departure = departure;
  }
  /**
   * Input: none
   * Returns int dir, the direction
   * the train is going, -1 for left
   * and 1 for right.
   * Gets the direction of the train.
   */
  public int getDir()
  {
    return dir;
  }
  /**
   * Input: int dir, the direction
   * the train is going, -1 for left
   * and 1 for right.
   * Returns void
   * Sets the direction of the train.
   */
  public void setDir(int dir)
  {
    this.dir = dir;
  }
  /**
   * Input: none
   * Returns boolean isValidPath
   * whether or not the Station was
   * found.
   * Returns true if the station was found,
   * false if it was not.
   */
  public boolean isValidPath()
  {
    return isValidPath;
  }
}
