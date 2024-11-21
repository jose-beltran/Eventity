package com.eventos;

public class Evento {

    private String nombre;
    private String ciudad;
    private String precio;
    private String direccion;
    private String imagenurl;

    public Evento(String nombre, String ciudad, String precio, String direccion, String imagenurl) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.precio = precio;
        this.direccion = direccion;
        this.imagenurl = imagenurl;
    }

    public String getNombre(){ return  nombre;}
    public String getCiudad(){ return  ciudad;}
    public String getPrecio(){ return  precio;}
    public String getDireccion(){ return  direccion;}
    public String getImagenurl(){ return  imagenurl;}
}
