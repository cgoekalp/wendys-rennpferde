package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.IHorseService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.HorseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/horses")
public class HorseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseEndpoint.class);
    private static final String BASE_URL = "/api/v1/horses";
    private final IHorseService horseService;
    private final HorseMapper horseMapper;

    @Autowired
    public HorseEndpoint(IHorseService horseService, HorseMapper horseMapper) {
        this.horseService = horseService;
        this.horseMapper = horseMapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HorseDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return horseMapper.entityToDto(horseService.findOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveHorse(@RequestBody HorseDto parameterDto) {

        HorseDto horseDto = null;

        try {
            horseDto = horseMapper.entityToDto(horseService.saveNewHorse(parameterDto));
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(horseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorse(@PathVariable("id") Integer id, @RequestBody HorseDto parameterDto) {

        HorseDto horseDto = null;
        parameterDto.setId(id);

        try {
            horseDto = horseMapper.entityToDto(horseService.updateHorse(parameterDto));
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(horseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
        @PathVariable("id") Integer id) {

        HorseDto horseDto = new HorseDto();

        try {
            horseDto = horseMapper.entityToDto(horseService.deleteById(id));

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }

        return ResponseEntity.ok(horseDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllHorses() {

        List<HorseDto> list = null;

        try {
            list = horseMapper.entityToDtoList(horseService.findAll());
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterHorse(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value = "minSpeed", required = false) Double minSpeed,
        @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
        @RequestParam(value="breed", required = false) String breed
    ) {

        List<HorseDto> list = null;

        try {
            list = horseMapper.entityToDtoList(horseService.filterHorse(name, breed, minSpeed, maxSpeed));
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(list);
    }

}
