package com.example.mensajeria.modelos;

import android.net.Uri;


public class Usuario{
    public String nombre;
    public String email;
    public String contraseña;
    public String telefono;
    public String providers;
    public String uid;
    public String fotoURL;

    public Usuario(String nombre, String email, String contraseña, String telefono, String providers, String uid, String fotoURL) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.providers = providers;
        this.uid = uid;
        this.fotoURL = fotoURL;
    }

    public Usuario() {

    }

    public Usuario(String uid, String email, String contraseña) {
        this.uid = uid;
        this.email = email;
        this.contraseña = contraseña;
        nombre = "";
    }

    public String toString(){
        return "Nombre : " + nombre +
                "Email : " + email +
                "Contraseña" + contraseña +
                "Uid : " + uid +
                "Poveedor : " + providers;
    }

}
