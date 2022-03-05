package se.llbit.fxutil.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertDialogBuilder
  extends AbstractDialogBuilder<ButtonType, Alert, AlertDialogBuilder>
  implements ConfirmationDialogBuilderExtension
{
  private final Alert.AlertType alertType;

  public AlertDialogBuilder(Alert.AlertType alertType) {
    this.alertType = alertType;
  }

  @Override
  protected AlertDialogBuilder getThis() {
    return this;
  }

  @Override
  protected Alert constructDialog() {
    return new Alert(alertType);
  }
}
