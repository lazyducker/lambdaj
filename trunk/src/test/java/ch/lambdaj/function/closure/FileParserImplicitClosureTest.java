// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.Lambda.*;
import static org.junit.Assert.*;

import org.junit.*;

import java.io.*;

import ch.lambdaj.function.closure.*;

/**
 * @author Mario Fusco
 */
public class FileParserImplicitClosureTest {

    private int lineCounter = 0;

    void countNonEmptyLine(String line) {
        if (line != null && line.trim().length() > 0) lineCounter++;
    }

    @Test
    public void testPrintFile() {
        readFileByLine("commedia.txt"); {
            of(System.out).println(var(String.class));
        }
    }

    @Test
    public void testReadFile() {
        StringWriter sw = new StringWriter();
        readFileByLine("commedia.txt"); {
            of(sw).write(var(String.class));
        }
    }

    @Test
    public void testCountFile() {
        lineCounter = 0;
        readFileByLine("commedia.txt"); {
            of(this).countNonEmptyLine(var(String.class));
        }
        assertEquals(9, lineCounter);
    }

    private void readFileByLine(String fileName) {
        Closure1<String> lineReader = closure(String.class);
        BufferedReader reader = null;
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lineReader.apply(line);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Error while reading file " + fileName, ioe);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Error while closing file reader", ioe);
            }
        }
    }
}
