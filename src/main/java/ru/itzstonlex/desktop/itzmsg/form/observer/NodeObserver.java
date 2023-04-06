package ru.itzstonlex.desktop.itzmsg.form.observer;

import javafx.scene.Node;
import lombok.NonNull;
import ru.itzstonlex.desktop.itzmsg.form.ApplicationFormKeys;
import ru.itzstonlex.desktop.itzmsg.form.controller.AbstractComponentController;

public interface NodeObserver<T extends AbstractComponentController> {

  void beginObserving();

  ApplicationFormKeys.Key getExtendedFormKey();

  T getController();

  void setController(T controller);

  Node getComponent();

  void setComponent(@NonNull Node node);
}
