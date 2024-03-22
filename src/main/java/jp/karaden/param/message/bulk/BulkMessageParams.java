package jp.karaden.param.message.bulk;

import jp.karaden.exception.InvalidParamsException;

public class BulkMessageParams {
    public final static String CONTEXT_PATH = "/messages/bulks";
    public BulkMessageParams validate() throws InvalidParamsException { return this; }
}
