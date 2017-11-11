package components;

public class SwitchTop extends Component
{
  private boolean up = false;
  private Component train = null; // null when train is not on this track
  private Component downRight = null;

  void update()
  {
    justWait();
    lock();
    System.out.println(getName() + " received message and woke up.");
    processMessage();
  }

  private void processMessage()
  {
    Component neighbor = getNeighbor(getMsg().getDirection());
    String destination = getMsg().getDestination();

    if (train != null && destination.equals("Train"))
    {
      train.accept(getMsg());
    }
    else if(!this.up && getMsg().getDirection() == 1)
    {
      downRight.accept(getMsg());
    }
    else
    {
      neighbor.accept(getMsg());
    }
  }
  public void setDownRight(Component downRight) { this.downRight = downRight; }
  public Component getDownRight(){return this.downRight; }
  public void setUp(boolean up) { this.up = up; }
  boolean getUp(){return this.up; }
  void setTrain(Component train) { this.train = train; }
}
