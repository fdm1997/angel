package com.fl.angel.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * @Author fdm
 * @Date 2020/11/10 16:57
 * @description：
 */
public class QrCodeUtil {
    /**
     * 编码格式
     */
    private static final String CHARSET = "utf-8";
    /**
     * 生成文件格式
     */
    public static String FORMAT = "JPG";
    /**
     * 二维码尺寸
     */
    private static  int QRCODE_SIZE = 300;
    /**
     * LOGO宽
     */
    private static  int LOGO_WIDTH = 60;
    /**
     * LOGO高
     */
    private static  int LOGO_HEIGHT = 60;

    /**
     * 生成二维码
     * @param content      二维码内容
     * @param logo     logo图片字节数组
     * @param needCompress 是否压缩logo
     * @return 图片
     * @throws Exception
     */
    public static BufferedImage createImage(String content, byte[] logo, boolean needCompress, int logWidth, int logHeight, int qrcoderSize) throws Exception {
        int coderSize = QRCODE_SIZE;
        int mywidth = LOGO_WIDTH;
        int myheight = LOGO_HEIGHT;
        if (qrcoderSize != 0){
            coderSize = qrcoderSize;
        }
        if (logWidth != 0){
            mywidth = logWidth;
        }
        if (logHeight != 0){
            myheight = logHeight;
        }
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, coderSize, coderSize,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logo == null ) {
            return image;
        }
        // 插入图片
        QrCodeUtil.insertImage(image, logo, needCompress,mywidth,myheight,coderSize);
        return image;
    }

    /**
     * 插入LOGO
     * @param source       二维码图片
     * @param logo     LOGO图片字节数组
     * @param needCompress 是否压缩
     * @throws IOException
     */
    private static void insertImage(BufferedImage source, byte[] logo, boolean needCompress,int logWidth,int logHeight,int qrcoderSize) throws IOException {
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(logo);
            Image src = ImageIO.read(inputStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            // 压缩LOGO
            if (needCompress) {
                if (width > logWidth) {
                    width = logWidth;
                }
                if (height > logHeight) {
                    height = logHeight;
                }
                Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制缩小后的图
                g.drawImage(image, 0, 0, null);
                g.dispose();
                src = image;
            }
            // 插入LOGO
            Graphics2D graph = source.createGraphics();
            int x = (qrcoderSize - width) / 2;
            int y = (qrcoderSize - height) / 2;
            graph.drawImage(src, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    /**
     * 生成二维码（带logo,可以指定的本地文件系统地址,二维码大小和logo大小使用默认大小，logo可以指定是否压缩）
     * @param content 内容
     * @param logo LOGO字节数组
     * @param needCompress 是否压缩LOGO
     * @param path  将二维码文件写入本地指定地址
     * @return
     * @throws Exception
     */
    public static void encode(String content, byte[] logo, boolean needCompress,String path) throws Exception {
        File file = new File(path);
        OutputStream outputStream = new FileOutputStream(file);
        BufferedImage image = QrCodeUtil.createImage(content, logo, needCompress,0,0,0);
        ImageIO.write(image, FORMAT, outputStream);
    }

    /**
     *生成二维码（带logo，返回二维码图片字节数组,二维码大小和logo大小使用默认大小，logo可以指定是否压缩）
     * @param content 内容
     * @param logo LOGO输入流
     * @param needCompress 是否压缩LOGO
     * @return 将二维码文件写入本地指定地址
     * @throws Exception
     */
    public static byte[] encode(String content, byte[] logo, boolean needCompress) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = QrCodeUtil.createImage(content, logo, needCompress,0,0,0);
        ImageIO.write(image, FORMAT, outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }

    /**
     *生成二维码（不带logo,二维码大小使用默认大小）
     * @param content 内容
     * @return二维码字节数组
     * @throws Exception
     */
    public static byte[] encode(String content) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = QrCodeUtil.createImage(content, null, false,0,0,0);
        ImageIO.write(image, FORMAT, outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }

    /**
     * 生成二维码（带logo,返回二维码图片字节数组,二维码大小和logo大小使用默认大小，可指定是否压缩，可设定logo宽高，可指定二维码大小）
     * @param content  内容
     * @param logo  二维码大小
     * @param needCompress  是否压缩LOGO
     * @param logWidth  LOGO宽
     * @param logHeight LOGO高
     * @param qrcoderSize  二维码尺寸
     * @return
     * @throws Exception
     */
    public static byte[] encode(String content, byte[] logo, boolean needCompress,int logWidth,int logHeight,int qrcoderSize) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = QrCodeUtil.createImage(content, logo, needCompress,logWidth,logHeight,qrcoderSize);
        ImageIO.write(image, FORMAT, outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }

    /**
     * 生成二维码（不带logo,返回二维码图片字节数组,可指定二维码大小）
     * @param content  内容
     * @param qrcoderSize  二维码大小
     * @return   二维码字节数组
     * @throws Exception
     */
    public static byte[] encode(String content,int qrcoderSize) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = QrCodeUtil.createImage(content, null, false,0,0,qrcoderSize);
        ImageIO.write(image, FORMAT, outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }

    /**
     * 生成二维码（带logo,返回二维码图片字节数组,二维码大小和logo大小使用默认大小，可指定是否压缩，可设定logo宽高，可指定二维码大小）
     * @param content  内容
     * @param logo  二维码大小
     * @param needCompress  是否压缩LOGO
     * @param logWidth  LOGO宽
     * @param logHeight LOGO高
     * @param qrcoderSize  二维码尺寸
     * @param format  二维码文件格式
     * @return  二维码字节数组
     * @throws Exception
     */
    public static byte[] encode(String content, byte[] logo, boolean needCompress,int logWidth,int logHeight,int qrcoderSize,String format) throws Exception{
        if (format != null && "".equals(format)){
            FORMAT = format;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = QrCodeUtil.createImage(content, logo, needCompress,logWidth,logHeight,qrcoderSize);
        ImageIO.write(image, FORMAT, outputStream);
        byte[] data = outputStream.toByteArray();

        return data;
    }
}
