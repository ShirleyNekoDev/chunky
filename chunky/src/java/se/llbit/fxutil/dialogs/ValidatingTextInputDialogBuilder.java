package se.llbit.fxutil.dialogs;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.function.Predicate;

public class ValidatingTextInputDialogBuilder
  extends AbstractDialogBuilder<String, TextInputDialog, ValidatingTextInputDialogBuilder> {

  @Override
  protected ValidatingTextInputDialogBuilder getThis() {
    return this;
  }

  @Override
  protected TextInputDialog constructDialog() {
    return new TextInputDialog();
  }

  Predicate<String> validator;

  public ValidatingTextInputDialogBuilder setValidator(Predicate<String> validator) {
    this.validator = validator;
    return this;
  }

  @Override
  protected void setupButtons(TextInputDialog dialog) {
    super.setupButtons(dialog);
    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    okButton.addEventFilter(ActionEvent.ACTION, event -> {
      String currentInput = dialog.getEditor().getText();
      if(!validator.test(currentInput)) {
        event.consume();
        // show invalid
      }
    });
  }
}
