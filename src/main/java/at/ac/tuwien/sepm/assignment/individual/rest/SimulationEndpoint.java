package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.*;
import at.ac.tuwien.sepm.assignment.individual.service.ISimulationService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationEndpoint.class);
    private static final String BASE_URL = "/api/v1/simulations";
    private final ISimulationService simulationService;
    private final SimulationMapper simulationMapper;

    @Autowired
    public SimulationEndpoint(ISimulationService simulationService, SimulationMapper simulationMapper){
        this.simulationMapper = simulationMapper;
        this.simulationService = simulationService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {

            SimulationResultDto simulationResultDto = simulationMapper.entityToDto(simulationService.findOnebyId(id));
            List<HorseJockeyCombination> horseJockeyCombinationList = null;
            horseJockeyCombinationList =
                    simulationService.getHorseJockeyCombinationById(simulationResultDto.getId());

            simulationResultDto.getHorseJockeyCombinations().addAll(simulationMapper.entityListToDto(horseJockeyCombinationList));

            return ResponseEntity.ok(simulationResultDto);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterSimulation(
            @RequestParam(value="name", required = false) String name
    ) {

        List<SimulationResultDto> simulationResultDtoList = null;
        List<HorseJockeyCombination> horseJockeyCombinationList = null;

        try {

            simulationResultDtoList = simulationMapper.entityToDtoList(simulationService.filter(name));

            for(SimulationResultDto simulationResultDto : simulationResultDtoList){

                horseJockeyCombinationList = simulationService.getHorseJockeyCombinationById(simulationResultDto.getId());

                simulationResultDto.getHorseJockeyCombinations().addAll(simulationMapper.entityListToDto(horseJockeyCombinationList));
            }

        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(simulationResultDtoList);
    }


    @GetMapping
    public ResponseEntity<?> getAllSimulations() {

        List<SimulationResultDto> simulationResultDtoList = null;
        List<HorseJockeyCombination> horseJockeyCombinationList = null;

        try {

            simulationResultDtoList = simulationMapper.entityToDtoList(simulationService.findAll());

            for(SimulationResultDto simulationResultDto : simulationResultDtoList){

                horseJockeyCombinationList = simulationService.getHorseJockeyCombinationById(simulationResultDto.getId());

                simulationResultDto.getHorseJockeyCombinations().addAll(simulationMapper.entityListToDto(horseJockeyCombinationList));
            }

        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(simulationResultDtoList);
    }

    @PostMapping
    public ResponseEntity<?> saveSimulation(@RequestBody SimulationDto simulationDtoParameter)
    {

        SimulationResultDto simulationResultDto = new SimulationResultDto();
        List<HorseJockeyCombinationDto> horseJockeyCombinationDtoList = null;
        try {

            Simulation simulation = simulationService.saveNewSimulation(simulationDtoParameter);

            simulationService.saveParticipants(simulation.getId(), simulationDtoParameter.getSimulationParticipants());

            horseJockeyCombinationDtoList = simulationMapper.entityListToDto(simulationService.runSimulation(simulation.getId()));

            simulationResultDto = simulationMapper.entityToDto(simulation);

            simulationResultDto.getHorseJockeyCombinations().addAll(horseJockeyCombinationDtoList);


        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(simulationResultDto);
    }

}
