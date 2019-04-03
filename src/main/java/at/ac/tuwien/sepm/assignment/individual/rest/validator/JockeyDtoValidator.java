package at.ac.tuwien.sepm.assignment.individual.rest.validator;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;

public class JockeyDtoValidator {

    public static String isValid(JockeyDto jockeyDto) {

        if (jockeyDto.getName() == null || jockeyDto.getName().isEmpty()) {
            return "Could not save jockey, invalid dto provided, please check name";
        }

        return "";
    }
}
