package jp.karaden.param.message;

import jp.karaden.exception.InvalidParamsException;

public abstract class MessageParams {
    public final static String CONTEXT_PATH = "/messages";
    public void validate() throws InvalidParamsException { }
}
