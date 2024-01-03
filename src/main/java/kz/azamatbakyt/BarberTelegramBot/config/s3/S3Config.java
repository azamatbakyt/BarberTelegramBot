package kz.azamatbakyt.BarberTelegramBot.config.s3;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.slf4j.Logger;

import java.net.URI;

@Configuration
public class S3Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Config.class);

    @Bean
    public S3Client s3Client(S3ClientProps props){

        if (props.isAuto()) {
            LOGGER.info("Auto configuration");
            return S3Client.builder()
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        } else {
            LOGGER.info("Manual Configuration");
            final var creds = AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey());
            return S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(creds))
                    .region(Region.of(props.getRegion()))
                    .endpointOverride(URI.create(props.getEndpoint()))
                    .build();
        }


    }





}
