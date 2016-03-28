package com.erpyjune;


import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by oj.bae on 2016. 1. 2..
 */
public class StdUtils {

    /**
     *
     * @param s
     * @return
     */
    public static boolean isAllDigitChar(String s) {
        for (char ch : s.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isAllFloatChar(String s) {
        for (char ch : s.toCharArray()) {
            if (!Character.isDigit(ch) && !(ch == '.')) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param src
     * @param startTag
     * @param endTag
     * @return
     */
    public String getFieldData(String src, String startTag, String endTag) {
        if (src==null || startTag==null || endTag==null) return "";
        int spos = src.indexOf(startTag);
        if (spos<0) return "";
        int epos = src.indexOf(endTag, spos+1);
        if (epos<0) {
            epos = src.length(); // 맨끌 길이를 준다.
        }
        return src.substring(spos + startTag.length(), epos);
    }

    /**
     *
     * @param src
     * @param startTag
     * @param endTag
     * @return
     */
    public List<String> getFieldDataList(String src, String startTag, String endTag) {
        if (src==null || startTag==null || endTag==null) return null;
        int spos, epos, npos=0;
        List<String> dataList = new ArrayList<String>();

        while (true) {
            spos = src.indexOf(startTag, npos);
            if (spos<0) break;
            epos = src.indexOf(endTag, spos+1);
            if (epos<0) break;
            dataList.add(src.substring(spos + startTag.length(), epos));
            npos = epos;
        }
        return dataList;
    }

    /**
     *
     * @param src
     * @param startTag
     * @return
     */
    public String getFieldData(String src, String startTag) {
        if (src==null || startTag==null) return "";
        int spos = src.indexOf(startTag);
        if (spos<0) return "";
        if ((spos + startTag.length()) > src.length()) return "";
        return src.substring(spos + startTag.length());
    }

    /**
     *
     * @param s
     * @return
     */
    public String priceDataCleaner(String s) {
        if (s==null) return "";
        return s.replace("원", "").replace("won", "").replace(",", "").
                replace("<b>", "").replace("</b>", "").replace("판매가", "").replace(" ","").
                replace(":", "").replace("이벤트가", "").replace("개 구매중","").replace("개구매중","").
                replace("%", "").replace("~","").replace("개","").replace("구매", "").replace("HIT","").
                replace("할인가", "").replace("￦","").trim();
    }

    /**
     *
     * @param s
     * @return
     */
    public String htmlCleaner(String s) {
        if (s==null) return "";
        return s.replace("&lt;", "<").replace("&gt;",">").replace("[", "").replace("]", "").replace(",", " ");
    }


    /**
     *
     * @param crawlKeyword
     * @param bMan
     * @param bWoman
     * @return
     */
    public String isSexKeywordAdd(String crawlKeyword, boolean bMan, boolean bWoman) {
        StringBuilder sb = new StringBuilder(crawlKeyword);
        if (bMan) sb.append(" 남자");
        if (bWoman) sb.append(" 여자");
        return sb.toString();
    }

    /**
     *
     * @param md5
     * @return
     */
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(String.format(" Hash value generated - (%s)", md5));
        }
        return "";
    }

    /**
     *
     * @param localPath
     * @param cpName
     * @param url
     * @param fileName
     * @throws Exception
     */
    public String saveDiskImgage(String localPath, String cpName, String url, String fileName) throws Exception {
        String saveFullPath="";
        String file_ext = fileName.substring(
                fileName.lastIndexOf('.') + 1,
                fileName.length());

        BufferedImage image;

        image = ImageIO.read(new URL(url));
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_BGR);

        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.drawImage(image, 0, 0, null);

        saveFullPath = localPath + File.separator + cpName + File.separator + fileName;
        ImageIO.write(bufferedImage, file_ext, new File(saveFullPath));
        System.out.println(" Downloaded : " + url);
        System.out.println(" Save path : " + saveFullPath);

        return saveFullPath;
    }

    /**
     *
     * @param url
     * @return
     */
    public String splieImageFileName(String url) {
        String[] array = url.split("/");
        return array[array.length-1];
    }


    /**
     *
     * @param filePath
     * @param outputFile
     * @throws Exception
     */
    public void makeThumbnail(String filePath, String outputFile) throws Exception {
        try {
            BufferedImage sourceImage = ImageIO.read(new File(filePath));
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();

            if(width>height){
                float extraSize=    height-100;
                float percentHight = (extraSize/height)*100;
                float percentWidth = width - ((width/100)*percentHight);
                BufferedImage img = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);
                ImageIO.write(img2, "jpg", new File(outputFile));
            }else{
                float extraSize=    width-100;
                float percentWidth = (extraSize/width)*100;
                float  percentHight = height - ((height/100)*percentWidth);
                BufferedImage img = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);


                ImageIO.write(img2, "jpg", new File(outputFile));
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }


    /**
     *
     * @param filePath
     * @param outputFile
     * @throws Exception
     */
    public void makeThumbnailTest(String filePath, String outputFile) throws Exception {
        try {
            BufferedImage sourceImage = ImageIO.read(new File(filePath));
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();

            if(width>height){
                float extraSize=    height-100;
                float percentHight = (extraSize/height)*100;
                float percentWidth = width - ((width/100)*percentHight);
                BufferedImage img = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);
                ImageIO.write(img2, "jpg", new File(outputFile));
            }else{
                float extraSize=    width-100;
                float percentWidth = (extraSize/width)*100;
                float  percentHight = height - ((height/100)*percentWidth);
                BufferedImage img = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2 = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);
                ImageIO.write(img2, "jpg", new File(outputFile));
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }


    /**
     *
     * @param loadFile
     * @param saveFile
     * @param maxDim
     * @return
     * @throws IOException
     */
    public boolean makeThumbnail2(String loadFile, String saveFile, int maxDim)
            throws IOException {
        File save = new File(saveFile.replaceAll("/", "\\" + File.separator));
        FileInputStream fis = new FileInputStream(loadFile.replaceAll("/", "\\"
                + File.separator));
        BufferedImage im = ImageIO.read(fis);
        Image inImage = new ImageIcon(loadFile).getImage();
        double scale = (double) maxDim / (double) inImage.getHeight(null);
        if (inImage.getWidth(null) > inImage.getHeight(null)) {
            scale = (double) maxDim / (double) inImage.getWidth(null);
        }
        int scaledW = (int) (scale * inImage.getWidth(null));
        int scaledH = (int) (scale * inImage.getHeight(null));
        BufferedImage thumb = new BufferedImage(scaledW, scaledH,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = thumb.createGraphics();
        g2.drawImage(im, 0, 0, scaledW, scaledH, null);
        return ImageIO.write(thumb, "jpg", save);
    }

    /**
     *
     * @param str
     * @param delimeter
     * @return
     * @throws Exception
     */
    public List<String> tokenizerByTag(String str, String delimeter) throws Exception {
        String data;
        List<String> list = new ArrayList<String>();

        StringTokenizer st1 = new StringTokenizer(str, delimeter);
        while(st1.hasMoreTokens()){
            data = st1.nextToken();
            if(!st1.equals(delimeter)){
                list.add(data);
//                System.out.println(data);
            }
        }

        return list;
    }


    /**
     *
     * @param src
     * @param startTag
     * @param endTag
     * @return
     * @throws Exception
     */
    public String removeData(String src, char startTag, char endTag) throws Exception {
        char ch;
        boolean extractStart=false;

        StringBuilder sb = new StringBuilder();
        for (int i=0;i<src.length(); i++) {
            ch = src.charAt(i);
            if (ch == startTag) {
                extractStart=true;
            } else if (ch == endTag) {
                extractStart = false;
            } else {
                if (!extractStart) sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getCurrDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     *
     * @return
     */
    public String getCurrDateTimeString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     *
     * @return
     */
    public String getCurrDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    /**
     *
     * @param option
     * @return
     */
    public String getCurrDateOption(String option) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(option); // yyyyMMdd HH:mm:ss
        return sdf.format(date);
    }

    /**
     *
     * @param beforeAfterMinutes
     * @return
     */
    public String getMinutesBeforeAfter(int beforeAfterMinutes) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.add(Calendar.MINUTE, beforeAfterMinutes); // -30, +10
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param beforeAfterMinutes
     * @return
     */
    public String getMinutesBeforeAfterString(int beforeAfterMinutes) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
        cal.add(Calendar.MINUTE, beforeAfterMinutes); // -30, +10
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param beforeAfterHour
     * @return
     */
    public String getHourBeforeAfter(int beforeAfterHour) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.add(Calendar.HOUR, beforeAfterHour); // -3, +2
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param beforeAfterHour
     * @return
     */
    public String getHourBeforeAfterString(int beforeAfterHour) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
        cal.add(Calendar.HOUR, beforeAfterHour); // -3, +2
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param beforeAfterDay
     * @return
     */
    public String getDayBeforeAfter(int beforeAfterDay) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.add(Calendar.DATE, beforeAfterDay); // -3, +2
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param beforeAfterDay
     * @return
     */
    public String getDayBeforeAfterString(int beforeAfterDay) {
        String today;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
        cal.add(Calendar.DATE, beforeAfterDay); // -3, +2
        today = sdformat.format(cal.getTime());
        return today.replace(" ","").replace("-","").replace(":","");
    }

    /**
     *
     * @param totalSize
     * @return
     */
    public int getRandomNumber(int totalSize) {
        Random oRandom = new Random();
        return oRandom.nextInt(totalSize);
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     *
     * @param s
     * @return
     */
    public String removeSpace(String s) {
        int index=0;
        char[] temp = new char[64];

        for (char c : s.toCharArray()) {
            if (!Character.isSpaceChar(c)) {
                temp[index++] = c;
            }
            if (index>50) break;
        }
        return String.valueOf(temp);
    }

    /**
     *
     * @param sourceFile
     * @param destFile
     * @throws Exception
     */
    public void makeThumbnailator(String sourceFile, String destFile, int width, int height) throws Exception {
        System.out.println(" Make thumbnail [" + destFile + "]");
        try {
            Thumbnails.of(sourceFile)
                    .size(width, height)
                    .toFile(destFile);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     *
     * @param filePath
     * @param tag
     * @return
     * @throws Exception
     */
    public String insertStringToFileExtFront(String filePath, String tag) throws Exception {
        if (filePath==null || tag==null) return "";
        int pos = filePath.lastIndexOf(".");
        if (pos<0) {
            System.out.println("ERROR not found comma [" + filePath + "]");
            return "";
        }
        String fileFront = filePath.substring(0, pos);
        String fileExt   = filePath.substring(pos, filePath.length());

        return fileFront + tag + fileExt;
    }

    public void saveImage(String imageUrl, String destinationFile, Map<String,String> properties) throws Exception {
        System.out.println("Download [" + destinationFile + "]");
        File picutreFile = new File(destinationFile);
        URL url=new URL(imageUrl);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4");
        conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Upgrade-Insecure-Requests","1");
        if (properties != null && properties.size()>0) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
                System.out.println(String.format("set request header[%s=%s", entry.getKey(), entry.getValue()));
            }
        }
        conn.connect();
        FileUtils.copyInputStreamToFile(conn.getInputStream(), picutreFile);
    }

    private void makeThumbnailTest() throws Exception {
        StdUtils stdUtils = new StdUtils();
        Map<String,String> properties=null;
        String localPath="/Users/oj.bae/Work/BoardWang/thumb/JjangOu/";
        String imageUrl = "http://www.dogdrip.net/files/attach/dvs/16/02/08/78/787/882/090/70a10f193453494fff3b28cf57f985ce.jpg";
        int width = 50;
        int height = 50;

        System.out.println("ext  : " + FilenameUtils.getExtension("http://www.dogdrip.net/files/attach/dvs/16/02/09/78/975/912/090/f9e5e16ef13214a4848e96f702233443.jpg"));
        System.out.println("base : " + FilenameUtils.getBaseName("http://www.dogdrip.net/files/attach/dvs/16/02/09/78/975/912/090/f9e5e16ef13214a4848e96f702233443.jpg"));
        System.out.println("name : " + FilenameUtils.getName("http://www.dogdrip.net/files/attach/dvs/16/02/09/78/975/912/090/f9e5e16ef13214a4848e96f702233443.jpg"));
        System.out.println("path : " + FilenameUtils.getPath("http://www.dogdrip.net/files/attach/dvs/16/02/09/78/975/912/090/f9e5e16ef13214a4848e96f702233443.jpg"));
        System.out.println("prefix : " + FilenameUtils.getPrefix("http://www.dogdrip.net/files/attach/dvs/16/02/09/78/975/912/090/f9e5e16ef13214a4848e96f702233443.jpg"));

        String sourceFile = localPath + FilenameUtils.getName(imageUrl);
        stdUtils.saveImage(imageUrl, sourceFile, properties);
        String destFileName = stdUtils.insertStringToFileExtFront(sourceFile, "_thumb");
        stdUtils.makeThumbnailator(sourceFile, destFileName, width, height);
    }

    ///////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
        StdUtils stdUtils = new StdUtils();
        stdUtils.makeThumbnailTest();

    }
}
