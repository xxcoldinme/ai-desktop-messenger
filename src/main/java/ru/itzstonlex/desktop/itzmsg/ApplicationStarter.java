package ru.itzstonlex.desktop.itzmsg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itzstonlex.desktop.itzmsg.form.FormKeys;
import ru.itzstonlex.desktop.itzmsg.loading.ApplicationServicesLoadingController;

public class ApplicationStarter extends Application {

  private static final String LOADPAGE_PATH = (File.separator + FormKeys.VIEW_FORMS_PATH + "loadpage.fxml");

  private static final String APP_TITLE = ("Messenger by @itzstonlex");

  private static final int APP_WIDTH = 1080;
  private static final int APP_HEIGHT = 720;

  @Override
  public void start(Stage stage) {
    URL resource = getClass().getResource(LOADPAGE_PATH);

    FXMLLoader fxmlLoader = new FXMLLoader(resource);
    fxmlLoader.setControllerFactory(cls ->
        new ApplicationServicesLoadingController(stage));

    try {
      setStageDefaultParameters(stage);

      Parent window = fxmlLoader.load();

      stage.setScene(new Scene(window));
      stage.show();

    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public static void setStageDefaultParameters(Stage stage) {
    System.out.printf("[ApplicationStarter] Set default parameters for frame: \"%s\" (%dx%d px)%n",
        APP_TITLE, APP_WIDTH, APP_HEIGHT);

    stage.setTitle(APP_TITLE);

    stage.setWidth(APP_WIDTH);
    stage.setHeight(APP_HEIGHT);
    // ...
  }

}
