package com.petrovma92.tests.task_1_1;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Test
public class MainTest {
    static File tempFile;
    static String pathToTempFile;

    @BeforeSuite
    public void preConditions() throws IOException {
        System.out.println("\u001B[32m=============\npreConditions");
        File currentDirFile = new File(".");
        String helper = currentDirFile.getCanonicalPath();

        tempFile = Files.createTempDirectory(Paths.get(helper), "tmpDir").toFile();
        pathToTempFile = tempFile.getAbsolutePath();

        System.out.println("createTempDirectory\u001B[0m\n");
    }

    @AfterSuite
    public void postConditions() {
        System.out.println("\n\u001B[36mdeleteTempDirectory: " + String.valueOf(tempFile.delete()).toUpperCase() + "\npostConditions\n==============\u001B[0m");
    }
}
