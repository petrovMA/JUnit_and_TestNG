package com.petrovma92.tests;

import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class MainTest {
    private static File tempDir;
    static String pathToTempFile;

    static File fileSimpleName;
    static File fileNameWithExtension;
    static File fileSpecCharName;

    @BeforeMethod(groups = {"negative", "positive"})
    public void before(Method m) throws IOException {
        System.out.println("\u001B[35m\u001B[01m\n@BeforeMethod\u001B[35m\n"+getClass().getName() + "."+ new Object(){}.getClass().getEnclosingMethod().getName());

        boolean read = true, write = true;
        if(m.getAnnotation(TempDir.class) != null) {
            read = m.getAnnotation(TempDir.class).read();
            write = m.getAnnotation(TempDir.class).write();
        }
        System.out.println("setReadable("+read+") = " + tempDir.setReadable(read) + ";" + " setWritable("+write+") = " + tempDir.setWritable(write) + ";\u001B[0m");
    }

    @BeforeSuite(groups = {"negative", "positive"})
    private void preConditions() throws IOException {
        System.out.println("\u001B[32m\u001B[01m=============\n@BeforeSuite");
        File currentDirFile = new File(".");
        String helper = currentDirFile.getCanonicalPath();

        tempDir = Files.createTempDirectory(Paths.get(helper), "tmpDir").toFile();
        pathToTempFile = tempDir.getAbsolutePath();

        System.out.println("createTempDirectory\u001B[0m");
    }

    @AfterSuite(groups = {"negative", "positive"})
    private void postConditions() {
        System.out.println("\n\u001B[32m\u001B[01m@AfterSuite\u001B[0m");

        System.out.println("setReadable = " + tempDir.setReadable(true));
        System.out.println("setWritable = " + tempDir.setWritable(true));

        File[] files = new File(pathToTempFile).listFiles();

        if(files != null) {
            for (File f : files) {
                System.out.println("deleteFile fileName = " + f.getName() + " \t\t" + String.valueOf(f.delete()).toUpperCase());
            }
        }

        System.out.println("\u001B[32m\u001B[01mdeleteTempDirectory: " + String.valueOf(tempDir.delete()).toUpperCase() + "\n==============\u001B[0m");
    }

    String generateRandomString(int length, boolean upperChar, boolean lowerChar, boolean integer) {
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
}
