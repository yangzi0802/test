package com.test.pdf;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yangfq
 * @version V3.4
 * @Project: test
 * @Package com.test.pdf
 * @Description: TODO
 * @date Date : 2020年09月30日 9:50
 */
public class PdfUtil {

    public static void pdf2Pic() {
        try {
            URL url = new URL("https://powerlong-1300783495.cos.ap-shanghai.myqcloud.com/2020-09-08/6afdc9f2e7824974a91fdfd3ad2403f9.pdf");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();

            byte buffer[] = new byte[1024];
            int length;
            FileOutputStream os = new FileOutputStream("D:/a.pdf");
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();


            File file = new File("D:/a.pdf");
            // 总宽度
            int width = 0;
            // 保存一张图片中的RGB数据
            int[] singleImgRGB;
            // 保存每张图片的像素值
            int shiftHeight = 0;
            BufferedImage imageResult = null;
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            // BufferedImage image = null;
            for (int i = 0, len = pageCount; i < pageCount; i++) {
                /// image = renderer.renderImageWithDPI(i, 144);
                /// ImageIO.write(image, "jpg", file);
                BufferedImage image = renderer.renderImageWithDPI(i, 105, ImageType.RGB);
                int imageHeight = image.getHeight();
                int imageWidth = image.getWidth();
                // 计算高度和偏移量
                if (i == 0) {
                    // 使用第一张图片宽度;
                    width = imageWidth;
                    // 保存每页图片的像素值
                    imageResult = new BufferedImage(width, imageHeight * len, BufferedImage.TYPE_INT_RGB);
                } else {
                    // 计算偏移高度
                    shiftHeight += imageHeight;
                }
                singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
                // 写入流中
                imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
            }

            // 写图片
            ImageIO.write(imageResult, "jpg", new File("d:/a.jpg"));
            doc.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void word2003ToHtml() {
        URL url = null;
        try {
            url = new URL("https://powerlong-1300783495.cos.ap-shanghai.myqcloud.com/2020-09-24/bb101a5642e94a0d9693403834bc71e5.doc");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            HWPFDocument wordDocument = new HWPFDocument(is);
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            /*wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                                          float heightInches) {
                    String fileName = AliOssUtil.generateImageFileName() + suggestedName.substring(suggestedName.lastIndexOf("."));
                    return uploadFileUtil.uploadFile(content, bucket, directory, fileName, visitPoint);
                }
            });*/
            // 解析word文档
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream outStream = new BufferedOutputStream(baos);
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            String content = baos.toString();
            baos.close();
            System.out.println(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public static void word07ToHtml() {
        URL url = null;
        try {
            url = new URL("https://powerlong-1300783495.cos.ap-shanghai.myqcloud.com/2020-09-09/b3582309ba534f2ea5b3d93c69ba54a5.docx");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            XWPFDocument document = new XWPFDocument(is);
            // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
            XHTMLOptions options = XHTMLOptions.create();
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            // 3) 将 XWPFDocument转换成XHTML
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, baos, options);
            String content = baos.toString();
            baos.close();
            System.out.println(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void pdfToHtml() {

        URL url = null;
        try {
            url = new URL("https://powerlong-1300783495.cos.ap-shanghai.myqcloud.com/2020-09-08/6afdc9f2e7824974a91fdfd3ad2403f9.pdf");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();

            byte buffer[] = new byte[1024];
            int length;
            FileOutputStream os = new FileOutputStream("D:/a.pdf");
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();

            File file = new File("D:/a.pdf");
            PDDocument doc = PDDocument.load(file);
            PDFDomTree pdfDomTree = new PDFDomTree();
            // pdfDomTree.writeText(doc, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:/a.html")),"UTF-8")));
            /*pdfDomTree.createDOM(doc);
            Document document = pdfDomTree.getDocument();
            System.out.println(document);*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // pdf2Pic();

        // word2003ToHtml();

        // word07ToHtml();

        pdfToHtml();
    }

}
