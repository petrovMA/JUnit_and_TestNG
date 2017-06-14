package com.petrovma92.tests.task_1_1;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


@Test
public class PositiveTest extends MainTest {

    private File fileSimpleName;
    private File fileNameWithExtension;
    private File fileSpecCharName;

//    @BeforeClass
//    private void createTestFiles() throws IOException {
//        fileSimpleName = new File(pathToTempFile, "someFile");
//        fileNameWithExtension = new File(pathToTempFile, "someFile.txt");
//        fileSpecCharName = new File(pathToTempFile, "$%)()()(=+=&&#@!!");
//    }

    @AfterClass
    private void deleteTestFiles() throws IOException {
        System.out.println("deleteFile fileSimpleName: \t\t" + String.valueOf(fileSimpleName.delete()).toUpperCase());
        System.out.println("deleteFile fileNameWithExtension: \t" + String.valueOf(fileNameWithExtension.delete()).toUpperCase());
        System.out.println("deleteFile fileSpecCharName: \t\t" + String.valueOf(fileSpecCharName.delete()).toUpperCase());
    }

    public void testCreateFile() throws IOException {
        fileSimpleName = new File(pathToTempFile, "someFile");
        System.out.println("createFile fileSimpleName: \t\t" + String.valueOf(fileSimpleName.createNewFile()).toUpperCase());
    }

    public void testRenameFileWithExtension() throws IOException {
        fileNameWithExtension = new File(pathToTempFile, "someFile.txt");
        System.out.println("createFile fileNameWithExtension: \t" + String.valueOf(fileNameWithExtension.createNewFile()).toUpperCase());
    }

    public void testRemoveFileWithSpecificChar() throws IOException {
        fileSpecCharName = new File(pathToTempFile, "$%)()()(=+=&&#@!!");
        System.out.println("createFile fileSpecCharName: \t\t" + String.valueOf(fileSpecCharName.createNewFile()).toUpperCase());
    }

}
