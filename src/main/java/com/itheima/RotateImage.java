package com.itheima;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RotateImage {
//    public static void main(String[] args) {
//        try {
//            // 读取图片
//            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\测试用图\\1.jpg"));
//
//            // 旋转角度
//            double theta = Math.toRadians(90); // 假设旋转45度
//
//            // 计算旋转后的新尺寸
//            int newWidth = (int) Math.sqrt(originalImage.getWidth() * originalImage.getWidth() + originalImage.getHeight() * originalImage.getHeight());
//            int newHeight = (int) Math.sqrt(originalImage.getWidth() * originalImage.getWidth() + originalImage.getHeight() * originalImage.getHeight());
//
//            // 创建新的空白图片用于绘制旋转后的图片
//            BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
//
//            // 获取绘图环境
//            Graphics2D g2d = rotatedImage.createGraphics();
//
//            // 设置旋转的中心点为新图片的中心
//            AffineTransform at = new AffineTransform();
//            at.setToRotation(theta, newWidth / 2, newHeight / 2);
//            at.translate(newWidth / 2, newHeight / 2);
//
//            // 绘制旋转后的图片
//            g2d.setTransform(at);
//            g2d.drawImage(originalImage, -originalImage.getWidth() / 2, -originalImage.getHeight() / 2, null);
//
//            // 释放资源
//            g2d.dispose();
//
//            // 输出旋转后的图片
//            ImageIO.write(rotatedImage, "png", new File("C:\\Users\\Administrator\\Desktop\\output.png"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



//    }



    public static BufferedImage rotateImage(BufferedImage originalImage, double degree) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // 计算旋转后的新尺寸
        double sin = Math.abs(Math.sin(Math.toRadians(degree))),
                cos = Math.abs(Math.cos(Math.toRadians(degree)));
        int newWidth = (int) Math.floor(width * cos + height * sin),
                newHeight = (int) Math.floor(height * cos + width * sin);

        // 创建一个新的空白图片用于将原始图片绘制上去
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // 设置旋转的中心点为新图片的中心
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - width) / 2, (newHeight - height) / 2);
        at.rotate(Math.toRadians(degree), width / 2, height / 2);
        g2d.setTransform(at);

        // 将原始图片绘制到新图片上
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        // 返回旋转后的图片
        return rotatedImage;
    }

    public static void main(String[] args) throws IOException {
        //            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\测试用图\\1.jpg"));

//        File inputFile = new File("C:\\Users\\Administrator\\Pictures\\测试用图\\1.jpg"); // 输入图片路径
//        BufferedImage originalImage = ImageIO.read(inputFile);
//
//        BufferedImage rotatedImage = rotateImage(originalImage, 90); // 旋转45度
//
//        // 将旋转后的图片保存到文件
//        File outputFile = new File("C:\\Users\\Administrator\\Desktop\\output1.png"); // 输出图片路径
//        ImageIO.write(rotatedImage, "PNG", outputFile);
        filtImage();

    }



    public static void filtImage(){
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\测试用图\\1.jpg"));

            // 创建翻转的AffineTransform对象
            AffineTransform transform = new AffineTransform();
            transform.scale(1, -1); // 垂直翻转
            transform.translate(0, -originalImage.getHeight()); // 移动图片到翻转的位置

            // 创建AffineTransformOp对象
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            // 执行翻转操作
            BufferedImage flippedImage = op.filter(originalImage, null);

            // 保存翻转后的图片
            ImageIO.write(flippedImage, "jpg", new File("C:\\Users\\Administrator\\Desktop\\output1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}