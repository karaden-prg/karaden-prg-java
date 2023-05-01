package jp.karaden.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ErrorTest {
    @Test
    void codeを出力できる() {
        String value = "code";
        Error error = new Error();
        error.setProperty("code", value);
        assertEquals(value, error.getCode());
    }

    @Test
    void messageを出力できる() {
        String value = "message";
        Error error = new Error();
        error.setProperty("message", value);
        assertEquals(value, error.getMessage());
    }

    @Test
    void errorsを出力できる() {
        KaradenObject value = new KaradenObject();
        Error error = new Error();
        error.setProperty("errors", value);
        assertInstanceOf(KaradenObject.class, error.getErrors());
    }
}