package com.petrovma92.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
