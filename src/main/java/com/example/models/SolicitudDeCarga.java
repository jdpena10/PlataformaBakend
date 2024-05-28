/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import java.io.Serializable;
import javax.persistence.*;


@Entity
public class SolicitudDeCarga implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fecha;
    private String propietarioCarga;
    private String origen;
    private String destino;
    private String dimensiones;
    private int peso;
    private int valorAsegurado;
    private String empaque;


    public SolicitudDeCarga() {
    }

    public SolicitudDeCarga(String fechaN,String propietarioCargaN,String origenN, String destinoN, String dimensionesN, int pesoN,int valorAseguradoN, String empaqueN) {
        fecha = fechaN;
        propietarioCarga = propietarioCargaN;
        origen = origenN;
        destino = destinoN;
        dimensiones = dimensionesN;
        peso = pesoN;
        valorAsegurado = valorAseguradoN;
        empaque = empaqueN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPropietarioCarga() {
        return propietarioCarga;
    }

    public void setPropietarioCarga(String propietarioCarga) {
        this.propietarioCarga = propietarioCarga;
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

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getValorAsegurado() {
        return valorAsegurado;
    }

    public void setValorAsegurado(int valorAsegurado) {
        this.valorAsegurado = valorAsegurado;
    }

    public String getEmpaque() {
        return empaque;
    }

    public void setEmpaque(String empaque) {
        this.empaque = empaque;
    }
    
}    
