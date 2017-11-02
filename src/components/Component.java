package components;

public abstract class Component implements Runnable
{
    private Message msg;
    private Component right;
    private Component left;
    private boolean shutdown;

    @Override
    public void run()
    {
        shutdown = false;

        while (!shutdown)
        {
            //System.out.println(Thread.currentThread().getName() + " has started!");
            update();
        }
    }

    abstract void update();

    synchronized void accept(Message msg)
    {
        this.msg = msg;
        notify();
    }

    synchronized void justWait()
    {
        try
        {
            //System.out.println(Thread.currentThread().getName() + " is waiting.");
            wait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    boolean processMessage()
    {
        String destination = msg.getDestination();
        Component neighbor = getNeighbor(msg.getDirection());

        if (Thread.currentThread().getName().equals(destination))
        {
            System.out.println("Arrived in " + destination);
            System.out.println();
            return true;
        }
        else
        {
            if (neighbor != null) neighbor.accept(msg);
            return false;
        }
    }

    private Component getNeighbor(int direction)
    {
        if (direction == 1) return right;
        else return left;
    }

    public Component getRight()
    {
        return right;
    }
    public void setRight(Component right)
    {
        this.right = right;
    }
    public Component getLeft()
    {
        return left;
    }
    public void setLeft(Component left)
    {
        this.left = left;
    }
    Message getMsg()
    {
        return msg;
    }
    void shutdown()
    {
        this.shutdown = true;
    }
}
