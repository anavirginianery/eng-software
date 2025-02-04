package com.grupo2.diabetter.dto.insulin;

public class InsulinPostPutRequestDTO {
    private String type;
    private int units;
    private String horarioUuid;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getHorarioUuid() {
        return horarioUuid;
    }

    public void setHorarioUuid(String horarioUuid) {
        this.horarioUuid = horarioUuid;
    }
}
