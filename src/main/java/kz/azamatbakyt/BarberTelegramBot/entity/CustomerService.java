package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;



@Entity
@Table(name = "customer_service")
public class CustomerService{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Укажите правильно услугу")
    private String name;

    @Min(value=0, message = "Укажите стоимость правильно")
    private String price;

    @NotEmpty(message="Укажите длительность правильно")
    private String duration;

    @NotEmpty(message = "Вы забыли выбрать группу")
    @ManyToOne
    @JoinColumn(name = "customer_service_group_id", referencedColumnName = "id")
    private CustomerServiceGroup group;

    public CustomerService() {
    }

    public CustomerService(Long id, String name, String price, String duration, CustomerServiceGroup group) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.group = group;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public CustomerServiceGroup getGroup() {
        return group;
    }

    public void setGroup(CustomerServiceGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "CustomerService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
