package com.me.config.logging;

import java.util.Collection;
import java.util.Map;

import lombok.Builder;

@Builder
public record ResponseLog(Integer status,
                          String contentType,
                          Map<String, Collection<String>> headers,
                          Object body) {

}
