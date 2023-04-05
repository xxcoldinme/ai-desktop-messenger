package ru.itzstonlex.desktop.itzmsg.type.feed.controller;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.NonNull;
import ru.itzstonlex.desktop.itzmsg.form.AbstractSceneForm;
import ru.itzstonlex.desktop.itzmsg.form.controller.AbstractComponentController;
import ru.itzstonlex.desktop.itzmsg.form.controller.ControllerConfiguration;

public final class ChatBotHeaderController extends AbstractComponentController {

  public static final String USER_NAME = "user_name";
  public static final String USER_STATUS = "user_status";

  private Label username, userstatus;

  public ChatBotHeaderController(AbstractSceneForm form) {
    super(form);
  }

  @Override
  protected void initNodes(@NonNull ControllerConfiguration configuration) {
    username = configuration.getNode(USER_NAME);
    userstatus = configuration.getNode(USER_STATUS);
  }

  @Override
  protected void configureController() {
    configureDefaultDisplay();
  }

  private void configureDefaultDisplay() {
    username.setText("Chat Bot Assistant");
    username.setTextFill(Color.WHITE);

    username.setFont(Font.font(18));

    setTypingStatus(TypingStatus.AWAIT);
  }

  public void setTypingStatus(TypingStatus typingStatus) {
    typingStatus.inject(userstatus);
  }

  public enum TypingStatus {
    TYPING {
      @Override
      public void inject(Label label) {
        label.setText("Печатает...");
        label.setTextFill(Color.WHITE);
      }
    },

    ONLINE {
      @Override
      public void inject(Label label) {
        label.setText("Ожидание следующего ответа");
        label.setTextFill(Color.LIMEGREEN);
      }
    },

    AWAIT {
      @Override
      public void inject(Label label) {
        label.setText("Ожидание собеседника");
        label.setTextFill(Color.YELLOW);
      }
    },
    ;

    protected abstract void inject(Label label);
  }
}
