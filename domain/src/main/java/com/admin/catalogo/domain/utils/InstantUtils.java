package com.admin.catalogo.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtils {

    public InstantUtils() {
    }


    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }


}
