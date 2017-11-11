package components;

public class SwitchBottom extends Component
{
  private boolean up = false;
  private Component train = null; // null when train is not on this track
  private Component upLeft = null;

  void update()
  {
    justWait();
    System.out.println(getName() + " received message and woke up.");
    processMessage();
  }

  private void processMessage()
  {
    Component neighbor = getNeighbor(getMsg().getDirection());
    String destination = getMsg().getDestination();

    getMsg().add(getName()); // add current track to path

    if (train != null && destination.equals("Train"))
    {
      train.accept(getMsg());
    }
    else if(this.up && getMsg().getDirection() == -1){
      upLeft.accept(getMsg());
    }
    else
    {
      System.out.println(getMsg().getDirection());
      neighbor.accept(getMsg());
    }
  }
  public void setUpLeft(Component upLeft) { this.upLeft = upLeft; }
  public Component getUpLeft(){return this.upLeft; }
  public void setUp(boolean up) { this.up = up; }
  public boolean getUp(){return this.up; }
  void setTrain(Component train) { this.train = train; }
}
