package jp.karaden.model;

public interface ErrorInterface {
    String getCode();
    String getMessage();
    KaradenObjectInterface getErrors();
}
