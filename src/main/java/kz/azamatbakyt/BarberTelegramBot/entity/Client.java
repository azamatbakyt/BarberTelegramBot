package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "name")
    public String name;


    @Column(name = "phone")
    public String phone;

    @Column(name = "registration_completed")
    private boolean registrationCompleted;

    public Client() {
    }

    public Client(Long id, Long chatId, String name, String phone, boolean registrationCompleted) {
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
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
