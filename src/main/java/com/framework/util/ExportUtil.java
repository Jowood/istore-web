package com.framework.util;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 导出Excel操作类
 * User: Simple
 */
public class ExportUtil {

    /**
     * @param filePath
     * @param keys
     * @param mp
     * @param lst
     * @return
     */
    public static File writeXML(String filePath, String[] keys, Map mp, List<Map> lst) {
        File xlsFile = new File(filePath);
        FileWriter fw = null;
        try {
            fw = new FileWriter(xlsFile);
            fw.write("<?xml version='1.0' encoding='GBK'?>\n");
            fw.write("<?mso-application progid='Excel.Sheet'?>\n");
            fw.write("<Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet'\n");
            fw.write("xmlns:o='urn:schemas-microsoft-com:office:office'\n");
            fw.write("xmlns:x='urn:schemas-microsoft-com:office:excel'\n");
            fw.write("xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet'\n");
            fw.write("xmlns:html='http://www.w3.org/TR/REC-html40'>\n");
            fw.write("<DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'>\n");
            fw.write("<Author>Simple</Author>\n");
            fw.write("<LastAuthor>Simple</LastAuthor>\n");
            fw.write("<Created>2010-02-03 15:24:20</Created>\n");
            fw.write("<Company>Microsoft</Company>\n");
            fw.write("<Version>11.9999</Version>\n");
            fw.write("</DocumentProperties>\n");
            fw.write("<ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>\n");
            fw.write("<WindowHeight>9000</WindowHeight>\n");
            fw.write("<WindowWidth>16020</WindowWidth>\n");
            fw.write("<WindowTopX>0</WindowTopX>\n");
            fw.write("<WindowTopY>15</WindowTopY>\n");
            fw.write("<ProtectStructure>False</ProtectStructure>\n");
            fw.write("<ProtectWindows>False</ProtectWindows>\n");
            fw.write("</ExcelWorkbook>\n");
            fw.write("<Styles>\n");
            fw.write("<Style ss:ID='Default' ss:Name='Normal'>\n");
            fw.write("<Alignment ss:Vertical='Center'/>\n");
            fw.write("<Borders/>\n");
            fw.write("<Font ss:FontName='宋体' x:CharSet='134' ss:Size='12'/>\n");
            fw.write("<Interior/>\n");
            fw.write("<NumberFormat/>\n");
            fw.write("<Protection/>\n");
            fw.write("</Style>\n");
            fw.write("</Styles>\n");
            int count = lst.size();
            int size = 20000;
            int sheetcount = (count % size == 0) ? count / size : count / size + 1;
            for (int i = 1; i <= sheetcount; i++) {
                fw.write("<Worksheet ss:Name='第" + i + "页'>\n");
                fw.write("<Table ss:ExpandedColumnCount='" + keys.length + "' ss:ExpandedRowCount='" + (size + 1) + "' x:FullColumns='1'\n");
                fw.write("x:FullRows='1' ss:DefaultColumnWidth='54' ss:DefaultRowHeight='14.25'>\n");
                fw.write("<Row ss:AutoFitHeight='0'>\n");
                for (String key : keys) {
                    fw.write("<Cell><Data ss:Type='String'>" + ExportUtil.toString(mp.get(key)) + "</Data></Cell>\n");
                }
                fw.write("</Row>\n");
                for (Map m : lst) {
                    fw.write("<Row ss:AutoFitHeight='0'>\n");
                    for (String key : keys) {
                        fw.write("<Cell><Data ss:Type='String'>" + ExportUtil.toString(m.get(key)) + "</Data></Cell>\n");
                    }
                    fw.write("</Row>\n");
                }
                fw.write("</Table>\n");
                fw.write("<WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>\n");
                fw.write("<Unsynced/>\n");
                fw.write("<ProtectObjects>False</ProtectObjects>\n");
                fw.write("<ProtectScenarios>False</ProtectScenarios>\n");
                fw.write("</WorksheetOptions>\n");
                fw.write("</Worksheet>\n");
            }
            fw.write("</Workbook>\n");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
            try {
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return xlsFile;
    }

    /**
     * 压缩成zip
     *
     * @param filePath
     * @param file
     * @return
     */
    public static File reduceZIP(String filePath, File file) {
        byte[] buf = new byte[1024];
        //压缩文件名
        File zipFile = new File(filePath);
        ZipOutputStream zos = null;
        InputStream is = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipEntry ze = new ZipEntry(file.getName());
            ze.setSize(file.length());
            ze.setTime(file.lastModified());
            zos.putNextEntry(ze);
            is = new BufferedInputStream(new FileInputStream(file));
            int readLen = -1;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                zos.write(buf, 0, readLen);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
            try {
                if (is != null) is.close();
                if (zos != null) zos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return zipFile;
    }

    /**
     * 返回对象的String
     *
     * @param obj
     * @return
     */
    private static String toString(Object obj) {
        return (obj != null) ? obj.toString() : "";
    }
}
