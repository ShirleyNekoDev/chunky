package se.llbit.fxutil.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * Creates an alert with an extra checkbox which enables the OK button.
 * Default buttons are OK and CANCEL.
 */
public class CheckApprovalConfirmationBuilder
  extends AbstractDialogBuilder<ButtonType, Alert, CheckApprovalConfirmationBuilder>
  implements ConfirmationDialogBuilderExtension
{

  @Override
  protected CheckApprovalConfirmationBuilder getThis() {
    return this;
  }

  @Override
  protected Alert constructDialog() {
    return new Alert(Alert.AlertType.WARNING);
  }

  CheckBox checkBox = new CheckBox();

  public CheckApprovalConfirmationBuilder() {
    buttons = Arrays.asList(ButtonType.OK, ButtonType.CANCEL);
  }

  public CheckApprovalConfirmationBuilder setCheckBoxLabel(String checkBoxLabel) {
    checkBox.setText(checkBoxLabel);
    return this;
  }

  @Override
  protected void setupContent(Alert dialog) {
    final DialogPane dialogPane = dialog.getDialogPane();
//    dialogPane.getStyleClass().add("alert");
//    dialogPane.getStyleClass().add("warning");

    if(title != null)
      dialog.setTitle(title);
    if(headerText != null)
      dialog.setHeaderText(headerText);
//      dialogPane.setHeaderText(header);
    if(contentText != null)
      dialogPane.setContent(new VBox(16, new Text(contentText), checkBox));
  }

  @Override
  protected void setupButtons(Alert dialog) {
    super.setupButtons(dialog);
    dialog.getDialogPane()
      .lookupButton(ButtonType.OK)
      .disableProperty()
      .bind(checkBox.selectedProperty().not());
  }
}
