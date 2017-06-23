package com.petrovma92.tests;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.petrovma92.tests.MainTest.*;

@RunWith(JUnitParamsRunner.class)
public class PositiveTests {

    @Rule
    public TestRule runTwiceRule = new RunAgainRule();

    @Parameters
    public Iterator<Object[]> positiveTestFromFile() throws IOException, JSONException {
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

    @Parameters
    public Iterator<Object[]> positiveTestRandom() throws IOException, JSONException {
        System.out.println("\u001B[36m\u001B[01m\n@DataProvider\u001B[36m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
                generateRandomString(20, false, true, false),
                generateRandomString(20, false, true, false) + "." +generateRandomString(2, false, true, false),
                generateRandomString(20, true, false, false)});

        data.add(new Object[]{
                generateRandomString(20, false, true, true),
                generateRandomString(19, true, true, false) + "." +generateRandomString(3, false, true, false),
                generateRandomString(20, false, true, false)});

        data.add(new Object[]{
                generateRandomString(20, true, false, true),
                generateRandomString(18, true, true, true) + "." +generateRandomString(4, false, true, false),
                generateRandomString(20, false, false, true)});

        data.add(new Object[]{
                generateRandomString(20, false, false, true),
                generateRandomString(15, true, true, true) + "." +generateRandomString(7, true, true, false),
                generateRandomString(20, false, false, false)});

        return data.iterator();
    }

    @Test
    @Unstable(2)
//    @Parameters(method = "positiveTestFromFile")
    @Parameters(method = "positiveTestRandom")
    public void positiveTestCreateFile(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n" + getClass().getName() + "." + new Object() {
        }.getClass().getEnclosingMethod().getName() + "\u001B[0m");

        Assume.assumeTrue(getTempFile().isDirectory());

        if (fileName != null && !fileName.isEmpty())
        {
            fileSimpleName = new File(pathToTempFile, fileName);
            Assert.assertTrue("Не удалось создать файл с именем: " + fileSimpleName.getName(), fileSimpleName.createNewFile());
            Assert.assertTrue("Файл с именем: \"" + fileSimpleName.getName() + "\" не найден", fileSimpleName.exists());
        }
        else
            System.out.println("\u001B[36mIGNORE");

    }

    @Test
    @Unstable(4)
    @Parameters(method = "positiveTestFromFile")
//    @Parameters(method = "positiveTestRandom")
    public void positiveTestCreateFileWithExtension(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        Assume.assumeTrue(getTempFile().isDirectory());

        if (fileNameExtension != null && !fileNameExtension.isEmpty())
        {
            fileNameWithExtension = new File(pathToTempFile, fileNameExtension);
            SoftAssertions softAssert = new SoftAssertions();
            softAssert.assertThat(fileNameWithExtension.createNewFile()).isTrue();
            softAssert.assertThat(fileNameWithExtension.getName().matches(".+\\..+")).isTrue();
            softAssert.assertThat(fileNameWithExtension.getName()).isEqualTo(fileNameExtension);
            softAssert.assertThat(fileNameWithExtension.exists()).isTrue();
            softAssert.assertAll();
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }

    @Test
    @Unstable(6)
//    @Parameters(method = "positiveTestFromFile")
    @Parameters(method = "positiveTestRandom")
    public void positiveTestCreateFileWithSpecificChar(String fileName, String fileNameExtension, String specificChar) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        Assume.assumeTrue(getTempFile().isDirectory());

        if (specificChar != null && !specificChar.isEmpty())
        {
            fileSpecCharName = new File(pathToTempFile, specificChar);
            Assert.assertTrue("Не удалось создать файл с именем: " + fileSpecCharName.getName(), fileSpecCharName.createNewFile());
            Assert.assertTrue("Файл не найден", fileSpecCharName.exists());
        }
        else
            System.out.println("\u001B[36mIGNORE");
    }
}
