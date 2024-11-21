package com.eventos;

public class City {

    private String departamento;
    private String ciudad;
    private String imagenUrl;


    public City(String departamento, String ciudad, String imagenUrl) {
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.imagenUrl = imagenUrl;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

}


