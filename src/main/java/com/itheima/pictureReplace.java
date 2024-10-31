package com.itheima;

import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class pictureReplace {


    public static void main(String[] args) throws Exception {
        String imagePath = "C:\\Users\\Administrator\\Pictures\\测试用图\\3.jpeg";

        // 读取图片文件到字节数组
        Path path = new File(imagePath).toPath();
        byte[] imageBytes = Files.readAllBytes(path);

        // 对字节数组进行Base64编码
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\测试文档.docx"));
        XWPFDocument document = new XWPFDocument(fis);

        for (PackageRelationship relationship : document.getPackagePart().getRelationships()) {
            String id = relationship.getId();
            System.out.println(id);
            URI targetURI = relationship.getTargetURI();
            System.out.println(targetURI.getPath());
        }


        try {
            replaceImg(document, base64String, "rId4");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        is.close();
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        return bytes;
    }


    public static void replaceImg(XWPFDocument document, String base64, String rid) throws Exception {
        ByteArrayInputStream byteArrayInputStream = getBase64Input(base64);
        String ind = "";
        //已经改好了不用动了。
        try {
            document.getPackagePart().removeRelationship(rid);//删除原有印章
            ind = document.addPictureData(byteArrayInputStream,
                    XWPFDocument.PICTURE_TYPE_PNG);//在word插入一张没有关系的图片数据

            //将新插入的图片关系替换到模板图片上，替换的只是图片数据，大小位置还是原来模板的
            document.getPackagePart().addRelationship(document.getPackagePart().
                            getRelationship(ind).getTargetURI(), TargetMode.INTERNAL,
                    XWPFRelation.IMAGES.getRelation(), rid);


            // 写出到文件
            try (FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\测试文档.docx")) {
                document.write(out);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("替换图片失败");
        } finally {
            byteArrayInputStream.close();
        }
    }

    private static ByteArrayInputStream getBase64Input(String base64) {
        byte[] data = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return bis;
    }


//    public static void main(String[] args) throws Exception {
//
////        String imagePath = "C:\\Users\\Administrator\\Pictures\\测试用图\\2.jpeg";
////
////        // 读取图片文件到字节数组
////        Path path = new File(imagePath).toPath();
////        byte[] imageBytes = Files.readAllBytes(path);
////
////        // 对字节数组进行Base64编码
////        String base64String = Base64.getEncoder().encodeToString(imageBytes);
////        FileInputStream fis = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\测试文档.docx"));
//
//        File inputFile = new File("C:\\Users\\Administrator\\Desktop\\测试文档.docx");
//        File imageFile = new File("C:\\Users\\Administrator\\Pictures\\测试用图\\2.jpeg");
//
//        InputStream in = new FileInputStream(inputFile);
//        XWPFDocument doc = new XWPFDocument(in);
//
//        // 替换第一张图片
//        List<XWPFPictureData> picList = doc.getAllPictures();
//        if (picList.size() > 0) {
//            byte[] bytes = getBytesFromFile(imageFile);
//            String prefix = imageFile.getName().substring(imageFile.getName().lastIndexOf('.') + 1);
//            String pictureId = doc.addPictureData(bytes, Document.PICTURE_TYPE_PNG);
//            XWPFRun run = doc.getParagraphs().get(0).getRuns().get(0); // 假设替换第一个段落的第一个文本 run
//            run.setText("", 0); // 清除原图片占位符
//
//            run.addPicture(new FileInputStream(imageFile), picList.get(0).getPictureType(), run.getPictureText(), Units.toEMU(200), Units.toEMU(200));
//        }
//
//        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\测试文档2.docx");
//        doc.write(out);
//        out.close();
//        in.close();
//    }
//
//    private static byte[] getBytesFromFile(File file) throws IOException {
//        InputStream is = new FileInputStream(file);
//        long length = file.length();
//        byte[] bytes = new byte[(int) length];
//        int offset = 0;
//        int numRead = 0;
//        while (offset < bytes.length
//                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
//            offset += numRead;
//        }
//        is.close();
//        if (offset < bytes.length) {
//            throw new IOException("Could not completely read file " + file.getName());
//        }
//        return bytes;
//    }

}
