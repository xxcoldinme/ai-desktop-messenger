package ru.itzstonlex.desktop.chatbotmessenger.form.feed.controller;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.itzstonlex.desktop.chatbotmessenger.api.chatbot.ChatBotAssistant;
import ru.itzstonlex.desktop.chatbotmessenger.api.chatbot.type.request.ChatBotRequest;
import ru.itzstonlex.desktop.chatbotmessenger.api.form.AbstractSceneForm;
import ru.itzstonlex.desktop.chatbotmessenger.api.form.ApplicationFormKeys;
import ru.itzstonlex.desktop.chatbotmessenger.api.form.controller.AbstractComponentController;
import ru.itzstonlex.desktop.chatbotmessenger.api.form.observer.ObserveBy;
import ru.itzstonlex.desktop.chatbotmessenger.form.feed.FeedForm;
import ru.itzstonlex.desktop.chatbotmessenger.form.feed.controller.ChatBotHeaderController.TypingStatus;
import ru.itzstonlex.desktop.chatbotmessenger.form.feed.function.FeedFormFunctionReleaser;
import ru.itzstonlex.desktop.chatbotmessenger.form.feed.view.FeedFormFrontView;
import ru.itzstonlex.desktop.chatbotmessenger.form.feed.view.FeedFormFrontViewConfiguration;
import ru.itzstonlex.desktop.chatbotmessenger.form.message.MessageForm;
import ru.itzstonlex.desktop.chatbotmessenger.form.message.function.MessageFormFunctionReleaser.SenderType;
import ru.itzstonlex.desktop.chatbotmessenger.observer.FooterButtonSendClickObserver;
import ru.itzstonlex.desktop.chatbotmessenger.observer.FooterMessageInputEnterObserver;

public final class BothMessagesReceiveController extends AbstractComponentController<FeedForm> {

  @ObserveBy(FooterMessageInputEnterObserver.class)
  private TextField inputMessageField;

  @ObserveBy(FooterButtonSendClickObserver.class)
  private Button messageSendButton;

  @Getter
  private final List<Node> messageNodesList = new ArrayList<>();

  @Getter
  private final ChatBotAssistant chatBotAssistant;

  public BothMessagesReceiveController(FeedForm form, ChatBotAssistant chatBotAssistant) {
    super(form);
    this.chatBotAssistant = chatBotAssistant;
  }

  @Override
  protected void configureController() {
    this.inputMessageField = getForm().getView().find(FeedFormFrontViewConfiguration.INPUT_MESSAGE_FIELD);
    this.messageSendButton = getForm().getView().find(FeedFormFrontViewConfiguration.MESSAGE_SEND_BUTTON);
  }

  private String reformatMessage(String text) {
    return text.replace("<br>", "\n");
  }

  private final OpenAiService chatGPT = new OpenAiService("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", 5);

  public void onMessageReceive(@NonNull String receivedMessage) {
    ChatBotHeaderController chatBotHeaderController = getForm().getController(ChatBotHeaderController.class);

    // send question
    fireFunction(FeedFormFunctionReleaser.SEND, receivedMessage);
    chatBotHeaderController.setTypingStatus(TypingStatus.TYPING);

    // send answer
    CompletableFuture.supplyAsync(() -> chatGPT.createCompletion(CompletionRequest.builder()
            .model("text-davinci-003")
            .prompt(receivedMessage)
            .temperature(0.9)
            .maxTokens(500)
            .topP(1.0)
            .frequencyPenalty(0.0)
            .presencePenalty(0.6)
            .build()))
        .whenCompleteAsync((completionResult, error) -> fireFunction(FeedFormFunctionReleaser.REPLY,
            completionResult.getChoices().get(0).getText()));
    // ChatBotRequest chatBotRequest = new ChatBotRequest(receivedMessage);
    // chatBotAssistant.requestBestSuggestion(chatBotRequest)
    //     .thenAccept(response -> fireFunction(FeedFormFunctionReleaser.REPLY, response.getMessageText()));

    // hide suggestions.
    FooterSuggestionsController footerSuggestionsController = getForm().getController(FooterSuggestionsController.class);
    footerSuggestionsController.setSuggestionsVisible(false);
  }

  @SneakyThrows
  private Node createMessageNode(SenderType senderType, String msg) {
    AbstractSceneForm<?> messageForm = getForm().getSceneLoader()
        .loadUncachedForm(ApplicationFormKeys.MESSAGE);

    // todo - replace to function MessageFormFunctionReleaser.UPDATE_MESSAGE_TEXT
    ((MessageForm) messageForm).updateMessageText(senderType, msg);

    return messageForm.getJavafxNode();
  }

  private void addMessageChildren(ObservableList<Node> childrenList, Node messageNode) {
    FeedFormFrontView view = getForm().getView();

    Node wrappedMessageNode = view.createWrappedMessageNode(messageNode);

    childrenList.add(wrappedMessageNode);
    childrenList.add(view.createMessageEmptySeparator(10));

    messageNodesList.add(wrappedMessageNode);
  }

  public void addMessage(SenderType senderType, String text) {
    VBox messagesBox = getForm().getView().find(FeedFormFrontViewConfiguration.MESSAGES_DISPLAY_BOX);
    Node messageNode = createMessageNode(senderType, reformatMessage(text));

    addMessageChildren(messagesBox.getChildren(), messageNode);

    AnchorPane noMessagesPanel = getForm().getView().find(FeedFormFrontViewConfiguration.NO_MESSAGES_PANEL);

    if (noMessagesPanel.isVisible()) {
      noMessagesPanel.setVisible(false);
    }
  }
}
