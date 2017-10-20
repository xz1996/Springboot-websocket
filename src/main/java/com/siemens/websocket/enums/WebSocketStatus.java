package com.siemens.websocket.enums;

public enum WebSocketStatus {

    OK(0, "ok"),

    FAILED(1, "failed");

    private final int value;

    private final String reasonPhrase;

    private WebSocketStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Return the integer value of this status code.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
