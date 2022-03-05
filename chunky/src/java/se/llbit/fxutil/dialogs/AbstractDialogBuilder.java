package se.llbit.fxutil.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import se.llbit.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDialogBuilder<
  Result,
  D extends Dialog<Result>,
  Builder extends AbstractDialogBuilder<Result, D, Builder>
  > {
  protected abstract Builder getThis();

  protected abstract D constructDialog();

  @Nullable
  Scene parentScene;
  @Nullable
  String title, headerText, contentText;
  List<ButtonType> buttons = new ArrayList<>();
  boolean stayOnTop = false;

  public Builder setParentScene(Scene parentScene) {
    this.parentScene = parentScene;
    return getThis();
  }

  public Builder setTitle(String title) {
    this.title = title;
    return getThis();
  }

  public Builder setHeaderText(String headerText) {
    this.headerText = headerText;
    return getThis();
  }

  public Builder setContentText(String contentText) {
    this.contentText = contentText;
    return getThis();
  }

  public Builder setButtons(ButtonType... buttons) {
    this.buttons = Arrays.asList(buttons);
    return getThis();
  }

  public Builder setStayOnTop() {
    this.stayOnTop = true;
    return getThis();
  }

  public Builder setStayOnTop(boolean stayOnTop) {
    this.stayOnTop = stayOnTop;
    return getThis();
  }

  public D build() {
    D dialog = constructDialog();
    setupParentWindow(dialog);
    setupContent(dialog);
    setupButtons(dialog);
    setupStayOnTop(dialog);
    fixTextSizingOverflow(dialog);
    return dialog;
  }

  /**
   * short for {@link #build()}, then {@link Dialog#showAndWait()}
   *
   * @return result of the dialog
   */
  public Optional<Result> showAndWait() {
    return build().showAndWait();
  }

  protected void setupParentWindow(D dialog) {
    if (parentScene != null) {
      Window parentSceneWindow = parentScene.getWindow();
      // init design to parent design
      dialog.initOwner(parentSceneWindow);

      if (parentSceneWindow instanceof Stage) {
        Stage mainWindowStage = (Stage) parentSceneWindow;
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();

        // init icon to parent icon
        dialogStage.getIcons().addAll(mainWindowStage.getIcons());
      }
    }
  }

  protected void setupContent(D dialog) {
    if (title != null)
      dialog.setTitle(title);
    if (headerText != null)
      dialog.setHeaderText(headerText);
    if (contentText != null)
      dialog.setContentText(contentText);
  }

  protected void setupButtons(D dialog) {
    if (!buttons.isEmpty()) {
      List<ButtonType> buttonList = dialog.getDialogPane().getButtonTypes();
      buttonList.clear();
      buttonList.addAll(buttons);
    }
  }

  protected void setupStayOnTop(D dialog) {
    if (stayOnTop) {
      Window window = dialog.getDialogPane().getScene().getWindow();
      if (window instanceof Stage) {
        ((Stage) window).setAlwaysOnTop(true);
        ((Stage) window).toFront();
      }
    }
  }

  /**
   * Fixes an issue with text content properly resizing on Linux in JavaFX 8.
   * Source: http://stackoverflow.com/a/33905734
   */
  protected void fixTextSizingOverflow(D dialog) {
    dialog.getDialogPane().getChildren()
      .stream()
      .filter(node -> node instanceof Label)
      .forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
  }
}
