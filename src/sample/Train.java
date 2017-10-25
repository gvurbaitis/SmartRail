package sample;

public class Train extends Component implements Runnable{
  Component current;
  public void run() {
    System.out.println("Train Running!");
  }


}
