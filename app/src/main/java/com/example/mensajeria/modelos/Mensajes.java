package com.example.mensajeria.modelos;

import java.io.Serializable;
import java.util.Date;

public class Mensajes {
    public String mensaje, uidRecibe, uidEnvia, remitente;

    public Mensajes(){

    }

    public Mensajes(String mensaje, String uidRecibe, String uidEnvia, String remitente) {
        this.mensaje = mensaje;
        this.uidRecibe = uidRecibe;
        this.uidEnvia = uidEnvia;
        this.remitente = remitente;
    }
}
