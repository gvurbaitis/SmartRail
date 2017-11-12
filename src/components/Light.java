package components;

public class Light extends Component
{
    private boolean on = false;
    @Override
    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up." + " (" + isLock() + ")");
        if(getLeft().isLock()||getRight().isLock()){
            setOn(true);
        }
        if(!getLeft().isLock()&&!getRight().isLock()){
            setOn(false);
        }
        processMessage();
    }

    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        neighbor.accept(getMsg());
    }
    public boolean isOn()
    {
        return on;
    }
    public void setOn(boolean on){
        this.on = on;
        System.out.println("on = "+on);
    }

}
