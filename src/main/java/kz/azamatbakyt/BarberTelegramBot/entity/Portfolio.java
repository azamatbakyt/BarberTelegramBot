package kz.azamatbakyt.BarberTelegramBot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bucket;
    private String key;

    public Portfolio() {
    }

    public Portfolio(Long id, String bucket, String key) {
        this.id = id;
        this.bucket = bucket;
        this.key = key;
    }

    public Portfolio(String bucket, String key) {
        this.bucket = bucket;
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
