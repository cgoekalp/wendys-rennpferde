package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseJockeyCombinationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationResultDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimulationMapper {


    public SimulationResultDto entityToDto(Simulation simulation) {

        SimulationResultDto simulationDto = new SimulationResultDto(simulation.getId(), simulation.getName(), simulation.getCreated());

        return simulationDto;
    }


    public HorseJockeyCombinationDto entityToDto(HorseJockeyCombination horseJockeyCombination) {

        HorseJockeyCombinationDto horseJockeyCombinationDto = new HorseJockeyCombinationDto(
                horseJockeyCombination.getRank(),
                horseJockeyCombination.getHorseName(),
                horseJockeyCombination.getJockeyName(),
                horseJockeyCombination.getAvgSpeed(),
                horseJockeyCombination.getHorseSpeed(),
                horseJockeyCombination.getSkill(),
                horseJockeyCombination.getLuckFactor()
        );

        return horseJockeyCombinationDto;
    }

    public List<HorseJockeyCombinationDto> entityListToDto(List<HorseJockeyCombination> horseJockeyCombinationList) {

        List<HorseJockeyCombinationDto> listDto = new ArrayList<>();

        for(HorseJockeyCombination horseJockeyCombination : horseJockeyCombinationList){

            if(horseJockeyCombination != null){
                listDto.add(entityToDto(horseJockeyCombination));
            }
        }

        return listDto;
    }

    public List<SimulationResultDto> entityToDtoList(List<Simulation> all) {

        List<SimulationResultDto> simulationResultDtoList = new ArrayList<>();

        for(Simulation simulation : all){
            simulationResultDtoList.add(entityToDto(simulation));
        }

        return simulationResultDtoList;
    }
}
