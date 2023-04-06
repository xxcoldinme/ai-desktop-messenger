package ru.itzstonlex.desktop.itzmsg.form.type.message.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.TimeUnit;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.NonNull;
import ru.itzstonlex.desktop.itzmsg.form.AbstractSceneForm;
import ru.itzstonlex.desktop.itzmsg.form.controller.ControllerConfiguration;
import ru.itzstonlex.desktop.itzmsg.form.controller.AbstractComponentController;
import ru.itzstonlex.desktop.itzmsg.form.function.FormFunctionReleaser;
import ru.itzstonlex.desktop.itzmsg.utility.Schedulers;

public final class CopyActionLabelController extends AbstractComponentController {

  public static final String NAME = "name";
  public static final String MESSAGE_TEXT = "text";

  private Label actionLabel;
  private Label messageLabel;

  public CopyActionLabelController(AbstractSceneForm<?> form) {
    super(form);
  }

  @Override
  protected void initNodes(@NonNull ControllerConfiguration configuration) {
    actionLabel = configuration.getNode(NAME);
    messageLabel = configuration.getNode(MESSAGE_TEXT);
  }

  @Override
  protected void configureController() {
    actionLabel.setCursor(Cursor.HAND);
    actionLabel.setOnMouseClicked(event -> {

      copyMessageToBuffer();
      changeToCopiedStatus();

      resetMouseClickedEvent();
    });
  }

  public void copyMessageToBuffer() {
    StringSelection stringSelection =
        new StringSelection(messageLabel.getText());

    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  public void changeToCopiedStatus() {
    actionLabel.setUnderline(false);

    actionLabel.setText("Copied");
    actionLabel.setTextFill(Color.BLACK);

    Schedulers.lateSync(TimeUnit.SECONDS, 2, this::resetDisplayToDefault);
  }

  private void resetDisplayToDefault() {
    actionLabel.setUnderline(true);

    actionLabel.setText("Copy");
    actionLabel.setTextFill(Color.web("#000fff"));

    configureController();
  }

  private void resetMouseClickedEvent() {
    actionLabel.setCursor(Cursor.DEFAULT);
    actionLabel.setOnMouseClicked(null);
  }
}