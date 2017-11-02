package components;

public class Track extends Component
{
    void update()
    {
        justWait();
        System.out.println(Thread.currentThread().getName() + " received message and woke up.");
        processMessage();
    }
}
