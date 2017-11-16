package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.display.MainMenu;

public class SmartRail extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MainMenu mainMenu = new MainMenu(primaryStage);
        mainMenu.initialize();
    }
}
