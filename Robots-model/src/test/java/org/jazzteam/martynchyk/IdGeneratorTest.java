package org.jazzteam.martynchyk;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class IdGeneratorTest {

    @Test
    public void testGenerateUniqueIdNotNull() {
        assertNotEquals(IdGenerator.generateUniqueId(),-1);
    }

    @Test
    public void testGenerateUniqueIdNotEmpty() {
        assertNotNull(IdGenerator.generateUniqueId());
    }

    @Test
    public void testGenerateUniqueId() {
        Long id1 = IdGenerator.generateUniqueId();
        Long id2 = IdGenerator.generateUniqueId();
        assertNotEquals(id1,id2);
    }
}