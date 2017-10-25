package components;

public class Track extends Component
{
    private Component next; // reference to the track to the right
    private Component previous; // reference to the track to the left

    public Component getNeighbor(int direction)
    {
        if (direction == 1) return this.next;
        else return this.previous;
    }

    public Component getNext()
    {
        return next;
    }

    public void setNext(Component next)
    {
        this.next = next;
    }

    public Component getPrevious()
    {
        return previous;
    }

    public void setPrevious(Component previous)
    {
        this.previous = previous;
    }
}
