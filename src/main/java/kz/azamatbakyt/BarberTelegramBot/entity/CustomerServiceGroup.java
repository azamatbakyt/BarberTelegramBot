package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "customer_service_group")
public class CustomerServiceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Укажите правильное название")
    private String name;

    public CustomerServiceGroup() {}

    public CustomerServiceGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CustomerServiceGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

