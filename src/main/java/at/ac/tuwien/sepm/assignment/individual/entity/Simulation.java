package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;

public class Simulation {

    private Integer id;
    private String name;
    private LocalDateTime created;
    private Boolean finished;

    public Simulation(){
    }

    public Simulation(String name, LocalDateTime created, Boolean finished){
        this.name = name;
        this.created = created;
        this.finished = finished;
    }

    public Simulation(Integer id, String name, LocalDateTime created, boolean finished) {
        this.name = name;
        this.id = id;
        this.created = created;
        this.finished = finished;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
