package components;

public class Light extends Component
{
    private boolean state; // true means green light, else false

    @Override
    void update()
    {
        checkNeighbors();
    }

    private void checkNeighbors()
    {
        Track left = (Track) getNeighbor(-1);
        Track right = (Track) getNeighbor(1);

        state = left.isLock() && right.isLock();
    }
}
