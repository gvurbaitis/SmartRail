package main.components;

public abstract class Component implements Runnable
{
    private Message msg;
    private String name;
    private String group;

    private Component right;
    private Component left;

    private boolean shutdown;
    private boolean lock;

    private double x, y;

    @Override
    public void run()
    {
        shutdown = false;
        name = Thread.currentThread().getName();
        group = Thread.currentThread().getThreadGroup().getName();

        while (!shutdown)
        {
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

    public String getName() { return this.name; }
    public String getGroup() { return this.group; }
    Message getMsg() { return msg; }

    void shutdown() { this.shutdown = true; }
    public boolean isShutdown() { return this.shutdown; }

    public void setRight(Component right) { this.right = right; }
    public void setLeft(Component left) { this.left = left; }

    public Component getLeft() { return left; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    boolean isLock() { return lock; }
    void unlock() { this.lock = false; }
    void lock() { this.lock = true; }
}
