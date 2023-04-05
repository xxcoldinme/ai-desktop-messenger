package ru.itzstonlex.desktop.itzmsg.form.controller.observer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObserveBy {

  Class<? extends NodeObserver<?>>[] value();
}
