package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Jockey {

        private Integer id;
        private String name;
        private Double skill;
        private LocalDateTime created;
        private LocalDateTime updated;
        private Boolean deleted;

        public Jockey() {
        }


        public Jockey(Integer id, String name, Double skill, LocalDateTime created, LocalDateTime updated, Boolean deleted) {
            this.id = id;
            this.name = name;
            this.skill = skill;
            this.created = created;
            this.updated = updated;
            this.deleted = deleted;
        }

        public Boolean getDeleted() {
            return deleted;
        }

        public void setDeleted(Boolean deleted) {
            this.deleted = deleted;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public LocalDateTime getUpdated() {
            return updated;
        }

        public void setUpdated(LocalDateTime updated) {
            this.updated = updated;
        }

        public Double getSkill() {
            return skill;
        }

        public void setSkill(Double skill) {
            this.skill = skill;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Jockey)) return false;
            Jockey horse = (Jockey) o;
            return Objects.equals(id, horse.id) &&
                Objects.equals(name, horse.name) &&
                Objects.equals(skill, horse.skill) &&
                Objects.equals(created, horse.created) &&
                Objects.equals(updated, horse.updated)&&
                Objects.equals(deleted, horse.deleted);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, name, skill, created, updated);
        }

        @Override
        public String toString() {
            return "Horse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skill=" + skill +
                ", created=" + created +
                ", updated=" + updated +
                '}';
        }
}
