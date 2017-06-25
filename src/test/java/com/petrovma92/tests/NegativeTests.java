package com.petrovma92.tests;

import com.petrovma92.tests.annotations.GetDataFromFile;
import com.petrovma92.tests.annotations.Unstable;
import com.petrovma92.tests.rules.RunAgainRule;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
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
public class NegativeTests {

    @Rule
    public TestRule runTwiceRule = new RunAgainRule();

    @Test(expected = Exception.class)
//    @Unstable(5)
//    @GetDataFromFile("/src/test/resources/test-data.json")
    @GetDataFromFile()
    @UseDataProvider(value = "testData", location = MainTest.class)
    public void negativeTestCreateException(String fileNameException) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        Assume.assumeTrue(getTempFile().isDirectory());

        File f = fileNameException == null ? null : fileNameException.isEmpty() ? new File(fileNameException) : new File(pathToTempFile, fileNameException);
        Assert.assertFalse(f.createNewFile());
    }

    @Test
    @Unstable(3)
//    @GetDataFromFile("/src/test/resources/test-data.json")
    @GetDataFromFile()
    @UseDataProvider(value = "testData", location = MainTest.class)
    public void negativeTestCreateFileAlreadyExist(String fileNameExist) throws IOException {
        System.out.println("\u001B[34m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName()+"\u001B[0m");

        Assume.assumeTrue(getTempFile().isDirectory());

        File negativeTestFile = new File(pathToTempFile, fileNameExist);
        System.out.println("createFile: " + String.valueOf(negativeTestFile.createNewFile()).toUpperCase());
        assert !negativeTestFile.createNewFile();
        assert negativeTestFile.exists();
    }
}
