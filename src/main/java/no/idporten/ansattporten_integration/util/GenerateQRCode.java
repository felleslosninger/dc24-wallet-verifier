package no.idporten.ansattporten_integration.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Utility class for generating QR Code images.
 */
public class GenerateQRCode {

    // Private constructor to hide the public one.
    private GenerateQRCode () {

    }

    /**
     * Generates a QR Code with the specified text, dimensions, and file path.
     *
     * @param text the text to encode in the QR Code
     * @param width the width of the QR Code image
     * @param height the height of the QR Code image
     * @param filePath the file path where the QR Code image will be saved
     * @throws WriterException if an error occurs while encoding the text to QR Code
     * @throws IOException if an error occurs while writing the QR Code image to file
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Encode the text to a BitMatrix representing the QR code
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Get the file path and write the BitMatrix to an image file
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
