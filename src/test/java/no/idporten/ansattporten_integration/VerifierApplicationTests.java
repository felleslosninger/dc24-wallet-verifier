package no.idporten.ansattporten_integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = VerifierApplication.class)
@AutoConfigureWebTestClient
class VerifierApplicationTests {

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
	void contextLoads() {
	}

}
