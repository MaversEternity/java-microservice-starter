package com.me.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CurrentTimeProvider {

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

}
