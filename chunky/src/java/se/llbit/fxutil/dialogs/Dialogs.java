package se.llbit.fxutil.dialogs;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Dialogs {

  /**
   * Build an alert dialog which is by default always on top.
   */
  public static AlertDialogBuilder buildAlert(AlertType alertType) {
    return new AlertDialogBuilder(alertType);
  }

  /**
   * @param yesButton custom YES button
   * @param noButton  custom CANCEL button
   * @return Dialog builder for an alert with custom YES and custom CANCEL button.
   */
  public static AlertDialogBuilder buildConfirmationDialog(ButtonType yesButton, ButtonType noButton) {
    return buildAlert(AlertType.CONFIRMATION).setButtons(yesButton, noButton);
  }

  /**
   * @param yesButtonLabel custom YES button label
   * @return Dialog builder for an alert with custom YES and default CANCEL button.
   */
  public static AlertDialogBuilder buildConfirmationDialog(String yesButtonLabel) {
    return buildConfirmationDialog(new ButtonType(yesButtonLabel, ButtonBar.ButtonData.YES));
  }

  /**
   * @param yesButton custom YES button
   * @return Dialog builder for an alert with custom YES and default CANCEL button.
   */
  public static AlertDialogBuilder buildConfirmationDialog(ButtonType yesButton) {
    return buildConfirmationDialog(yesButton, ButtonType.CANCEL);
  }

  /**
   * @return Dialog builder for an alert with default YES and default CANCEL button.
   */
  public static AlertDialogBuilder buildConfirmationDialog() {
    return buildConfirmationDialog(ButtonType.YES);
  }

  public static CheckApprovalConfirmationBuilder buildCheckApprovalConfirmationDialog() {
    return new CheckApprovalConfirmationBuilder();
  }

}
