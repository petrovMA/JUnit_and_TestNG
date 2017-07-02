package com.petrovma92.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PositiveTests extends MainTest {

    @DataProvider
    private Iterator<Object[]> positiveTestFromFile() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

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
    private Iterator<Object[]> positiveTestRandom() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                super.generateRandomString(20, false, true, false),
                super.generateRandomString(20, false, true, false) + "." +super.generateRandomString(2, false, true, false),
                super.generateRandomString(20, true, false, false)});

        data.add(new Object[]{
                super.generateRandomString(20, false, true, true),
                super.generateRandomString(19, true, true, false) + "." +super.generateRandomString(3, false, true, false),
                super.generateRandomString(20, false, true, false)});

        data.add(new Object[]{
                super.generateRandomString(20, true, false, true),
                super.generateRandomString(18, true, true, true) + "." +super.generateRandomString(4, false, true, false),
                super.generateRandomString(20, false, false, true)});

        data.add(new Object[]{
                super.generateRandomString(20, false, false, true),
                super.generateRandomString(15, true, true, true) + "." +super.generateRandomString(7, true, true, false),
                super.generateRandomString(20, false, false, false)});

        return data.iterator();
    }

    @TempDir()
    //    @Test(dataProvider = "positiveTestFromFile", groups = "positive")
    @Test(dataProvider = "positiveTestRandom", groups = "positive")
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
            throw new SkipException("IGNORE\nfileName null or empty.");

    }

    @TempDir()
    //    @Test(dataProvider = "positiveTestFromFile", groups = "positive")
    @Test(dataProvider = "positiveTestRandom", groups = "positive")
    public void positiveTestCreateFileWithExtension(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameExtension != null && !fileNameExtension.isEmpty())
        {
            fileNameWithExtension = new File(pathToTempFile, fileNameExtension);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(fileNameWithExtension.createNewFile(), "[Не удалось создать файл]");
            softAssert.assertTrue(fileNameWithExtension.getName().matches(".+\\..+"), "[У файла нет расширения]");
            softAssert.assertEquals(fileNameWithExtension.getName(), fileNameExtension, "[Имена файлов не совпадают]");
            softAssert.assertTrue(fileNameWithExtension.exists(), "[Файл не найден]");
            softAssert.assertAll();
        }
        else
            throw new SkipException("IGNORE\nfileName null or empty.");
    }

    @TempDir()
    //    @Test(dataProvider = "positiveTestFromFile", groups = "positive")
    @Test(dataProvider = "positiveTestRandom", groups = "positive")
    public void positiveTestCreateFileWithSpecificChar(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (specificChar != null && !specificChar.isEmpty())
        {
            fileSpecCharName = new File(pathToTempFile, specificChar);
            Assert.assertTrue(fileSpecCharName.createNewFile(), "Не удалось создать файл с именем: " + fileSpecCharName.getName());
            Assert.assertTrue(fileSpecCharName.exists(), "Файл не найден");
        }
        else
            throw new SkipException("IGNORE\nfileName null or empty.");
    }
}
