package main.components;

//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Component class is an abstract
// class extended by Light, Station
// Switch, Track, and Train. Basic
// thread functionality is implemented
// with a while loop running until
// a shutdown boolean is invoked, calling
// an abstract method update unique to
// each component. A synchronized justWait
// method handles the interruptedexception
// while accept provides the structure
// for messaging, notifying the thread
// to stop waiting and exectute.
//***********************************
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


    /**
     * Input: None
     * Returns void
     * Gathers the name and group from the thread
     * for each component and runs the associated
     * update() method until a shutdown boolean is
     * true.
     */
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

    /**
     * Input: None
     * Returns void
     * Performs the actions unique to
     * each component while its thread
     * is alive.
     */
    abstract void update();

    /**
     * Input: Message msg: Passes information
     * such as the direction of transfer and the
     * station of departure and destination.
     * Returns void
     * Sets the msg field of the component to the
     * message passed, and notifies the thread out
     * of a waiting state.
     */
    synchronized void accept(Message msg)
    {
        this.msg = msg;
        notify();
    }

    /**
     * Input: None
     * Returns void
     * Tries to put the thread into a wait,
     * catches an interrupted exception and
     * prints the stack trace if applicable.
     */
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

    /**
     * Input: int direction, 1 = right,
     * -1 = left
     * Returns Component, either the neighbor
     * on the right or on the left of the
     * component
     * returns the Neighbor on the right of the
     * component if the direction is 1, otherwise
     * returns the left neighbor
     */
    Component getNeighbor(int direction)
    {
        if (direction == 1) return right;
        else return left;
    }

    /**
     * Input: none
     * Returns String name, the name of the thread
     * the component is using
     * returns the name of the thread
     * the component is using
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Input: none
     * Returns String group, the group of the thread
     * the component is using
     * returns the group of the thread
     * the component is using
     */
    public String getGroup()
    {
        return this.group;
    }

    /**
     * Input: none
     * Returns Message msg, the last message passed
     * to the component
     * returns the last message the
     * component received
     */
    Message getMsg()
    {
        return msg;
    }

    /**
     * Input: none
     * Returns void
     * sets the shutdown boolean to true,
     * ending the while loop running the
     * thread.
     */
    void shutdown()
    {
        this.shutdown = true;
    }

    /**
     * Input: none
     * Returns boolean shutdown, whether the
     * thread is alive
     * Checks whether the thread has been shutdown
     */
    public boolean isShutdown()
    {
        return this.shutdown;
    }

    /**
     * Input: Component Right
     * Returns void
     * sets the inputted component to the
     * right field of the current component
     */
    public void setRight(Component right)
    {
        this.right = right;
    }

    /**
     * Input: none
     * Returns Component left, the component
     * to the left of the current component
     * returns  the component to the left
     * of the current component
     */
    public Component getLeft()
    {
        return left;
    }

    /**
     * Input: Component Left
     * Returns void
     * sets the inputted component to the
     * left field of the current component
     */
    public void setLeft(Component left)
    {
        this.left = left;
    }

    /**
     * Input: none
     * Returns double x, the horizontal
     * position of the component
     * returns the horizontal
     * position of the component in the GUI
     */
    public double getX()
    {
        return x;
    }

    /**
     * Input: double x, the horizontal position of the component
     * Returns void sets the horizontal
     * position of the component in the GUI
     */
    public void setX(double x)
    {
        this.x = x;
    }

    /**
     * Input: none
     * Returns double y, the vertical position of the component
     * returns the vertical position of the component in the GUI
     */
    public double getY()
    {
        return y;
    }

    /**
     * Input: double y, the vertical position of the component
     * Returns void sets the vertical position
     * of the component in the GUI
     */
    public void setY(double y)
    {
        this.y = y;
    }

    /**
     * Input: none
     * Returns boolean lock, the
     * protection of the component
     * returns whether or not the
     * component can be traversed
     */
    boolean isLock()
    {
        return lock;
    }

    /**
     * Input: none
     * Returns void
     * sets lock to false, indicating the component may not be traversed
     */
    void unlock()
    {
        this.lock = false;
    }

    /**
     * Input: none
     * Returns void
     * sets lock to true, indicating the component may be traversed
     */
    void lock()
    {
        this.lock = true;
    }
}
