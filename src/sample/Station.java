package sample;

public class Station extends Component implements Runnable
{
  String id;
  public void run() {
    System.out.println("At"+id);
  }
}
