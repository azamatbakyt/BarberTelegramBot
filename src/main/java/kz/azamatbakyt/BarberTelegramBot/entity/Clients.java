package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "name")
    public String name;


    @Column(name = "phone")
    public String phone;

    @Column(name = "registration_completed")
    private boolean registrationCompleted;

    public Clients() {
    }

    public Clients(Long id, String chatId, String name, String phone, boolean registrationCompleted) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.registrationCompleted = registrationCompleted;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRegistrationCompleted() {
        return registrationCompleted;
    }

    public void setRegistrationCompleted(boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
    }


    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", registrationCompleted=" + registrationCompleted +
                '}';
    }
}
