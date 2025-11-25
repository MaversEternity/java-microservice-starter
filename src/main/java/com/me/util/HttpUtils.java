package com.me.util;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import com.me.exception.BusinessException;
import com.me.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

@UtilityClass
public class HttpUtils {
  public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static final String ZIP_CONTENT_TYPE = "application/zip";
  private static final String EMPTY_BRACKETS = "{}";
  private static final Set<String> EXCLUDED_CONTENT_TYPES = Set.of(APPLICATION_PDF_VALUE,
      APPLICATION_XML_VALUE,
      APPLICATION_OCTET_STREAM_VALUE,
      TEXT_XML_VALUE,
      TEXT_PLAIN_VALUE,
      TEXT_HTML_VALUE,
      "text/yaml",
      "text/csv",
      XLSX_CONTENT_TYPE,
      ZIP_CONTENT_TYPE,
      IMAGE_PNG_VALUE,
      IMAGE_JPEG_VALUE,
      IMAGE_GIF_VALUE,
      MULTIPART_FORM_DATA_VALUE);

  /**
   * Get mandatory header value from request
   *
   * @param header  header name
   * @param request request
   * @return header value
   */
  public static String getRequiredHeaderValue(String header, HttpServletRequest request) {
    String headerValue = request.getHeader(header);

    if (headerValue == null) {
      throw BusinessException.of(ErrorCode.MISSING_HEADER);
//          .arg("Header", header)
//          .arg("Request method", request.getMethod())
//          .arg("Request URI", request.getRequestURI())
//          .build();
    }

    return headerValue;
  }

  public static Map<String, Collection<String>> toMultimap(Iterator<String> names, Function<String, Collection<String>> function) {
    Map<String, Collection<String>> map = new HashMap<>();
    names.forEachRemaining(h -> map.put(h, function.apply(h)));
    return map;
  }

  public static String getBody(byte[] body, String contentType) {
    return Optional.ofNullable(contentType)
        .filter(t -> EXCLUDED_CONTENT_TYPES.stream().noneMatch(t::contains))
        .map(t -> IOUtils.toString(body, StandardCharsets.UTF_8.name()))
        .filter(it -> !it.isBlank())
        .map(HttpUtils::hideSensitiveData)
        .orElse(EMPTY_BRACKETS);
  }

  private static String hideSensitiveData(String strBody) {
    //TODO : replace sensitive patient data with ***
    return strBody;
  }

}
