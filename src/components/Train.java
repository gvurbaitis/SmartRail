package components;

public class Train extends Component
{
    private Track currentTrack;
    private String departure, destination;
    private int dir; // -1 is left, 1 is right (not sure if needed just yet)

    public Train(Track currentTrack)
    {
        this.currentTrack = currentTrack;
    }

    void update()
    {
        findRoute();
        justWait();
        //System.out.println(getName() + " received a message and woke up!");
        if (isRouteConfirmed()) move();
        shutdown(); // temporarily for testing
        System.exit(0); // for now when train reaches destination kill simulation
    }

    private void findRoute()
    {
        Message msg = new Message();
        msg.setDestination(destination);
        msg.setDirection(dir);
        msg.setTrainName(getName());
        msg.setDepartureGroup(getGroup());
        currentTrack.setTrain(this);

        currentTrack.accept(msg);
    }

    private boolean isRouteConfirmed()
    {
        if (getMsg().isValidPath())
        {
            System.out.println(getName() + " received confirmation!");
            return true;
        }
        else
        {
            System.out.println(getName() + " shutting down...");
            shutdown();
            return false;
        }
    }

    private void move()
    {
        Component neighbor;
        boolean shouldUnlock = false;

        System.out.println();
        System.out.println("moving...");

        while (true)
        {
            System.out.println("On " + currentTrack.getName());
            currentTrack.setTrain(null);
            neighbor = currentTrack.getNeighbor(dir);

            if (neighbor instanceof Station)
            {
                System.out.println("Arrived in " + currentTrack.getNeighbor(dir).getName());

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

            if (neighbor instanceof Switch)
            {
                if (neighbor.isLock()) processSwitches((Switch) neighbor, shouldUnlock);
            }
        }
    }

    private void processSwitches(Switch sw, boolean shouldUnlock)
    {
        System.out.println("On " + sw.getName());

        sleep();
        if (!sw.isFlipped()) // if switch is flat then don't travel along switch
        {
            if (shouldUnlock)
            {
                currentTrack.unlock();
                sw.unlock();
            }

            currentTrack = (Track) sw.getNeighbor(dir);
        }
        else
        {
            if (shouldUnlock)
            {
                currentTrack.unlock();
                sw.unlock();
                sw.getFlippedNeighbor().unlock();
            }

            currentTrack = (Track) (sw.getFlippedNeighbor().getNeighbor(dir));
        }
        sleep();
    }

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

    public synchronized Track getCurrentTrack() { return currentTrack; }

    public void setDestination(String destination) { this.destination = destination; }

    public void setDeparture(String departure) { this.departure = departure; }
    public String getDeparture() { return departure; }

    public void setDir(int dir) { this.dir = dir; }
}
