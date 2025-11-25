package com.me.util;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@UtilityClass
public final class JsonUtils {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//        .addModule(new JavaTimeModule())     // add modules manually
        .build();

  @SneakyThrows
  public static <T> T convertToObject(String fileName, Class<T> type) {
    final InputStream file = new ClassPathResource(fileName).getInputStream();
    return MAPPER.readValue(file.readAllBytes(), type);
  }

  @SneakyThrows
  public static <T> T convertToObject(String fileName, TypeReference<T> type) {
    final InputStream file = new ClassPathResource(fileName).getInputStream();
    return MAPPER.readValue(file.readAllBytes(), type);
  }

  @SneakyThrows
  public static String convertToString(Object object) {
    return MAPPER.writeValueAsString(object);
  }

  @SneakyThrows
  public static <T> T jsonToObject(String json, Class<T> type) {
    return MAPPER.readValue(json, type);
  }

  @SneakyThrows
  public static String readJson(String fileName) {
    final InputStream file = new ClassPathResource(fileName).getInputStream();
    return new String(file.readAllBytes());
  }

}
