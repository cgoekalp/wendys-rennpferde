package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JockeyMapper {
    public JockeyDto entityToDto(Jockey saveNewJockey) {

        return new JockeyDto(saveNewJockey.getId(), saveNewJockey.getName(), saveNewJockey.getSkill(), saveNewJockey.getCreated(), saveNewJockey.getUpdated());
    }

    public List<JockeyDto> entityToDtoList(List<Jockey> all) {

        List<JockeyDto> dtoList = new ArrayList<>();

        for(Jockey jockey : all){
            dtoList.add(entityToDto(jockey));
        }

        return dtoList;
    }
}
