package kr.toyauction.global.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.toyauction.global.serializer.LocalDatetimeJsonDeserializer;
import kr.toyauction.global.serializer.LocalDatetimeJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .registerModule(javaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private SimpleModule javaTimeModule() {
        return new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDatetimeJsonSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDatetimeJsonDeserializer());
    }
}
