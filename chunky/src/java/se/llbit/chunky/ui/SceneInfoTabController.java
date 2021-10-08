package se.llbit.chunky.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.world.ChunkPosition;

import java.io.IOException;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

/**
 * @author Maximilian Stiede
 */
public class SceneInfoTabController {

  @FXML private TextField sceneDimensions;
  @FXML private TextField chunkCount;
  @FXML private TextField entityCount;
  @FXML private TextField actorCount;

  public SceneInfoTabController(Tab tab) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneInfoTab.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    tab.setContent(root);
  }

  public void refresh(Scene scene) {
    Collection<ChunkPosition> chunks = scene.getChunks();
    if(!chunks.isEmpty()) {
      IntSummaryStatistics xStats = chunks.stream()
        .mapToInt(chunkPosition -> chunkPosition.x << 4).summaryStatistics();
      IntSummaryStatistics zStats = chunks.stream()
        .mapToInt(chunkPosition -> chunkPosition.z << 4).summaryStatistics();
      sceneDimensions.setText(String.format(
        "(%d,%d,%d) to (%d,%d,%d)",
        xStats.getMin(),
        scene.getYClipMin(),
        zStats.getMin(),
        xStats.getMax(),
        scene.getYClipMax(),
        zStats.getMax()
      ));
    } else {
      sceneDimensions.setText("(0,0,0) to (0,0,0)");
    }

    chunkCount.setText(String.valueOf(chunks.size()));
    entityCount.setText(String.valueOf(scene.getEntities().size()));
    actorCount.setText(String.valueOf(scene.getActors().size()));
  }
}
