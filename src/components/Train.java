package components;

public class Train extends Component implements Runnable
{
    private Track currentTrack;
    private int direction; // -1 is left, 1 is right
    private boolean moving = false;

    @Override
    public void run()
    {
        System.out.println("in Train");
       //determine direction
       if(currentTrack.getPrevious() instanceof Station && !moving){
           Message first = new Message();
           first.setDirection(1);
           currentTrack.accept(first);
//           try
//           {
//               Thread.sleep(2000);
//               if (((Station) currentTrack.getPrevious()).getGo()){
//
//           }
//
//           } catch (InterruptedException e)
//           {
//               e.printStackTrace();
//           }
       }
        //determine direction
        if(currentTrack.getNext() instanceof Station  && !moving){
            Message first = new Message();
            first.setDirection(-1);
            currentTrack.accept(first);
//            try
//            {
//                Thread.sleep(2000);
//                if (((Station) currentTrack.getNext()).getGo()){
//
//                }
//
//            } catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
        }

    }

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
        currentTrack.setOccupied(true);
    }

    public Track getCurrentTrack() { return currentTrack; }
}
