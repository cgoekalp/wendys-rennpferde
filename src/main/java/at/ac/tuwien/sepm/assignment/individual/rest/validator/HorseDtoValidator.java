package at.ac.tuwien.sepm.assignment.individual.rest.validator;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

public class HorseDtoValidator {

    public static String isValid(HorseDto horseDto){

        if(horseDto.getName() == null || horseDto.getName().isEmpty()){
            return "Could not save horse, invalid dto provided, please check name,min and max speed";
        }

        if(horseDto.getMinSpeed() != null && (horseDto.getMinSpeed() < 40)){
            return "Could not lower than 40, invalid dto provided, please min speed";
        }

        if(horseDto.getMaxSpeed() != null && horseDto.getMaxSpeed() > 60){
            return "Max speed could not be greater >60, invalid dto provided, please check max speed";
        }

        if(horseDto.getMinSpeed() != null && horseDto.getMaxSpeed() != null){

            if(horseDto.getMinSpeed() > horseDto.getMaxSpeed()){
                return "Min speed cannot be smaller than max speed";
            }
        }

        return "";
    }
}
