package com.habosa.notificationbox.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for background wor.
 */
public class BackgroundUtils {

    public static final ThreadPoolExecutor SERIAL = new ThreadPoolExecutor(1, 1,
            60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

}
