package com.petrovma92.tests;

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

    private File negativeTestFile;

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

    @AfterGroups(groups = "negative")
    private void negativeDeleteTestFile() throws IOException {
        System.out.println("\u001B[35m\n@AfterGroups(groups = \"negative\")\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        System.out.println("deleteFile: " + String.valueOf(negativeTestFile.delete()).toUpperCase());
    }

    @Test(groups = "negative", expectedExceptions = IOException.class, alwaysRun = true)
    public void negativeTestCreateIOException() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        String emptyName = "";
        File f = new File(emptyName);
        Assert.assertFalse(f.createNewFile());
    }

    @Test(groups = "negative", alwaysRun = true)
    public void negativeTestCreateFileAlreadyExist() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        negativeTestFile = new File(pathToTempFile, "testFile205");
        System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
        assert !negativeTestFile.createNewFile();
        assert !negativeTestFile.exists();
    }

    @AfterGroups(groups = "positive")
    private void positiveDeleteTestPositiveFiles() throws IOException {
        System.out.println("\u001B[35m\n@AfterGroups(groups = \"positive\")\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        System.out.println("deleteFile fileSimpleName: \t\t" + String.valueOf(fileSimpleName.delete()).toUpperCase());
        System.out.println("deleteFile fileNameWithExtension: \t" + String.valueOf(fileNameWithExtension.delete()).toUpperCase());
        System.out.println("deleteFile fileSpecCharName: \t\t" + String.valueOf(fileSpecCharName.delete()).toUpperCase());
    }

    @Test(groups = "positive")
    public void positiveTestCreateFile() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        fileSimpleName = new File(pathToTempFile, "someFile");
        AssertJUnit.assertTrue("Не удалось создать файл с именем: " + fileSimpleName.getName(), fileSimpleName.createNewFile());
        AssertJUnit.assertTrue("Файл с именем: \"" + fileSimpleName.getName() + "\" не найден", fileSimpleName.exists());
    }

    @Test(groups = "positive")
    public void positiveTestCreateFileWithExtension() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        fileNameWithExtension = new File(pathToTempFile, "someFile.txt");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(fileNameWithExtension.createNewFile(), "[Не удалось создать файл]");
        softAssert.assertTrue(fileNameWithExtension.getName().contains("."), "[У файла нет расширения]");
        softAssert.assertEquals(fileNameWithExtension.getName(), "someFile.txt", "[Имена файлов не совпадают]");
        softAssert.assertTrue(fileNameWithExtension.exists(), "[Файл не найден]");
        softAssert.assertAll();
    }

    @Test(groups = "positive")
    public void positiveTestCreateFileWithSpecificChar() throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        fileSpecCharName = new File(pathToTempFile, "$%)()()(=+=&&#@!!");
        Assert.assertTrue(fileSpecCharName.createNewFile(), "Не удалось создать файл с именем: " + fileSpecCharName.getName());
        Assert.assertTrue(fileSpecCharName.exists(), "Файл не найден");
    }
}
