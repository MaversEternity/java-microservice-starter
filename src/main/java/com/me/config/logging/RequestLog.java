package com.me.config.logging;

import java.util.Collection;
import java.util.Map;

import lombok.Builder;

@Builder
public record RequestLog(String url,
                         String contentType,
                         Map<String, Collection<String>> headers,
                         Map<String, Collection<String>> params,
                         Object body) {

}
