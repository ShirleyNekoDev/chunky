/*
 * Copyright (c) 2016 Jesper Öqvist <jesper@llbit.se>
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
package se.llbit.chunky.ui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se.llbit.chunky.main.SceneHelper;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.fxutil.Dialogs;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonParser;
import se.llbit.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SceneChooserController implements Initializable {
  @FXML private TableView<SceneListItem> sceneTbl;

  @FXML private TableColumn<SceneListItem, String> nameCol;

  @FXML private TableColumn<SceneListItem, Number> chunkCountCol;

  @FXML private TableColumn<SceneListItem, String> sizeCol;

  @FXML private TableColumn<SceneListItem, Number> sppCol;

  @FXML private TableColumn<SceneListItem, String> renderTimeCol;

  @FXML private Button loadSceneBtn;

  @FXML private Button cancelBtn;

  @FXML private Button exportBtn;

  @FXML private Button deleteBtn;

  @FXML private TextField searchBox;

  private Stage stage;

  private ChunkyFxController controller;

  @Override public void initialize(URL location, ResourceBundle resources) {
    exportBtn.setTooltip(new Tooltip("Exports the selected scene as a Zip archive."));
    exportBtn.setOnAction(e -> {
      if(!sceneTbl.getSelectionModel().isEmpty()) {
        SceneListItem scene = sceneTbl.getSelectionModel().getSelectedItem();
        if(scene.sceneName.isEmpty()) {
          Log.error("Can not export scene with unknown filename.");
          return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Scene");
        fileChooser
                .getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip files", "*.zip"));
        fileChooser.setInitialFileName(String.format("%s.zip", scene.sceneName));
        File targetFile = fileChooser.showSaveDialog(stage);
        if(targetFile != null) {
          Scene.exportToZip(scene.sceneName, targetFile);
        }
      }
    });
    deleteBtn.setOnAction(e -> {
      if(!sceneTbl.getSelectionModel().isEmpty()) {
        SceneListItem scene = sceneTbl.getSelectionModel().getSelectedItem();
        if(scene.sceneName.isEmpty()) {
          Log.error("Can not delete scene with unknown filename.");
          return;
        }
        Alert alert = Dialogs.createAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Scene");
        alert.setContentText(String.format("Are you sure you want to delete the scene %s? "
                + "All files for the scene, except snapshot images, will be deleted.", scene.sceneName));
        if(alert.showAndWait().get() == ButtonType.OK) {
          Scene.delete(scene.sceneName, scene.sceneDirectory);
          sceneTbl.getItems().remove(sceneTbl.getSelectionModel().getSelectedItem());
        }
      }
    });
    nameCol.setCellValueFactory(data -> {
      SceneListItem scene = data.getValue();
      if(scene.isBackup) {
        return new ReadOnlyStringWrapper(scene.sceneName + " [backup]");
      } else {
        return new ReadOnlyStringWrapper(scene.sceneName);
      }
    });
    chunkCountCol.setCellValueFactory(data -> {
      SceneListItem scene = data.getValue();
      return new ReadOnlyIntegerWrapper(scene.chunkSize);
    });
    sizeCol.setCellValueFactory(data -> {
      SceneListItem scene = data.getValue();
      return new ReadOnlyStringWrapper(scene.dimensions);
    });
    sppCol.setCellValueFactory(data -> {
      SceneListItem scene = data.getValue();
      return new ReadOnlyIntegerWrapper(scene.sppCount);
    });
    renderTimeCol.setCellValueFactory(data -> {
      SceneListItem scene = data.getValue();
      return new ReadOnlyStringWrapper(scene.renderTime);
    });

    searchBox.setOnKeyTyped(evt -> {
      String searchString = (searchBox.getText() + evt.getCharacter()).trim();
      if(!searchString.isEmpty()) {
        Platform.runLater(() -> {
          Predicate<String> regex = Pattern.compile(Pattern.quote(searchString)).asPredicate();
          List<SceneListItem> items = sceneTbl.getItems();
          items.forEach(item -> {
            item.isSearchMatch = regex.test(item.sceneName);
          });
          items.sort(Comparator.comparing(item -> !item.isSearchMatch));
          sceneTbl.scrollTo(0);
        });
      } else {
        sceneTbl.sort();
      }
    });
  }

  public void setStage(Stage stage) {
    this.stage = stage;
    sceneTbl.setRowFactory(tbl -> {
      TableRow<SceneListItem> row = new TableRow<>();
      row.setOnMouseClicked(e -> {
        if(e.getClickCount() == 2 && !row.isEmpty()) {
          SceneListItem scene = row.getItem();
          if(scene.sceneName.isEmpty()) {
            Log.error("Can't load scene with unknown filename.");
          } else {
            controller.loadScene(scene.sceneName);
            e.consume();
            stage.close();
          }
        }
      });
      return row;
    });
    loadSceneBtn.setOnAction(e -> {
      if(!sceneTbl.getSelectionModel().isEmpty()) {
        SceneListItem scene = sceneTbl.getSelectionModel().getSelectedItem();
        if(scene.sceneName.isEmpty()) {
          Log.error("Can't load scene with unknown filename.");
        } else {
          controller.loadScene(scene.sceneName);
          stage.close();
        }
      }
    });
    cancelBtn.setOnAction(e -> {
      sceneTbl.getItems().clear();
      stage.hide();
    });
  }

  private void populateSceneTable(File sceneDir) {
    List<SceneListItem> scenes = new ArrayList<>();
    List<File> fileList = SceneHelper.getAvailableSceneFiles(sceneDir);
    Collections.sort(fileList);
    for(File sceneFile : fileList) {

      try(JsonParser parser = new JsonParser(new FileInputStream(new File(sceneFile.getParentFile(), sceneFile.getName())))) {
        SceneListItem item = new SceneListItem(parser.parse().object(), sceneFile);
        scenes.add(item);

      } catch(IOException | JsonParser.SyntaxError e) {
        Log.warnf("Warning: could not load scene description: %s", sceneFile.getName());
      }
    }
    sceneTbl.setItems(FXCollections.observableArrayList(scenes));
    if(!scenes.isEmpty()) {
      sceneTbl.getSelectionModel().select(0);
    }
  }

  public void setController(ChunkyFxController controller) {
    this.controller = controller;

    populateSceneTable(controller.getChunky().options.sceneDir);
  }

  private static class SceneListItem {
    final String sceneName;
    final int chunkSize;
    final String dimensions;
    final int sppCount;
    final String renderTime;
    /**
     * What folder the scene is in
     */
    final File sceneDirectory;
    /**
     * Whether this scene description file is a backup file and the original .json is missing.
     */
    final boolean isBackup;

    boolean isSearchMatch = true;

    SceneListItem(JsonObject scene, File sceneFile) {
      sceneName = sceneFile.getName().substring(0, sceneFile.getName().length() - (
              sceneFile.getName().endsWith(".backup") ? 7 + Scene.EXTENSION.length()
                      : Scene.EXTENSION.length()));
      sceneDirectory = sceneFile.getParentFile();
      chunkSize = scene.get("chunkList").array().size();
      dimensions = String.format("%sx%s", scene.get("width").intValue(400), scene.get("height").intValue(400));
      sppCount = scene.get("spp").intValue(0);
      isBackup = sceneFile.getName().endsWith(".backup");

      long renderTime = scene.get("renderTime").longValue(0);
      int seconds = (int) ((renderTime / 1000) % 60);
      int minutes = (int) ((renderTime / 60000) % 60);
      int hours = (int) (renderTime / 3600000);
      this.renderTime = String.format("%d:%d:%d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
      return String.format("Name:%s, Chunks:%d, Size:%s, Spp:%d, Time:%s, Location:%s", sceneName, chunkSize, dimensions, sppCount, renderTime, sceneDirectory.getName());
    }
  }

}
