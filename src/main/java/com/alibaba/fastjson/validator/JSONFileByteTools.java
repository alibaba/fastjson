package com.alibaba.fastjson.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JSONFileByteTools {
    private String JsonFileName;
    private File JsonFile;
    private FileInputStream FIS;

    JSONFileByteTools(String FileName) {
        JsonFileName = FileName;
        JsonFile = new File(FileName);
        FIS = null;
    }

    public byte[] getJsonByte() {

        byte [] fileContent = null;
        try {
            FIS = new FileInputStream(JsonFile);
            fileContent = new byte[(int)JsonFile.length()];
            FIS.read(fileContent);
            /*
            String s = new String(fileContent);
            System.out.println("File content: " + s);
             */
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (FIS != null) {
                    FIS.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
            return fileContent;
        }
    }
}

