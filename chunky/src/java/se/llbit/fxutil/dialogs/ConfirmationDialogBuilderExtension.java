package se.llbit.fxutil.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Optional;

public interface ConfirmationDialogBuilderExtension {
  /**
   * {@link Dialog#showAndWait()}
   * @return result of the dialog
   */
  Optional<ButtonType> showAndWait();

  /**
   * Show dialog and wait for user response.
   * @return True if the user confirmed by pressing YES, any other interaction results in false.
   */
  default boolean showAndConfirm() {
    return showAndConfirm(ButtonBar.ButtonData.YES);
  }

  /**
   * Show dialog and wait for user response.
   * @param confirmation button data which shall be associated with the confirmation
   * @return True if the user confirmed by pressing the button associated with confirmation, any other interaction results in false.
   */
  default boolean showAndConfirm(ButtonBar.ButtonData confirmation) {
    return showAndWait()
      .orElse(ButtonType.CANCEL)
      .getButtonData()
      .equals(confirmation);
  }
}
