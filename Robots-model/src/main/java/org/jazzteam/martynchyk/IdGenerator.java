package org.jazzteam.martynchyk;

import java.util.UUID;

public class IdGenerator {
    public static Long generateUniqueId() {
        {
            long val = -1;
            do {
                val = UUID.randomUUID().getMostSignificantBits();
            } while (val < 0);
            return val;
        }
    }
}
