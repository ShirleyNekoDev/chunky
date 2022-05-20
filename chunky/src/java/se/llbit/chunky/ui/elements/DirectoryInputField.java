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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import se.llbit.fxutil.Dialogs;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class DirectoryInputField extends GridPane {

  // TODO: save the history (textfield -> https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm)
  public final ObjectProperty<File> directoryProperty = new SimpleObjectProperty<>();

  protected TextField directoryDisplayField = new TextField();
  protected Button openChangeDialogButton = new Button("Browse");
  protected Button openExplorerAtDirectoryButton = new Button("Open");

  protected DirectoryChooser directoryChooser = new DirectoryChooser();
  protected DirectoryValidator directoryValidator = new DirectoryValidator() {};

  interface DirectoryValidator {
    default boolean isValid(File file) {
      return file.canWrite();
    }

    /**
     * Will be called if the directory is not valid.
     * This can be used as feedback to the user about the invalid selection.
     *
     * @return true if the directory selection dialog should be shown again
     */
    default boolean shouldReshowDialog() {
      Alert reshowChooserConfirmation = Dialogs.createAlert(Alert.AlertType.WARNING);
      reshowChooserConfirmation.setContentText("Please select a different directory.");
      reshowChooserConfirmation.getButtonTypes().setAll(
        ButtonType.OK,
        ButtonType.CANCEL
      );
      reshowChooserConfirmation.setTitle("Invalid directory");
      ButtonType result = reshowChooserConfirmation.showAndWait().orElse(ButtonType.CANCEL);
      return result == ButtonType.OK;
    }
  }

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

  /**
   * @return this field's set directory
   */
  public File getDirectory() {
    return directoryProperty.getValue();
  }

  /**
   * Open this field's set directory in the system's file browser.
   */
  public void openDirectoryInFileBrowser() {
    // Running Desktop.open() on the JavaFX application thread seems to
    // lock up the application on Linux, so we create a new thread to run that.
    // This StackOverflow question seems to ask about the same bug:
    // http://stackoverflow.com/questions/23176624/javafx-freeze-on-desktop-openfile-desktop-browseuri
    new Thread(() -> {
      try {
        Desktop.getDesktop().open(directoryProperty.get());
      } catch (IOException ex) {
        throw new RuntimeException("Failed to open directory in system file browser.", ex);
      }
    }, "Desktop-FileBrowser-Thread").start();
  }

  /**
   * Shows a dialog to change this field's set directory.
   * Will validate and retry the selected directory until it is deemed valid
   * or the change is cancelled.
   */
  public void promptDirectoryChange() {
    while(true) {
      File chosenDirectory = openDirectoryChooserDialog();

      if(chosenDirectory == null)
        return; // dialog aborted, no change

      if (!directoryValidator.isValid(chosenDirectory)) {
        // invalid directory - ask to choose again
        // TODO: set initial directory?
        if (directoryValidator.shouldReshowDialog())
          continue;
      }

      directoryProperty.set(chosenDirectory);
      return;
    }
  }

  protected File openDirectoryChooserDialog() {
    directoryChooser.setInitialDirectory(directoryProperty.get());
    return directoryChooser.showDialog(this.getScene().getWindow());
  }
}
