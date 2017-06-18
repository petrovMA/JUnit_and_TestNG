package com.petrovma92.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NegativeTests extends MainTest {

    @DataProvider
    private Iterator<Object[]> negativeTestFromFile() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        List<Object[]> data = new ArrayList<>();

        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/test-data.json")), StandardCharsets.UTF_8));
        JSONArray array = json.getJSONArray("negativeTest");

        JSONObject obj;
        String data1, data2;
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

    @DataProvider
    private Iterator<Object[]> negativeTestRandom() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");


        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                super.generateRandomString(20, false, true, false),
                super.generateRandomString(20, false, false, false)});

        data.add(new Object[]{
                super.generateRandomString(20, false, true, true),
                super.generateRandomString(0, false, true, false)});

        data.add(new Object[]{
                super.generateRandomString(20, false, false, true),
                super.generateRandomString(5, true, true, true)});

        return data.iterator();
    }

    //    @Test(dataProvider = "negativeTestFromFile", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
    @Test(dataProvider = "negativeTestRandom", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
    public void negativeTestCreateException(String fileNameExist, String fileNameException) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameException == null || fileNameException.isEmpty())
        {
            File f = new File(fileNameException);
            Assert.assertFalse(f.createNewFile());
        }
        else {
            System.out.println("\u001B[36mIGNORE");
            throw new NullPointerException();
        }
    }

    //    @Test(dataProvider = "negativeTestFromFile", groups = "negative", alwaysRun = true)
    @Test(dataProvider = "negativeTestRandom", groups = "negative", alwaysRun = true)
    public void negativeTestCreateFileAlreadyExist(String fileNameExist, String fileNameException) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameExist != null && !fileNameExist.isEmpty())
        {
            File negativeTestFile = new File(pathToTempFile, fileNameExist);
            System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
            assert !negativeTestFile.createNewFile();
            assert negativeTestFile.exists();
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }
}
