package no.idporten.ansattporten_integration.util;

import com.google.zxing.WriterException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class GenerateQRCodeTest {

    private AutoCloseable closeable;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // Close mocks after each test
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGenerateQRCodeImage() throws IOException, WriterException {
        GenerateQRCode.generateQRCodeImage("text", 0, 0, "filePath");
    }
}

