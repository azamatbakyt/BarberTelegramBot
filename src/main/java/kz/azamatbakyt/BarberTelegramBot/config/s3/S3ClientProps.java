package kz.azamatbakyt.BarberTelegramBot.config.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class S3ClientProps {

    @Value("${s3.endpoint}")
    private String endpoint;
    @Value("${s3.accessKey}")
    private String accessKey;
    @Value("${s3.secretKey}")
    private String secretKey;
    @Value("${s3.region}")
    private String region;
    @Value("${s3.auto}")
    private boolean auto;

    public S3ClientProps() {
    }

    public S3ClientProps(String endpoint, String accessKey, String secretKey, String region, boolean auto) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.auto = auto;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getRegion() {
        return region;
    }

    public boolean isAuto() {
        return auto;
    }
}
