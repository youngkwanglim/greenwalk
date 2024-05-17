package codehanzoom.greenwalk.photo.domain;

public class ImageName {

    private static final String EXTENSION_DELIMITER = ".";
    private final String fileName;

    public ImageName(final String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return fileName.substring(fileName.lastIndexOf(EXTENSION_DELIMITER)+1);
    }

    public String getFilename() {
        return fileName;
    }
}
