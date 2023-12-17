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
    private int price;


    @NotEmpty(message="Укажите длительность правильно")
    private int duration;


    @ManyToOne
    @JoinColumn(name = "customer_service_group_id", referencedColumnName = "id")
    private CustomerServiceGroup group;

    public CustomerService() {
    }

    public CustomerService(Long id, String name, int price, int duration, CustomerServiceGroup group) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.group = group;
    }

    public CustomerService(String name, int price, int duration, CustomerServiceGroup group) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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
