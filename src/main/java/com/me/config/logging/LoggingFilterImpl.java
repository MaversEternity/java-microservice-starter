package com.me.config.logging;

import java.util.Collections;
import java.util.List;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.me.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;

import static com.me.util.HttpUtils.getBody;
import static com.me.util.HttpUtils.toMultimap;

@Slf4j
@RequiredArgsConstructor
public class LoggingFilterImpl implements LoggingFilter {

  private final LoggingFormat format;

  @Override
  public void logRequest(ContentCachingRequestWrapper request) {
    try {
      RequestLog requestLog = RequestLog.builder()
          .url(request.getRequestURI())
          .contentType(request.getContentType())
          .headers(toMultimap(request.getHeaderNames().asIterator(), h -> Collections.list(request.getHeaders(h))))
          .params(toMultimap(request.getParameterNames().asIterator(), p -> List.of(request.getParameterValues(p))))
          .body(convertBodySafe(getBody(request.getContentAsByteArray(), request.getContentType())))
          .build();

      if (format == LoggingFormat.JSON) {
        LogstashMarker marker = Markers.append("request", requestLog);
        log.info(marker, "Logging request");
      } else {
        log.info("Logging request: {}", requestLog);
      }

    } catch (Exception e) {
      log.error("An error occurred while logging request", e);
    }
  }

  @Override
  public void logResponse(ContentCachingResponseWrapper response) {
    try {
      ResponseLog responseLog = ResponseLog.builder()
          .status(response.getStatus())
          .contentType(response.getContentType())
          .headers(toMultimap(response.getHeaderNames().iterator(), response::getHeaders))
          .body(convertBodySafe(getBody(response.getContentAsByteArray(), response.getContentType())))
          .build();

      if (format == LoggingFormat.JSON) {
        LogstashMarker marker = Markers.append("response", responseLog);
        log.info(marker, "Logging response");
      } else {
        log.info("Logging response: {}", responseLog);
      }

      response.copyBodyToResponse();
    } catch (Exception e) {
      log.error("An error occurred while logging response", e);
    }
  }

  private Object convertBodySafe(String body) {
    try {
      return JsonUtils.jsonToObject(body, Object.class);
    } catch (Exception e) {
      log.error("Error while reading body: {}", body, e);
    }
    return null;
  }
}
