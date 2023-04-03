package com.bigcatai.matchingengine.model;

public class BigCatAiException extends Exception {

    private String wmgptErrorMessage;

    public BigCatAiException (String wmgptErrorMessage) {
        this.wmgptErrorMessage = wmgptErrorMessage;
    }
    
    public String getWmgptErrorMessage() {
        return wmgptErrorMessage;
    }

    public void setWmgptErrorMessage(String wmgptErrorMessage) {
        this.wmgptErrorMessage = wmgptErrorMessage;
    }
    
    
    
}
