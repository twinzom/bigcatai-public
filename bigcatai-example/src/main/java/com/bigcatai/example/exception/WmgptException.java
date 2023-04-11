package com.bigcatai.example.exception;

public class WmgptException extends Exception {

    private String wmgptErrorMessage;

    public WmgptException (String wmgptErrorMessage) {
        this.wmgptErrorMessage = wmgptErrorMessage;
    }
    
    public String getWmgptErrorMessage() {
        return wmgptErrorMessage;
    }

    public void setWmgptErrorMessage(String wmgptErrorMessage) {
        this.wmgptErrorMessage = wmgptErrorMessage;
    }
    
    
    
}
