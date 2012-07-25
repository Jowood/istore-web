package com.framework.security;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

/**
 * 随机验证码
 *
 * @author 姜敏
 */
public class ImgCheckCode implements Serializable {
    private String charRandomCode = "";
    //private BufferedImage imageRandomCode;
    private int width;
    private int height;
    private int length;

    /**
     * 构造函数
     *
     * @param width  图像验证码宽度
     * @param height 图像验证码高度
     */
    public ImgCheckCode(int width, int height) {
        this.width = width;
        this.height = height;
        this.length = 4;
        //create(4);
    }

    /**
     * 构造函数
     *
     * @param width  图像验证码宽度
     * @param height 图像验证码高度
     * @param length 验证码位数
     */
    public ImgCheckCode(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = 4;
        //create(length);
    }

    /**
     * 获得图像验证码宽度
     *
     * @return int 图像验证码宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 设置图像验证码宽度
     *
     * @param width 图像验证码宽度
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 获得图像验证码高度
     *
     * @return int 图像验证码高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 设置图像验证码高度
     *
     * @param height 图像验证码高度
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 获得随机验证码字符串
     *
     * @return 随机验证码字符串
     */
    public String getCharRandomCode() {
        return charRandomCode;
    }

    /**
     * 获得随机验证码图像
     *
     * @return 随机验证码图像
     */
    public BufferedImage getImageRandomCode() {
        return create(length);
    }

    private BufferedImage create(int length) {
        // 在内存中创建图象
        BufferedImage imageRandomCode = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = imageRandomCode.getGraphics();

        //生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandomColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            charRandomCode += rand;
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return imageRandomCode;
    }

    /**
     * 给定范围获得随机颜色
     *
     * @param fc
     * @param bc
     * @return Color
     */
    private Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
