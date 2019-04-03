package at.ac.tuwien.sepm.assignment.individual.rest.validator;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;

public class SimulationDtoValidator {

    public static String isValid(SimulationDto simulationDto) {

        if (simulationDto.getName() == null || simulationDto.getName().isEmpty()) {
            return "Could not save Simulation, invalid dto provided, please check name";
        }
        return "";
    }

    public static String isValid(SimulationParticipantDto simulationParticipantDto) {

        if (simulationParticipantDto.getLuckFactor() == null
                || simulationParticipantDto.getLuckFactor() < 0.95
                || simulationParticipantDto.getLuckFactor() > 1.0) {
            return "Could not save Simulation, invalid luck factor provided";
        }
        return "";
    }
}
