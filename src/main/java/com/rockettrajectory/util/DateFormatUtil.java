package com.rockettrajectory.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * DateFormatUtil
 * --------------
 * Convenience helpers so JSPs can render java.sql.Timestamp values
 * without resorting to scriptlets or extra taglibs.
 */
public final class DateFormatUtil {

    private DateFormatUtil() { }

    /** "06 May 2026, 14:30" */
    public static String pretty(Timestamp t) {
        if (t == null) return "-";
        return new SimpleDateFormat("dd MMM yyyy, HH:mm").format(t);
    }

    /** "06 May 2026" */
    public static String shortDate(Timestamp t) {
        if (t == null) return "-";
        return new SimpleDateFormat("dd MMM yyyy").format(t);
    }
}
