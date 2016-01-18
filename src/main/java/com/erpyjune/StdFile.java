package com.erpyjune;

import java.io.*;
import java.util.*;

/**
 * Created by oj.bae on 2016. 1. 18..
 */
public class StdFile {

    /**
     *
     * @param filePath
     * @param fileEncoding
     * @return
     * @throws Exception
     */
    public String fileReadToString(String filePath, String fileEncoding) throws Exception {
        String buffer;
        StringBuilder content = new StringBuilder();

        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileEncoding));
        while ((buffer = br.readLine()) != null) {
            content.append(buffer);
        }
        br.close();
        return content.toString();
    }

    /**
     *
     * @param filePath
     * @return
     */
    public List<String> fileReadLineToList(String filePath) {
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filePath));
            while ((sCurrentLine = br.readLine()) != null) {
                list.add(sCurrentLine.trim());
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        }
        return list;
    }

    /**
     *
     * @param filePath
     * @return
     */
    public Set<String> fileReadLineToSet(String filePath) {
        BufferedReader br = null;
        Set<String> set = new HashSet<String>();
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filePath));
            while ((sCurrentLine = br.readLine()) != null) {
                set.add(sCurrentLine.trim());
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        }
        return set;
    }


    /**
     *
     * @param data
     * @param filePath
     */
    public void fileWriteFromString(String data, String filePath) {
        BufferedWriter writer=null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }
        }
    }
}
