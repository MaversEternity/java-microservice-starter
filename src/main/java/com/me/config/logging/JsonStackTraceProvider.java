package com.me.config.logging;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.SneakyThrows;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.composite.loggingevent.StackTraceJsonProvider;
import tools.jackson.core.JsonGenerator;

/**
 * Format stacktrace lines in json format, line by line
 */
public class JsonStackTraceProvider extends StackTraceJsonProvider {

  @Override
  public void writeTo(JsonGenerator generator, ILoggingEvent event) {
    Optional.ofNullable(event.getThrowableProxy())
        .map(t -> getThrowableConverter().convert(event))
        .map(msg -> msg.split("\\n\\t"))
        .map(lines -> IntStream
            .range(0, lines.length)
            .boxed()
            .collect(Collectors.toMap(i -> String.valueOf(i + 1), i -> lines[i], (a, b) -> a, LinkedHashMap::new)))
        .ifPresent(stacktrace -> writeStacktrace(generator, stacktrace));
  }

  @SneakyThrows
  private void writeStacktrace(JsonGenerator generator, Map<String, String> stacktrace) {
      JsonWritingUtils.writeMapStringFields(generator, getFieldName(), stacktrace);
  }
}
