package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import java.util.Objects;

public class SimulationParticipantDto {

    private Integer horseId;
    private Integer jockeyId;
    private Double luckFactor;

    public SimulationParticipantDto() {
    }

    public SimulationParticipantDto(Integer horseId, Integer jockeyId, Double luckFactor) {
        this.horseId = horseId;
        this.jockeyId = jockeyId;
        this.luckFactor = luckFactor;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public Integer getJockeyId() {
        return jockeyId;
    }

    public void setJockeyId(Integer jockeyId) {
        this.jockeyId = jockeyId;
    }

    public Double getLuckFactor() {
        return luckFactor;
    }

    public void setLuckFactor(Double luckFactor) {
        this.luckFactor = luckFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationParticipantDto)) return false;
        SimulationParticipantDto that = (SimulationParticipantDto) o;
        return Objects.equals(horseId, that.horseId) &&
                Objects.equals(jockeyId, that.jockeyId) &&
                Objects.equals(luckFactor, that.luckFactor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(horseId, jockeyId, luckFactor);
    }

    @Override
    public String toString() {
        return "SimulationParticipantDto{" +
                "horseId=" + horseId +
                ", jockeyId=" + jockeyId +
                ", luckFactor=" + luckFactor +
                '}';
    }

}
