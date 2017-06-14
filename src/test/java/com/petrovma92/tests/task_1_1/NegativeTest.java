package com.petrovma92.tests.task_1_1;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

@Test
public class NegativeTest extends MainTest {
    private String name = "testFile205";

    @AfterClass
    private void deleteTestFile() throws IOException {
        File f = new File(pathToTempFile, this.name);
        System.out.println("deleteFile: " + String.valueOf(f.delete()).toUpperCase());
    }

    @Test(expectedExceptions = IOException.class)
    public void testCreateIOException() throws IOException {
        String emptyName = "";
        File f = new File(emptyName);
        Assert.assertEquals(false, f.createNewFile());
    }

    public void testCreateFileAlreadyExist() throws IOException {
        File f = new File(pathToTempFile, this.name);
        System.out.println("createFile: " + String.valueOf(f.createNewFile()).toUpperCase());

        Assert.assertEquals(false, f.createNewFile());
    }
}
