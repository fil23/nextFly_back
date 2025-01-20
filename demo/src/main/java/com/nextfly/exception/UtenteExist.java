package com.nextfly.exception;

public class UtenteExist extends RuntimeException {

    public UtenteExist(String msg) {
        super(msg);
        printStackTrace();
    }

    public UtenteExist() {
        super();
        printStackTrace();
    }

}
