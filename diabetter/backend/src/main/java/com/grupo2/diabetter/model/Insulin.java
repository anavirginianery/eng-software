package com.grupo2.diabetter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;

@Entity
public class Insulin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String type;
    private int units;
    private String horarioUuid;


    public UUID getUuid() { return uuid; }

    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getUnits() { return units; }

    public void setUnits(int units) { this.units = units; }

    public String getHorarioUuid() { return horarioUuid; }

    public void setHorarioUuid(String horarioUuid) { this.horarioUuid = horarioUuid; }
}

