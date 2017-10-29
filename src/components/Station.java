package components;

public class Station extends Component implements Runnable
{
    private String city; // name of the city that the station is located at
    private Component originator; // the initial track connected to the station

    @Override
    public void run()
    {

    }

    public Station(String city)
    {
        this.city = city;
    }

    public void setOriginator(Component originator)
    {
        this.originator = originator;
    }
}
