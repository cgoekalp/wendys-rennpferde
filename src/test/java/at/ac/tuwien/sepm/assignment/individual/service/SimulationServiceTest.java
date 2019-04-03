package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Simulation;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationParticipantDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class SimulationServiceTest {

    @Autowired
    private ISimulationService simulationService;

    @Test(expected = ServiceException.class)
    public void saveSimulationWith_no_name() throws ServiceException {

        SimulationDto simulationDto = new SimulationDto();

        simulationService.saveNewSimulation(simulationDto);

    }

    @Test(expected = ServiceException.class)
    public void saveSimulationWith_invalid_luck() throws ServiceException {

        SimulationParticipantDto participantDto1 = new SimulationParticipantDto(1, 1, 5.0);
        SimulationParticipantDto participantDto2 = new SimulationParticipantDto(2, 2, 99.0);

        List<SimulationParticipantDto> participantDtoList = new ArrayList<>();
        participantDtoList.add(participantDto1);
        participantDtoList.add(participantDto2);

        simulationService.saveParticipants(1, participantDtoList);

    }

}
