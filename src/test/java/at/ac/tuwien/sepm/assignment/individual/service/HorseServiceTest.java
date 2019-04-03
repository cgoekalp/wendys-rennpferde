package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class HorseServiceTest {

    @Autowired
    private IHorseService horseService;

    @Test(expected = ServiceException.class)
    public void saveHorseWith_NoName() throws ServiceException {

        HorseDto horseDto = new HorseDto();
        horseService.saveNewHorse(horseDto);

    }

    @Test(expected = ServiceException.class)
    public void min_speed_is_greater_than_max() throws ServiceException {

        HorseDto horseDto = new HorseDto();
        horseDto.setName("hoarse");
        horseDto.setMinSpeed(55.0);
        horseDto.setMaxSpeed(45.0);

        horseService.saveNewHorse(horseDto);

    }

    @Test(expected = ServiceException.class)
    public void min_speed_is_smaller_tha_40() throws ServiceException {

        HorseDto horseDto = new HorseDto();
        horseDto.setName("hoarse");
        horseDto.setMinSpeed(39.0);
        horseDto.setMaxSpeed(45.0);

        horseService.saveNewHorse(horseDto);

    }
}
