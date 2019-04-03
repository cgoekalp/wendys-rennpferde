package at.ac.tuwien.sepm.assignment.individual.entity;

import java.util.Objects;

public class HorseJockeyJoined {

    private Integer id;
    private String horseName;
    private String jockeyName;
    private Double horseMin;
    private Double horseMax;

    public Double getHorseMin() {
        return horseMin;
    }

    public void setHorseMin(Double horseMin) {
        this.horseMin = horseMin;
    }

    public Double getHorseMax() {
        return horseMax;
    }

    public void setHorseMax(Double horseMax) {
        this.horseMax = horseMax;
    }

    private Double skill;
    private Double luckFactor;

    public HorseJockeyJoined() {
    }

    public HorseJockeyJoined(Integer id, Integer rank, String horseName, String jockeyName, Double horseMin, Double horseMax, Double skill, Double luckFactor) {

        this.horseName = horseName;
        this.jockeyName = jockeyName;
        this.skill = skill;
        this.luckFactor = luckFactor;
        this.id = id;
        this.horseMin = horseMin;
        this.horseMax = horseMax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public Double getSkill() {
        return skill;
    }

    public void setSkill(Double skill) {
        this.skill = skill;
    }

    public Double getLuckFactor() {
        return luckFactor;
    }

    public void setLuckFactor(Double luckFactor) {
        this.luckFactor = luckFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseJockeyJoined)) return false;
        HorseJockeyJoined that = (HorseJockeyJoined) o;
        return
                Objects.equals(horseName, that.horseName) &&
                Objects.equals(jockeyName, that.jockeyName) &&
                Objects.equals(skill, that.skill) &&
                Objects.equals(luckFactor, that.luckFactor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(horseName, jockeyName, skill, luckFactor);
    }

    @Override
    public String toString() {
        return "HorseJockeyJoined{" +
                "id=" + id +
                ", horseName='" + horseName + '\'' +
                ", jockeyName='" + jockeyName + '\'' +
                ", skill=" + skill +
                ", luckFactor=" + luckFactor +
                '}';
    }

}
