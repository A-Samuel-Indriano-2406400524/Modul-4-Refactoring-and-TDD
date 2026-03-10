package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mockStatic;

class EshopApplicationTest {

    @Test
    void testMain() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            EshopApplication.main(new String[] {});
            springApplication.verify(() -> SpringApplication.run(EshopApplication.class, new String[] {}), times(1));
        }
    }
}
