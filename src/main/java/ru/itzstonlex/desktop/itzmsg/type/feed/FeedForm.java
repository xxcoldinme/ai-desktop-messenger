package ru.itzstonlex.desktop.itzmsg.type.feed;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ru.itzstonlex.desktop.itzmsg.chatbot.ChatBotAssistant;
import ru.itzstonlex.desktop.itzmsg.form.function.FormFunctionReleaser;
import ru.itzstonlex.desktop.itzmsg.type.feed.controller.ChatBotHeaderController;
import ru.itzstonlex.desktop.itzmsg.type.feed.controller.BothMessagesReceiveController;
import ru.itzstonlex.desktop.itzmsg.form.AbstractSceneForm;
import ru.itzstonlex.desktop.itzmsg.form.FormKeys;
import ru.itzstonlex.desktop.itzmsg.form.usecase.FormUsecase;
import ru.itzstonlex.desktop.itzmsg.form.usecase.FormUsecaseKeys;
import ru.itzstonlex.desktop.itzmsg.type.feed.function.FeedFormFunctionReleaser;

public final class FeedForm extends AbstractSceneForm {

  private static final double WIDTH_CONST = 675;
  private static final double HEIGHT_CONST = 870;

  @FXML
  private Label username;

  @FXML
  private Label userStatus;

  @FXML
  private TextField messageField;

  @FXML
  private VBox messagesBox;

  @FXML
  private Button sendButton;

  @FXML
  private Button settingsButton;

  @FXML
  private Button clearButton;

  @FXML
  private AnchorPane firstMessageAnnotationPanel;

  public FeedForm() {
    super(FormKeys.FEED);
  }

  @Override
  public FormFunctionReleaser<?> newFunctionReleaser() {
    return new FeedFormFunctionReleaser();
  }

  @Override
  public void initializeUsecase(FormUsecase usecase) {
    usecase.set(FormUsecaseKeys.FRAME_RESIZABLE_DISABLE_FLAG, true);

    usecase.set(FormUsecaseKeys.CUSTOM_WIDTH, WIDTH_CONST);
    usecase.set(FormUsecaseKeys.CUSTOM_HEIGHT, HEIGHT_CONST);
  }

  @Override
  public void initializeControllers() {
    ChatBotAssistant chatBotAssistant = new ChatBotAssistant();

    ChatBotHeaderController botUserController = new ChatBotHeaderController(this);
    botUserController.with(ChatBotHeaderController.USER_NAME, username)
        .with(ChatBotHeaderController.USER_STATUS, userStatus);

    BothMessagesReceiveController messagesController = new BothMessagesReceiveController(this, chatBotAssistant);
    messagesController.with(BothMessagesReceiveController.MESSAGE_FIELD, messageField)
            .with(BothMessagesReceiveController.MESSAGES_BOX, messagesBox)
            .with(BothMessagesReceiveController.SEND_BUTTON, sendButton)
            .with(BothMessagesReceiveController.FIRST_MSG_ANNOTATION, firstMessageAnnotationPanel);

    this.addController(botUserController);
    this.addController(messagesController);
  }
}
