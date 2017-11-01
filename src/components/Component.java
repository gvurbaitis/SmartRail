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
            System.out.println(Thread.currentThread().getName() + " has started!");
            update();
        }
    }

    abstract void update();

    public synchronized void accept(Message msg)
    {
        this.msg = msg;
        System.out.println(super.getClass()+"direction "+msg.getDirection()+"destination "+msg.getDestination());
        this.notify();
    }

    synchronized void justWait()
    {
        try
        {
            System.out.println(Thread.currentThread().getName() + " is waiting.");
            this.wait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
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
}
