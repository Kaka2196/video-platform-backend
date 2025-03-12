package com.yl.videoplatformbackend.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WatermarkGenerator {

    public static void createTextWatermark(String text, String targetImagePath) throws IOException {
        // 设置水印的字体、颜色和透明度
        Font font = new Font("微软雅黑", Font.BOLD, 25);
        Color color = new Color(255, 255, 255, 128); // 半透明的白色文字

        // 创建透明背景的图像
        BufferedImage image = new BufferedImage(500, 100, BufferedImage.TYPE_INT_ARGB); // 可以根据视频分辨率调整
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 使文本半透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 透明度50%

        // 设置水印字体和颜色
        g2d.setFont(font);
        g2d.setColor(color);

        // 在图片的中央绘制水印文字
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int x = (image.getWidth() - fontMetrics.stringWidth(text)) / 2; // 水平居中
        int y = (image.getHeight() - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent(); // 垂直居中

        // 绘制文本
        g2d.drawString(text, x, y);

        // 释放资源
        g2d.dispose();

        // 保存图片
        ImageIO.write(image, "PNG", new File(targetImagePath));
    }

    public static void main(String[] args) {
        try {
            createTextWatermark("qwe || MyTube", "C:/Users/Leon/Desktop/watermark/watermark.png"); // 保存水印图片
            System.out.println("Watermark created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}