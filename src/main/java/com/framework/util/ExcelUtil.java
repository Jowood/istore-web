package com.framework.util;

import jxl.CellType;
import jxl.format.CellFormat;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import java.io.File;
/**
 * Created by IntelliJ IDEA.
 * Author: 余飞
 * Date: 2011-1-25
 * Time: 13:30:07
 * To change this template use File | Settings | File Templates.
 */
public class ExcelUtil {
    /**
     * 修改数字单元格的值
     *
     * @param dataSheet WritableSheet : 工作表
     * @param col       int : 列
     * @param row       int : 行
     * @param str       String : 数值
     * @param format    CellFormat : 单元格的样式
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static void modiStrCell(WritableSheet dataSheet, int col, int row, String str, CellFormat format) throws RowsExceededException, WriteException {
        // 获得单元格对象
        WritableCell cell = dataSheet.getWritableCell(col, row);
        // 判断单元格的类型, 做出相应的转化
        if (cell.getType() == CellType.EMPTY) {
            Label lbl = new Label(col, row, str);
            if (null != format) {
                lbl.setCellFormat(format);
            } else {
                lbl.setCellFormat(cell.getCellFormat());
            }
            dataSheet.addCell(lbl);
        } else if (cell.getType() == CellType.LABEL) {
            Label lbl = (Label) cell;
            if (StringUtil.isEmpty(str)) {
                str = " ";
            }
            lbl.setString(str);
        } else if (cell.getType() == CellType.NUMBER) {
            // 数字单元格修改
            jxl.write.Number n1 = (Number) cell;
            n1.setValue(Double.parseDouble(str));
        }
    }

    /**
     * 插入图像到单元格（图像格式只支持png）
     *
     * @param dataSheet WritableSheet : 工作表
     * @param col       int : 列
     * @param row       int : 行
     * @param width     int : 宽
     * @param height    int : 高
     * @param imgName   String : 图像的全路径
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static void insertImgCell(WritableSheet dataSheet, int col, int row, int width,
                               int height, String imgName) throws RowsExceededException, WriteException {
        File imgFile = new File(imgName);
        WritableImage img = new WritableImage(col, row, width, height, imgFile);
        dataSheet.addImage(img);
    }
    /**
     * 插入图像到单元格（图像格式只支持png）
     *
     * @param dataSheet WritableSheet : 工作表
     * @param col       int : 列
     * @param row       int : 行
     * @param width     int : 宽
     * @param height    int : 高
     * @param imgbyte   byte[] : 图像的byte数组
     * @throws RowsExceededException
     * @throws WriteException
     */
    public static void insertImgCell(WritableSheet dataSheet, int col, int row, int width,
                               int height, byte[] imgbyte) throws RowsExceededException, WriteException {
        WritableImage img = new WritableImage(col, row, width, height, imgbyte);
        dataSheet.addImage(img);
    }
}
