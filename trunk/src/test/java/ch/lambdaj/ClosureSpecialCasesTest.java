package ch.lambdaj;


import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static ch.lambdaj.Lambda.*;

import ch.lambdaj.function.closure.*;

import java.util.*;

import org.junit.*;

/**
 * @author Mario Fusco
 */
public class ClosureSpecialCasesTest {

    void oneArg(Object o) {
        System.out.println("called one-arg: " + o);
    }

    void twoArg(Object o, String readThis) {
        System.out.println("called two-arg: " + o + ", " + readThis);
    }

    @Test
    public void testWithEmptyListAndUnboundVar() {
        List<String> items = new ArrayList<String>();
        Closure closure = closure(); {
            of(this).twoArg(items, var(String.class));
        }
        items.add("added later");
        closure.apply("Read this");
        assertEquals(asList("added later"), items);
    }

    @Test
    public void testWithNonEmptyListAndUnboundVar() {
        List<String> items = new ArrayList(asList("initial"));
        Closure closure = closure(); {
            of(this).twoArg(items, var(String.class));
        }
        items.add("added later");
        closure.apply("Read this");
        assertEquals(asList("initial", "added later"), items);
    }

    @Test
    public void testWithEmptyListOnly() {
        List<String> items = new ArrayList();
        Closure closure = closure(); {
            of(this).oneArg(items);
        }
        items.add("added later");
        closure.apply();
        assertEquals(asList("added later"), items);
    }

    @Test
    public void testWithNonEmptyListOnly() {
        List<String> items = new ArrayList(asList("initial"));
        Closure closure = closure(); {
            of(this).oneArg(items);
        }
        items.add("added later");
        closure.apply();
        assertEquals(asList("initial", "added later"), items);
    }
}
