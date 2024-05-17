package codehanzoom.greenwalk.photo.domain.implement;

import codehanzoom.greenwalk.photo.domain.ImageName;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageResizer{

    private final MultipartFile image;
    private final ImageName imageName;

    public ImageResizer(MultipartFile image, ImageName imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public File getResizedImage() throws IOException {

        //이미지로 변환
        BufferedImage bufferedImage = this.getBufferedImage();
        int width = 640; int height = 640;

        //리사이징 실행 -> 리사이징 이미지 -> byte 배열 -> 이미지 파일
        BufferedImage scaledImage = resize(bufferedImage, width, height);
        byte[] bytes = toByteArray(scaledImage, imageName);
        return tofile(bytes, imageName);

    }

    // 파일을 BufferedImage로 읽기
    private BufferedImage getBufferedImage() {
        try {
            return ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage resize(BufferedImage image, int width, int height) {

        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = canvas.getGraphics();
        graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  // 리사이즈 옵션
        graphics.dispose();
        return canvas;
    }

    private byte[] toByteArray(BufferedImage result, ImageName imageName) {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(result, imageName.getExtension(), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File tofile(byte[] byteArray, ImageName imageName) throws IOException {

        // 바이트 배열을 ByteArrayInputStream으로 변환
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

        // ByteArrayInputStream을 BufferedImage로 변환
        BufferedImage inputimage = ImageIO.read(bis);

        // BufferedImage를 파일로 저장
        File image = new File(imageName.getFilename());
        ImageIO.write(inputimage, imageName.getExtension(), image);

        return image;
    }
}
