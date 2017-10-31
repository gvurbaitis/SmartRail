import components.Station;
import components.Track;
import components.Train;
import display.XMLParser;
import javafx.application.Application;
import javafx.stage.Stage;

public class SmartRail extends Application
{
    public static void main(String[] args) {
        launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
      XMLParser parser = new XMLParser();
      parser.parse();
      Track track1 = new Track();
      Track track2 = new Track();
      Train train = new Train(track1);
      Station albuquerque = new Station("Albuquerque");
      Station atlantis = new Station("Atlantis");
      albuquerque.setOriginator(track1);
      albuquerque.setTrackSide(false);
      track1.setOccupied(true);
      track1.setPrevious(albuquerque);
      track1.setNext(track2);
      track2.setPrevious(track1);
      track2.setNext(atlantis);
      atlantis.setOriginator(track2);
      atlantis.setTrackSide(true);
      Thread abq = new Thread(albuquerque);
      Thread atl = new Thread(atlantis);
      Thread t1 = new Thread(track1);
      Thread t2 = new Thread(track2);
      Thread trn = new Thread(train);
      abq.start();
      atl.start();
      t1.start();
      t2.start();
      trn.start();
    }
}
