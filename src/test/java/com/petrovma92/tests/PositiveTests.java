package com.petrovma92.tests;

import com.petrovma92.tests.annotations.GetDataFromFile;
import com.petrovma92.tests.annotations.Unstable;
import com.petrovma92.tests.rules.RunAgainRule;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static com.petrovma92.tests.MainTest.*;

@RunWith(DataProviderRunner.class)
public class PositiveTests {

    @Rule
    public TestRule runTwiceRule = new RunAgainRule();

    @Test
    @Unstable(2)
//    @GetDataFromFile("/src/test/resources/test-data.json")
    @GetDataFromFile()
    @UseDataProvider(value = "testData", location = MainTest.class)
    public void positiveTestCreateFile(String fileName) throws IOException {
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
    @Unstable(2)
//    @GetDataFromFile("/src/test/resources/test-data.json")
    @GetDataFromFile()
    @UseDataProvider(value = "testData", location = MainTest.class)
    public void positiveTestCreateFileWithExtension(String fileNameExtension) throws IOException {
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
    @Unstable(2)
//    @GetDataFromFile("/src/test/resources/test-data.json")
    @GetDataFromFile()
    @UseDataProvider(value = "testData", location = MainTest.class)
    public void positiveTestCreateFileWithSpecificChar(String specificChar) throws IOException {
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
