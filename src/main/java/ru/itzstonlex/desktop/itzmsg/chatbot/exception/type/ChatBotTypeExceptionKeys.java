package ru.itzstonlex.desktop.itzmsg.chatbot.exception.type;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

public interface ChatBotTypeExceptionKeys {

  @Getter
  @ToString
  @EqualsAndHashCode
  @RequiredArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  class Key
  {
    String name;
  }

  Key PARAMETER_NOT_FOUND = new Key("NO_PARAMETER");

  Key EMPTY_SUGGESTIONS_RESPONSE = new Key("EMPTY_SUGGESTIONS_RESPONSE");
}
