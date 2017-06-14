package com.petrovma92.tests.task_1_1;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainTest {
    private File tempFile;
    private String pathToTempFile;

    private String name = "testFile205";

    private File fileSimpleName;
    private File fileNameWithExtension;
    private File fileSpecCharName;

    @BeforeSuite(groups = {"negative", "positive"})
    private void preConditions() throws IOException {
        System.out.println("\u001B[32m=============\n@BeforeSuite");
        File currentDirFile = new File(".");
        String helper = currentDirFile.getCanonicalPath();

        tempFile = Files.createTempDirectory(Paths.get(helper), "tmpDir").toFile();
        pathToTempFile = tempFile.getAbsolutePath();

        System.out.println("createTempDirectory\u001B[0m");
    }

    @AfterSuite(groups = {"negative", "positive"})
    private void postConditions() {
        System.out.println("\n\u001B[32m@AfterSuite\ndeleteTempDirectory: " + String.valueOf(tempFile.delete()).toUpperCase() + "\n==============\u001B[0m");
    }

    @BeforeGroups(groups = "negative")
    private void negativeCreateTestFile() throws IOException {
        System.out.println("\u001B[35m\n@BeforeGroups\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        File f = new File(pathToTempFile, this.name);
        System.out.println("createFile: " + String.valueOf(f.createNewFile()).toUpperCase());
    }

    @AfterGroups(groups = "negative")
    private void negativeDeleteTestFile() throws IOException {
        System.out.println("\u001B[35m\n@AfterGroups\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        File f = new File(pathToTempFile, this.name);
        System.out.println("deleteFile: " + String.valueOf(f.delete()).toUpperCase());
    }

    @Test(groups = "negative", expectedExceptions = IOException.class, alwaysRun = true)
    public void negativeTestCreateIOException() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        String emptyName = "";
        File f = new File(emptyName);
        Assert.assertEquals(false, f.createNewFile());
    }

    @Test(groups = "negative", alwaysRun = true)
    public void negativeTestCreateFileAlreadyExist() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        File f = new File(pathToTempFile, this.name);
        assert !f.createNewFile();
    }

    @BeforeGroups(groups = "positive")
    private void positiveCreateTestFiles() throws IOException {
        System.out.println("\u001B[35m\n@BeforeGroups\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        fileSimpleName = new File(pathToTempFile, "someFile");
        fileNameWithExtension = new File(pathToTempFile, "someFile.txt");
        fileSpecCharName = new File(pathToTempFile, "$%)()()(=+=&&#@!!");
    }

    @AfterGroups(groups = "positive")
    private void positiveDeleteTestPositiveFiles() throws IOException {
        System.out.println("\u001B[35m\n@AfterGroups\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        System.out.println("deleteFile fileSimpleName: \t\t" + String.valueOf(fileSimpleName.delete()).toUpperCase());
        System.out.println("deleteFile fileNameWithExtension: \t" + String.valueOf(fileNameWithExtension.delete()).toUpperCase());
        System.out.println("deleteFile fileSpecCharName: \t\t" + String.valueOf(fileSpecCharName.delete()).toUpperCase());
    }

    @Test(groups = "positive")
    public void positiveTestCreateFile() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        AssertJUnit.assertTrue("Не удалось создать файл с именем: " + fileSimpleName.getName(), fileSimpleName.createNewFile());
    }

    @Test(groups = "positive")
    public void positiveTestCreateFileWithExtension() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(fileNameWithExtension.createNewFile(), "[Не удалось создать файл]");
        softAssert.assertTrue(fileNameWithExtension.getName().contains("."), "[У файла нет расширения]");
        softAssert.assertEquals(fileNameWithExtension.getName(), "someFile.txt", "[Имена файлов не совпадают]");
        softAssert.assertAll();
    }

    @Test(groups = "positive")
    public void positiveTestCreateFileWithSpecificChar() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        Assert.assertTrue(fileSpecCharName.createNewFile(), "Не удалось создать файл с именем: " + fileSpecCharName.getName());
    }
}
