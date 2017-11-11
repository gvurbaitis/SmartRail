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
        move();
        shutdown(); // temporarily for testing
        System.exit(0); // for now when train reaches destination kill simulation
    }

    private void findRoute()
    {
        Message msg = new Message();
        msg.setDestination(destination);
        msg.setDirection(dir);
        msg.setTrainName(getName());
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
        boolean shouldUnlock = false;

        if (isRouteConfirmed())
        {
            Component neighbor;

            System.out.println();
            System.out.println("moving...");

            // temporary stop condition
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
                    shouldUnlock = true;
                    neighbor = currentTrack.getNeighbor(dir);
                }

                if (currentTrack.isLock()
                        && !(neighbor instanceof SwitchTop)
                        && !(neighbor instanceof SwitchBottom))
                {
                    sleep();
                    if (shouldUnlock) currentTrack.unlock();
                    currentTrack = (Track) neighbor;
                    sleep();
                }

                if (neighbor instanceof SwitchTop && neighbor.isLock())
                {
                    //System.out.println(dir);
                    //System.out.println("Found: " + currentTrack.getNeighbor(dir).getName());
                    if (((SwitchTop) neighbor).getUp() && dir == 1 || /*!((SwitchTop) neighbor).getUp() &&*/ dir == -1)
                    {
                        sleep();
                        if (shouldUnlock)
                        {
                            neighbor.unlock();
                            currentTrack.unlock();
                        }
                        currentTrack = (Track) neighbor.getNeighbor(dir);
                        //System.out.println(currentTrack.getName());
                        sleep();
                    }
                    else
                    {
                        sleep();

                        if (shouldUnlock)
                        {
                            neighbor.unlock();
                            ((SwitchTop) neighbor).getDownRight().unlock();
                            currentTrack.unlock();
                        }
                        currentTrack = (Track) ((SwitchTop) neighbor).getDownRight().getNeighbor(dir);
                        //System.out.println(currentTrack.getName());
                        sleep();
                    }
                }
                    if (neighbor instanceof SwitchBottom && neighbor.isLock())
                    {
                        //System.out.println(dir);
                        //System.out.println("Found: " + currentTrack.getNeighbor(dir).getName());
                        if (((SwitchBottom) neighbor).getUp() && dir == 1 || !((SwitchBottom) neighbor).getUp() && dir == -1)
                        {
                            sleep();
                            if (shouldUnlock)
                            {
                                neighbor.unlock();
                                currentTrack.unlock();
                            }
                            currentTrack = (Track) neighbor.getNeighbor(dir);
                            //System.out.println(currentTrack.getName());
                            sleep();
                        } else
                        {
                            sleep();

                            if (shouldUnlock)
                            {
                                neighbor.unlock();
                                ((SwitchBottom) neighbor).getUpLeft().unlock();
                                currentTrack.unlock();
                            }

                            currentTrack = (Track) ((SwitchBottom) neighbor).getUpLeft().getNeighbor(dir);
                            //System.out.println(currentTrack.getName());
                            sleep();
                        }
                    }


            }
        }
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
}
