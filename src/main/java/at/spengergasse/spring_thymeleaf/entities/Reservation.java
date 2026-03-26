package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

@Entity
@Table
public class Reservation {
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    @Column
        private int id;

    @Column
        private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
