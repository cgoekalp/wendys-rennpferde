package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import at.ac.tuwien.sepm.assignment.individual.service.IJockeyService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.JockeyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jockeys")
public class JockeyEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyEndpoint.class);
    private static final String BASE_URL = "/api/v1/horses";
    private final IJockeyService jockeyService;
    private final JockeyMapper jockeyMapper;

    @Autowired
    public JockeyEndpoint(IJockeyService jockeyService, JockeyMapper jockeyMapper) {
        this.jockeyService = jockeyService;
        this.jockeyMapper = jockeyMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAllJockey() {

        List<JockeyDto> list = null;

        try {
            list = jockeyMapper.entityToDtoList(jockeyService.findAll());
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JockeyDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return jockeyMapper.entityToDto(jockeyService.findOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveJockey(@RequestBody JockeyDto jockeyDtoparameter)
     {

        JockeyDto joykeyDto = new JockeyDto();

        try {
            joykeyDto = jockeyMapper.entityToDto(jockeyService.saveNewJockey(jockeyDtoparameter));
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(joykeyDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJockey(@PathVariable("id") Integer id, @RequestBody JockeyDto jockeyDtoparameter
    ) {

        JockeyDto jockeyDto = new JockeyDto();
        jockeyDtoparameter.setId(id);

        try {
            jockeyDto = jockeyMapper.entityToDto(jockeyService.updateJockey(jockeyDtoparameter));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read jockey with id " + jockeyDtoparameter.getId(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading jockey: " + e.getMessage(), e);
        }

        return ResponseEntity.ok(jockeyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable("id") Integer id) {

        JockeyDto jockeyDto = new JockeyDto();

        try {
            jockeyDto = jockeyMapper.entityToDto(jockeyService.deleteById(id));

        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read jockey with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading jockey: " + e.getMessage(), e);
        }

        return ResponseEntity.ok(jockeyDto);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterJockey(
            @RequestParam(value="name", required = false) String name,
            @RequestParam(value = "skill", required = false) Double minSkill
    ) {

        List<JockeyDto> list = null;

        try {
            list = jockeyMapper.entityToDtoList(jockeyService.filterJockey(name, minSkill));
        } catch (ServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return ResponseEntity.ok(list);
    }

}
