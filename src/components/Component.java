package components;

public abstract class Component implements Runnable
{
    private Message msg;
    private String name;
    private Component right;
    private Component left;
    private boolean shutdown;
    private double x, y;

    @Override
    public void run()
    {
        shutdown = false;
        name = Thread.currentThread().getName();

        while (!shutdown)
        {
            //System.out.println(getName() + " has started!");
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
            //System.out.println(getName() + " is waiting.");
            wait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    Component getNeighbor(int direction)
    {
        if (direction == 1) return right;
        else return left;
    }

    String getName() { return this.name; }
    Message getMsg()
    {
        return msg;
    }
    void shutdown()
    {
        this.shutdown = true;
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

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
}
