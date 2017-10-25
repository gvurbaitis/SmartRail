package sample;

//Welcome Akash!

public class Track extends Component implements Runnable{
  Component left;
  Component right;
  boolean occupied;
  public void run() {
    if (right instanceof Station)
    {
      System.out.println("Arrived at Station!");
    }
    else if(right instanceof Track){
      ((Track) right).occupied = true;
    }
  }

}
