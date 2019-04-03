package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationDto {

    private String name;
    private List<SimulationParticipantDto> simulationParticipants;

    public SimulationDto() {
    }

    public SimulationDto(String name, List<SimulationParticipantDto> simulationParticipants) {
        this.name = name;
        this.simulationParticipants = simulationParticipants;
    }

    public SimulationDto(String name) {
        this.name = name;
        this.simulationParticipants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimulationParticipantDto> getSimulationParticipants() {
        return simulationParticipants;
    }

    public void setSimulationParticipants(List<SimulationParticipantDto> simulationParticipants) {
        this.simulationParticipants = simulationParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationDto)) return false;
        SimulationDto that = (SimulationDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(simulationParticipants, that.simulationParticipants);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, simulationParticipants);
    }

    @Override
    public String toString() {
        return "SimulationInputDto{" +
                "name='" + name + '\'' +
                ", simulationParticipants=" + simulationParticipants +
                '}';
    }

}
