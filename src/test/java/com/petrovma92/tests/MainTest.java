package com.petrovma92.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainTest {
    private File tempFile;
    private String pathToTempFile;

    private File negativeTestFile;

    private File fileSimpleName;
    private File fileNameWithExtension;
    private File fileSpecCharName;

    @DataProvider
    private Iterator<Object[]> positiveTest() throws IOException, JSONException {
        List<Object[]> data = new ArrayList<>();

        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/test-data.json")), StandardCharsets.UTF_8));
        JSONArray array = json.getJSONArray("positiveTest");

        JSONObject obj;
        String data1, data2, data3;
        for(int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            try {
                data1 = obj.getString("1");
            } catch (JSONException e) {data1 = null;}
            try {
                data2 = obj.getString("2");
            } catch (JSONException e) {data2 = null;}
            try {
                data3 = obj.getString("3");
            } catch (JSONException e) {data3 = null;}
            data.add(new Object[]{data1, data2, data3});
        }

        return data.iterator();
    }

    @DataProvider
    private Iterator<Object[]> negativeTest() throws IOException, JSONException {
        List<Object[]> data = new ArrayList<>();

        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/test-data.json")), StandardCharsets.UTF_8));
        JSONArray array = json.getJSONArray("negativeTest");

        JSONObject obj;
        String data1, data2, data3;
        for(int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            try {
                data1 = obj.getString("1");
            } catch (JSONException e) {data1 = null;}
            try {
                data2 = obj.getString("2");
            } catch (JSONException e) {data2 = null;}
            data.add(new Object[]{data1, data2});
        }

        return data.iterator();
    }

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

    @AfterMethod(alwaysRun = true)
    private void positiveDeleteTestPositiveFiles() throws IOException {
        System.out.println("\u001B[35m\n@AfterMethod(groups = \"positive\")\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        File[] files = new File(pathToTempFile).listFiles();

        if(files != null) {
            for (File f : files) {
                System.out.println("deleteFile fileName = " + f.getName() + " \t\t" + String.valueOf(f.delete()).toUpperCase());
            }
        }
    }
/*
    @AfterGroups(groups = "negative")
    private void negativeDeleteTestFile() throws IOException {
        System.out.println("\u001B[35m\n@AfterGroups(groups = \"negative\")\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        System.out.println("deleteFile: " + String.valueOf(negativeTestFile.delete()).toUpperCase());
    }*/

    @Test(dataProvider = "negativeTest", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
    public void negativeTestCreateException(String fileNameExist, String fileNameException) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        File f = new File(fileNameException);
        Assert.assertFalse(f.createNewFile());
    }

    @Test(dataProvider = "negativeTest", groups = "negative", alwaysRun = true)
    public void negativeTestCreateFileAlreadyExist(String fileNameExist, String fileNameException) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameExist != null && !fileNameExist.isEmpty()) {
            negativeTestFile = new File(pathToTempFile, fileNameExist);
            System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
            assert !negativeTestFile.createNewFile();
            assert negativeTestFile.exists();
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }

    @Test(dataProvider = "positiveTest", groups = "positive")
    public void positiveTestCreateFile(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n" + getClass().getName() + "." + new Object() {
        }.getClass().getEnclosingMethod().getName() + "\u001B[0m");

        if (fileName != null && !fileName.isEmpty())
        {
            fileSimpleName = new File(pathToTempFile, fileName);
            AssertJUnit.assertTrue("Не удалось создать файл с именем: " + fileSimpleName.getName(), fileSimpleName.createNewFile());
            AssertJUnit.assertTrue("Файл с именем: \"" + fileSimpleName.getName() + "\" не найден", fileSimpleName.exists());
        }
        else
            System.out.println("\u001B[36mIGNORE");

    }

    @Test(dataProvider = "positiveTest", groups = "positive")
    public void positiveTestCreateFileWithExtension(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameExtension != null && !fileNameExtension.isEmpty())
        {
            fileNameWithExtension = new File(pathToTempFile, fileNameExtension);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(fileNameWithExtension.createNewFile(), "[Не удалось создать файл]");
            // TODO: 18.06.17 add regExp here
            softAssert.assertTrue(fileNameWithExtension.getName().contains("."), "[У файла нет расширения]");
            softAssert.assertEquals(fileNameWithExtension.getName(), fileNameExtension, "[Имена файлов не совпадают]");
            softAssert.assertTrue(fileNameWithExtension.exists(), "[Файл не найден]");
            softAssert.assertAll();
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }

    @Test(dataProvider = "positiveTest", groups = "positive")
    public void positiveTestCreateFileWithSpecificChar(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (specificChar != null && !specificChar.isEmpty())
        {
            fileSpecCharName = new File(pathToTempFile, specificChar);
            Assert.assertTrue(fileSpecCharName.createNewFile(), "Не удалось создать файл с именем: " + fileSpecCharName.getName());
            Assert.assertTrue(fileSpecCharName.exists(), "Файл не найден");
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }
}
