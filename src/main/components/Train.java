package main.components;

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

    void update()
    {
        findRoute();

        // wait for 2 seconds, if the message isn't back then there is no path
        try
        {
            synchronized (this) { wait(2000); }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // if there is a locked path then move
        if (isRouteConfirmed()) move();
        shutdown(); // shutdown after move is complete
    }

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

    private boolean isRouteConfirmed()
    {
        if (getMsg() != null)
        {
            if (getMsg().isValidPath())
            {
                return true;
            }
            else
            {
                shutdown();
                return false;
            }
        }
        return false;
    }

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
        }
        else
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

    private void sleep()
    {
        try
        {
            Thread.sleep(150);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized Track getCurrentTrack() { return currentTrack; }


    public void setDestination(String destination) { this.destination = destination; }

    public void setDeparture(String departure) { this.departure = departure; }

    public void setDir(int dir) { this.dir = dir; }

    public int getDir() { return dir; }

    public boolean isValidPath() { return isValidPath; }
}
