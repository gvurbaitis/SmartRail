package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.display.MainMenu;

/**
 * Main threads entry point. Initializes the Main Menu
 * and passes it the window object.
 */
public class SmartRail extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Method called by main thread to launch GUI
     * @param primaryStage main window object
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MainMenu mainMenu = new MainMenu(primaryStage);
        mainMenu.initialize();
    }
}
