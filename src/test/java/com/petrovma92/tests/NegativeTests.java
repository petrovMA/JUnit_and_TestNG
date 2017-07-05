package com.petrovma92.tests;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NegativeTests extends MainTest {

    @DataProvider
    private Iterator<Object[]> negativeTestFromExel(Method m) throws IOException, JSONException, InvalidFormatException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+new Object(){}.getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        final int START_READ_FROM = 5;

        int length = m.getParameterTypes().length;

        OPCPackage pkg = OPCPackage.open(new File( System.getProperty("user.dir") + "/src/test/resources/testData.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        XSSFSheet sheet = wb.getSheet("testData");

        List<Object[]> data = new ArrayList<>();
        Object[] arr;

        int i = 0;
        while (hasNext(sheet, START_READ_FROM, length, i)) {
            arr = new Object[length];
            for(int j = 0; j < length; j++) {
                arr[j] = getCellValue(sheet, START_READ_FROM+j, i);
            }
            data.add(arr);
            i++;
        }

        return data.iterator();
    }

    @DataProvider
    private Iterator<Object[]> negativeTestFromJson() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

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
                if(obj.getString("2").equalsIgnoreCase("NULL"))
                    data2 = null;
                else
                    data2 = obj.getString("2");
            } catch (JSONException e) {data2 = null;}
            try {
                data3 = obj.getString("1");
            } catch (JSONException e) {data3 = null;}
            data.add(new Object[]{data1, data2, data3});
        }

        return data.iterator();
    }

    @DataProvider
    private Iterator<Object[]> negativeTestRandom() throws IOException, JSONException, InvalidFormatException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                super.generateRandomString(20, false, true, false),
                super.generateRandomString(20, false, false, false),
                super.generateRandomString(20, false, false, true)});

        data.add(new Object[]{
                super.generateRandomString(20, false, true, true),
                super.generateRandomString(0, false, true, false),
                super.generateRandomString(20, true, true, false)});

        data.add(new Object[]{
                super.generateRandomString(20, false, false, true),
                super.generateRandomString(5, true, true, true),
                super.generateRandomString(20, false, true, true)});

        return data.iterator();
    }

    @TempDir()
    //    @Test(dataProvider = "negativeTestFromJson", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
    @Test(dataProvider = "negativeTestFromExel", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
//    @Test(dataProvider = "negativeTestRandom", groups = "negative", expectedExceptions = {IOException.class, NullPointerException.class}, alwaysRun = true)
    public void negativeTestCreateException(String fileNameExist, String fileNameException, String fileInReadOnlyDir) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameException == null || fileNameException.isEmpty())
        {
            File f = new File(fileNameException);
            Assert.assertFalse(f.createNewFile());
        }
        else
            throw new SkipException("IGNORE\nfileName notNull or notEmpty.");
    }

    @TempDir(read = false)
    //    @Test(dataProvider = "negativeTestFromJson", groups = "negative", alwaysRun = true)
    @Test(dataProvider = "negativeTestFromExel", groups = "negative", alwaysRun = true)
//    @Test(dataProvider = "negativeTestRandom", groups = "negative", alwaysRun = true)
    public void negativeTestCreateFileAlreadyExist(String fileNameExist, String fileNameException, String fileInReadOnlyDir) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileNameExist != null && !fileNameExist.isEmpty())
        {
            File negativeTestFile = new File(pathToTempFile, fileNameExist);
            System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
            assert !negativeTestFile.createNewFile();
            assert negativeTestFile.exists();
        }
        else
            throw new SkipException("IGNORE\nfileName null or empty.");
    }

    @TempDir(read = false, write = false)
    //    @Test(dataProvider = "negativeTestFromJson", groups = "negative", alwaysRun = true)
    @Test(dataProvider = "negativeTestFromExel", groups = "negative", alwaysRun = true, expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Отказано в доступе")
//    @Test(dataProvider = "negativeTestRandom", groups = "negative", alwaysRun = true, expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Отказано в доступе")
    public void cannotCreateFileInAReadOnlyDir(String fileNameExist, String fileNameException, String fileInReadOnlyDir) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        if (fileInReadOnlyDir != null && !fileInReadOnlyDir.isEmpty())
        {
            File negativeTestFile = new File(pathToTempFile, fileInReadOnlyDir);
            System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
        }
        else
            throw new SkipException("IGNORE\nfileName null or empty.");

    }
}
