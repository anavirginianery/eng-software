package com.grupo2.diabetter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Insulin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String uuid;
    private String type; // Enum seria implementado com uma classe Enum específica
    private int units;
    private String horarioUuid;

    // Getters e Setters
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }
    public String getHorarioUuid() { return horarioUuid; }
    public void setHorarioUuid(String horarioUuid) { this.horarioUuid = horarioUuid; }
}

