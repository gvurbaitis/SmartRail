import display.XMLParser;
import javafx.application.Application;
import javafx.stage.Stage;

public class SmartRail extends Application
{
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        XMLParser parser = new XMLParser();
        parser.parse();
    }
}
