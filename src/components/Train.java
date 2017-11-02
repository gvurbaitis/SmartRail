package components;

public class Train extends Component
{
    private Track currentTrack;
    //private int direction; // -1 is left, 1 is right (not sure if needed just yet)

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
    }

    void update()
    {
        findRoute();
        justWait();
        move();
        shutdown(); // temporarily for testing
        System.exit(0); // for now when train reaches destination kill simulation
    }

    private void findRoute()
    {
        Message msg = new Message();
        msg.setDestination("Atlantis");
        msg.setDirection(1);
        currentTrack.setTrain(this);

        currentTrack.accept(msg);
    }

    private void move()
    {
        if (isRouteConfirmed())
        {
            System.out.println("The path is: " + getMsg().getPath());
            System.out.println();
            System.out.println("moving...");

            // temporary stop condition
            while (!(currentTrack.getNeighbor(1) instanceof Station))
            {
                System.out.println("On " + currentTrack.getName());
                currentTrack.setTrain(null);

                if(currentTrack.getName().equals(getMsg().getPath().get(0)))
                {
                    currentTrack = (Track) currentTrack.getNeighbor(1);
                    getMsg().pop();
                }

                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            System.out.println("On " + currentTrack.getName());
            System.out.println("Arrived in " + currentTrack.getNeighbor(1).getName());
        }
    }

    private boolean isRouteConfirmed()
    {
        if (getName().equals(getMsg().getDestination()))
        {
            System.out.println(getName() + " received confirmation!");
            return true;
        }
        else
        {
            System.out.println(getName() + " cannot find path!");
            return false;
        }
    }
}
