package com.accenture.salvo.model;

public final class Consts {

    public static final String ERROR = "ERROR";
    public static final String ID = "gpid";
    public static final String SHIP = "ship";
    public static final String SALVO = "salvoes";
    public static final String CREATE = "OK";
    public static final String UNAUTHORIZED = "No autorizado";
    public static final String FORBIDDEN = "No es posible entrar";
    public static final String BAD_REQUEST = "La información es incorrecta";
    public static final String CONFLICT = "El nombre ya existe";
    public static final String SUCCESS_SHIPS = "Ships agregados";
    public static final String SUCCESS_SALVOES = "Salvo agregado";
    public static final String SUCCESS_PLAYER = "Usuario creado correctamente";

    private Consts(){
        throw new AssertionError();
    }
}
