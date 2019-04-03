package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationResultDto {

    private Integer id;
    private String name;
    private LocalDateTime created;
    private List<HorseJockeyCombinationDto> horseJockeyCombinations;

    public SimulationResultDto() {
    }

    public SimulationResultDto(String name, List<HorseJockeyCombinationDto> horseJockeyCombinations) {
        this.name = name;
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public SimulationResultDto(String name, LocalDateTime created) {
        this.name = name;
        this.created = created;
        this.horseJockeyCombinations = new ArrayList<>();
    }

    public SimulationResultDto(Integer id, String name, LocalDateTime created) {
        this.name = name;
        this.horseJockeyCombinations = new ArrayList<>();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<HorseJockeyCombinationDto> getHorseJockeyCombinations() {
        return horseJockeyCombinations;
    }

    public void setHorseJockeyCombinations(List<HorseJockeyCombinationDto> horseJockeyCombinations) {
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationResultDto)) return false;
        SimulationResultDto that = (SimulationResultDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(created, that.created) &&
                Objects.equals(horseJockeyCombinations, that.horseJockeyCombinations);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, created, horseJockeyCombinations);
    }

    @Override
    public String toString() {
        return "SimulationResultDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", horseJockeyCombinations=" + horseJockeyCombinations +
                '}';
    }

}
