package com.petrovma92.tests;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.activation.UnsupportedDataTypeException;
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
    private static Iterator<Object[]> negativeTestFromExel(Method m) throws IOException, JSONException, InvalidFormatException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+new Object(){}.getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        int length = m.getParameterTypes().length;

        return new FileIterator(new File( System.getProperty("user.dir") + "/src/test/resources/testData.xlsx"), length, 5, 3);
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

    @TempDir(read = false, write = true)
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

    private static class FileIterator implements Iterator<Object[]> {

        private int length;
        private XSSFSheet sheet;
        private int startReadFrom;
        private int maxTestDataCount;

        FileIterator(File file, int length, int startReadFrom, int maxTestDataCount) throws InvalidFormatException, IOException {
            this.length = length;
            this.startReadFrom = startReadFrom;
            this.maxTestDataCount = maxTestDataCount;


            // TODO: 03.07.17 create magic here
            OPCPackage pkg = OPCPackage.open(file);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            this.sheet = wb.getSheet("testData");

            System.out.println(wb.getSheet("testData").getRow(1).getCell(2).getStringCellValue());
            System.out.println(wb.getSheet("testData").getRow(1).getCell(0).getStringCellValue());
        }

        @Override
        public boolean hasNext() {
            int i = startReadFrom;
            while(i < maxTestDataCount + startReadFrom) {
                if(getCellValue(i, cuntTestRun+1) != null)
                    return true;
                i++;
            }
            return false;
        }

        @Override
        public Object[] next() {
            Object[] parameters = new Object[length];
            for (int i = 0; i < length; i++) {
                parameters[i] = getCellValue(i+startReadFrom, cuntTestRun);
            }
            cuntTestRun++;
            return parameters;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private String getCellValue(int testDataNumber, int column) {
            try {
                Cell cell = sheet.getRow(testDataNumber).getCell(column);
                if (cell != null) {
                    switch (cell.getCellTypeEnum()) {
                        case FORMULA:
                            return String.valueOf(cell.getCellFormula());
                        case NUMERIC:
                            return String.valueOf(cell.getNumericCellValue());
                        case STRING:
                            return cell.getStringCellValue();
                        case BLANK:
                            return null;
                        case BOOLEAN:
                            return String.valueOf(cell.getBooleanCellValue());
                        case ERROR:
                            return String.valueOf(cell.getErrorCellValue());
                        default:
                            throw new UnsupportedDataTypeException();
                    }
                }
                else return null;

            } catch (UnsupportedDataTypeException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}
