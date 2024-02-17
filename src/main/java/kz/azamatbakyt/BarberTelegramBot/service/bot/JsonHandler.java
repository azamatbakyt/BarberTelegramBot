package kz.azamatbakyt.BarberTelegramBot.service.bot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotV2.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return "Ошибка";
        }
    }

    public static <T> T toObject(Object object, Class<T> clazz) {
        try {
            return mapper.convertValue(object, clazz);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static List<String> toList(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return List.of();
        }
    }
}
