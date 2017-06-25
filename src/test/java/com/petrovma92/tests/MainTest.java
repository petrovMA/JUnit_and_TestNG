package com.petrovma92.tests;

import com.petrovma92.tests.exceptions.TestFileNotFoundException;
import com.petrovma92.tests.exceptions.TestMethodHasNoAnnotationException;
import com.petrovma92.tests.exceptions.UnsupportedDataProviderTestMethodException;
import com.petrovma92.tests.annotations.GetDataFromFile;
import com.tngtech.java.junit.dataprovider.DataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(Suite.class)
@SuiteClasses({
        PositiveTests.class,
        NegativeTests.class
})
public class MainTest {
    private static File tempFile;
    static String pathToTempFile;

    static File fileSimpleName;
    static File fileNameWithExtension;
    static File fileSpecCharName;

    @ClassRule
    public static ExternalResource suiteRule = new ExternalResource() {

        @Override
        protected void before() throws IOException {
            System.out.println("\u001B[32m\u001B[01m=============\n@ClassRule(Before)");
            File currentDirFile = new File(".");
            String helper = currentDirFile.getCanonicalPath();

            tempFile = Files.createTempDirectory(Paths.get(helper), "tmpDir").toFile();
            pathToTempFile = tempFile.getAbsolutePath();

            System.out.println("createTempDirectory\u001B[0m");
        }

        @Override
        protected void after() {
            System.out.println("\n\u001B[32m\u001B[01m@ClassRule(After)\u001B[0m");
            File[] files = new File(pathToTempFile).listFiles();

            if(files != null) {
                for (File f : files) {
                    System.out.println("deleteFile fileName = " + f.getName() + " \t\t" + String.valueOf(f.delete()).toUpperCase());
                }
            }

            System.out.println("\u001B[32m\u001B[01mdeleteTempDirectory: " + String.valueOf(tempFile.delete()).toUpperCase() + "\n==============\u001B[0m");
        }

    };

    @DataProvider
    public static Object[][] testData(FrameworkMethod method) throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+new Object(){}.getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        GetDataFromFile dataFromFile = method.getAnnotation(GetDataFromFile.class);
        if(dataFromFile == null) throw new TestMethodHasNoAnnotationException("Method has no @GetDataFromFile annotation: " + method.getName());

        List<Object[]> data = new ArrayList<>();

        if(dataFromFile.value().isEmpty())
            switch (method.getName()) {
                case "positiveTestCreateFile":
                    data.add(new Object[]{generateRandomString(20, false, true, false)});
                    data.add(new Object[]{generateRandomString(20, false, true, true)});
                    data.add(new Object[]{generateRandomString(20, true, false, true)});
                    data.add(new Object[]{generateRandomString(20, false, false, true)});
                    break;
                case "positiveTestCreateFileWithExtension":
                    data.add(new Object[]{
                            generateRandomString(20, false, true, false) + "." + generateRandomString(2, false, true, false)});
                    data.add(new Object[]{
                            generateRandomString(19, true, true, false) + "." + generateRandomString(3, false, true, false)});
                    data.add(new Object[]{
                            generateRandomString(18, true, true, true) + "." + generateRandomString(4, false, true, false)});
                    data.add(new Object[]{
                            generateRandomString(15, true, true, true) + "." + generateRandomString(7, true, true, false)});
                    break;
                case "positiveTestCreateFileWithSpecificChar":
                    data.add(new Object[]{generateRandomString(20, true, false, false)});
                    data.add(new Object[]{generateRandomString(20, false, true, false)});
                    data.add(new Object[]{generateRandomString(20, false, false, true)});
                    break;
                case "negativeTestCreateException":
                    data.add(new Object[]{generateRandomString(20, false, false, false)});
                    data.add(new Object[]{generateRandomString(0, false, true, false)});
                    data.add(new Object[]{null});
                    break;
                case "negativeTestCreateFileAlreadyExist":
                    data.add(new Object[]{generateRandomString(20, false, true, false)});
                    data.add(new Object[]{generateRandomString(20, false, true, true)});
                    data.add(new Object[]{generateRandomString(20, false, false, true)});
                    break;
                default:
                    throw new UnsupportedDataProviderTestMethodException("DataProvider not support method " + method.getName());
            }
        else
            switch (method.getName()) {
                case "positiveTestCreateFile":
                    data = getDataFomJsonFile(dataFromFile.value(), "positiveTestCreateFile");
                    break;
                case "positiveTestCreateFileWithExtension":
                    data = getDataFomJsonFile(dataFromFile.value(), "positiveTestCreateFileWithExtension");
                    break;
                case "positiveTestCreateFileWithSpecificChar":
                    data = getDataFomJsonFile(dataFromFile.value(), "positiveTestCreateFileWithSpecificChar");
                    break;
                case "negativeTestCreateException":
                    if (new File(System.getProperty("user.dir") + dataFromFile.value()).exists()) {
                        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/test-data.json")), StandardCharsets.UTF_8));
                        JSONArray array = json.getJSONArray("testData");
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                if(array.getJSONObject(i).getString("negativeTestCreateException").equalsIgnoreCase("null"))
                                        data.add(new Object[]{null});
                                else
                                    data.add(new Object[]{array.getJSONObject(i).getString("negativeTestCreateException")});
                            } catch (JSONException e) {
                                System.out.println(i + " data for negativeTestCreateException not found");
                            }
                        }
                    }
                    else throw new TestFileNotFoundException("");
                    break;
                case "negativeTestCreateFileAlreadyExist":
                    data = getDataFomJsonFile(dataFromFile.value(), "negativeTestCreateFileAlreadyExist");
                    break;
                default:
                    throw new UnsupportedDataProviderTestMethodException("DataProvider not support method " + method.getName());
            }
        return data.toArray(new Object[][]{});
    }

    private static List<Object[]> getDataFomJsonFile(String testFile, String testVariable) throws IOException, JSONException {
        List<Object[]> data = new ArrayList<>();
        if (new File(System.getProperty("user.dir") + testFile).exists()) {
            JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/test-data.json")), StandardCharsets.UTF_8));
            JSONArray array = json.getJSONArray("testData");
            for (int i = 0; i < array.length(); i++) {
                try {
                    data.add(new Object[]{array.getJSONObject(i).getString(testVariable)});
                } catch (JSONException e) {
                    System.out.println(i + " data for "+testVariable+" not found");
                }
            }
        }
        else throw new TestFileNotFoundException("");
        return data;
    }

    static String generateRandomString(int length, boolean upperChar, boolean lowerChar, boolean integer) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++) {
            if(upperChar && lowerChar && integer) {
                switch (random.nextInt(3)) {
                    case 0:
                        builder.append((char)(random.nextInt(26)+65));
                        break;
                    case 1:
                        builder.append((char)(random.nextInt(26)+97));
                        break;
                    case 2:
                        builder.append((char)(random.nextInt(10)+48));
                        break;
                }
            }
            else if(upperChar && lowerChar) {
                switch (random.nextInt(2)) {
                    case 0:
                        builder.append((char)(random.nextInt(26)+65));
                        break;
                    case 1:
                        builder.append((char)(random.nextInt(26)+97));
                        break;
                }
            }
            else if(upperChar && integer) {
                switch (random.nextInt(2)) {
                    case 0:
                        builder.append((char)(random.nextInt(26)+65));
                        break;
                    case 1:
                        builder.append((char)(random.nextInt(10)+48));
                        break;
                }
            }
            else if(lowerChar && integer) {
                switch (random.nextInt(2)) {
                    case 0:
                        builder.append((char)(random.nextInt(26)+97));
                        break;
                    case 1:
                        builder.append((char)(random.nextInt(10)+48));
                        break;
                }
            }
            else if(upperChar) {
                builder.append((char)(random.nextInt(26)+65));
            }
            else if(lowerChar) {
                builder.append((char)(random.nextInt(26)+97));
            }
            else if(integer) {
                builder.append((char)(random.nextInt(10)+48));
            }
            else return null;
        }

        return builder.toString();
    }

    public static File getTempFile() {
        return tempFile;
    }
}
