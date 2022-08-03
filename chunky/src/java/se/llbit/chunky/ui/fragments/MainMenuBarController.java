package se.llbit.chunky.ui.fragments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuBarController implements Initializable {

  @FXML private MenuItem menuExit;
  @FXML private MenuItem saveScene;
  @FXML private MenuItem saveSceneAs;
  @FXML private MenuItem saveSceneCopy;
  @FXML private MenuItem loadScene;
  @FXML private MenuItem loadSceneFile;
  @FXML private MenuItem creditsMenuItem;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
