package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HorseMapper {

    public HorseDto entityToDto(Horse horse) {
        return new HorseDto(horse.getId(), horse.getName(), horse.getBreed(), horse.getMinSpeed(), horse.getMaxSpeed(), horse.getCreated(), horse.getUpdated());
    }

    public List<HorseDto> entityToDtoList(List<Horse> horseList) {

        List<HorseDto> list = new ArrayList<>();

        for(Horse horse : horseList){
            list.add(entityToDto(horse));
        }

        return list;
    }



}
