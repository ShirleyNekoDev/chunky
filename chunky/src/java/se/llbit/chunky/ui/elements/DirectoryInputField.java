/* Copyright (c) 2022 Chunky contributors
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.llbit.chunky.ui.elements;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import se.llbit.fxutil.Dialogs;
import se.llbit.log.Log;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

/**
 *
 */
public class DirectoryInputField extends GridPane {

  public ObjectProperty<File> directoryProperty = new SimpleObjectProperty<>();

  protected TextField directoryDisplayField = new TextField();
  public Button openChangeDialogButton = new Button("Browse");
  public Button openExplorerAtDirectoryButton = new Button("Open");

  protected DirectoryChooser directoryChooser = new DirectoryChooser();
  protected Predicate<File> directoryValidator = File::canWrite;

  public DirectoryInputField() {
    this(new File(System.getProperty("user.home")));
  }

  public DirectoryInputField(File initialDirectory) {
    setDirectory(initialDirectory);

    directoryDisplayField.textProperty().bind(directoryProperty.asString());
    directoryDisplayField.setEditable(false);
    directoryDisplayField.setOnMouseClicked(event -> promptDirectoryChange());
    add(directoryDisplayField, 0, 0);

    openChangeDialogButton.setTooltip(new Tooltip("Choose a new directory"));
    openChangeDialogButton.setOnMouseClicked(event -> promptDirectoryChange());
    add(openChangeDialogButton, 1, 0);

    if(Desktop.isDesktopSupported()) {
      openExplorerAtDirectoryButton.setTooltip(new Tooltip("Open directory in system file browser"));
      openExplorerAtDirectoryButton.setOnMouseClicked(event -> openDirectoryInFileBrowser());
      add(openExplorerAtDirectoryButton, 2, 0);
    } else {
      openExplorerAtDirectoryButton.setDisable(true);
    }
  }

  public void setDirectory(File directory) {
    if(!directory.isDirectory()) {
      throw new IllegalArgumentException("File required to be a directory");
    }
    directoryProperty.set(directory);
  }

  public void openDirectoryInFileBrowser() {
    // Running Desktop.open() on the JavaFX application thread seems to
    // lock up the application on Linux, so we create a new thread to run that.
    // This StackOverflow question seems to ask about the same bug:
    // http://stackoverflow.com/questions/23176624/javafx-freeze-on-desktop-openfile-desktop-browseuri
    new Thread(() -> {
      try {
        Desktop.getDesktop().open(directoryProperty.get());
      } catch (IOException ex) {
        Log.warn("Failed to open directory in system file browser.", ex);
      }
    }).start();
  }

  public void promptDirectoryChange() {
    File chosenDirectory = openDirectoryChooserDialog();
    if(chosenDirectory != null) {
      if(directoryValidator.test(chosenDirectory)) {
        directoryProperty.set(chosenDirectory);
      } else {
        // invalid directory - ask to choose again
        Alert warning = Dialogs.createAlert(Alert.AlertType.CONFIRMATION);
        warning.setContentText("The selected chunks need to be reloaded in order for emitter sampling to work.");
//        warning.getButtonTypes().setAll(
//          ButtonType.CANCEL,
//          new ButtonType("Reload chunks", ButtonBar.ButtonData.FINISH));
        warning.setTitle("Chunk reload required");
        ButtonType result = warning.showAndWait().orElse(ButtonType.CANCEL);
        if (result.getButtonData() == ButtonBar.ButtonData.FINISH) {

        }
      }
    }
  }

  protected File openDirectoryChooserDialog() {
    directoryChooser.setInitialDirectory(directoryProperty.get());
    return directoryChooser.showDialog(this.getScene().getWindow());
  }
}
