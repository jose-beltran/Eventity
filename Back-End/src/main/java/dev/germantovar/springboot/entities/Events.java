package dev.germantovar.springboot.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
@Entity
@Table(name = "evento")
//@Table(name = "servicios")
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ciudad;
    private String precio;
    private String direccion;
    private String imagenurl;
}

