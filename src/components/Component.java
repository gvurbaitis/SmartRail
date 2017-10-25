package components;

import java.util.ArrayList;
import java.util.List;

public abstract class Component implements Runnable
{
    private List<Message> msgQueue;
    private Component next;
    private Component previous;
    private boolean shutdown;

    @Override
    public void run()
    {
        initialize();

        while(!shutdown)
        {
            try { this.wait(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public synchronized void accept(Message msg)
    {
        msgQueue.add(msg);
        this.notify();
    }

    private void initialize()
    {
        shutdown = false;
        msgQueue = new ArrayList<>();
    }
}
