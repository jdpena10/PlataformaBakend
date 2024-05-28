/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class RutaEnvio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fechaYhoraRecogida;
    private String origen;
    private String destino;
    private String placa;
    private String conductor;
    private String rutaAseguir;


    public RutaEnvio() {
    }

    public RutaEnvio(String fechaYhoraRecogidaN,String origenN, String destinoN,String placaN, String conductorN, String rutaAseguirN) {
        fechaYhoraRecogida = fechaYhoraRecogidaN;
        origen = origenN;
        destino = destinoN;
        placa = placaN;
        conductor = conductorN;
        rutaAseguir = rutaAseguirN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaYhoraRecogida() {
        return fechaYhoraRecogida;
    }

    public void setFechaYhoraRecogida(String fechaYhoraRecogida) {
        this.fechaYhoraRecogida = fechaYhoraRecogida;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getRutaAseguir() {
        return rutaAseguir;
    }

    public void setRutaAseguir(String rutaAseguir) {
        this.rutaAseguir = rutaAseguir;
    }

}
