package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {
    @InjectMocks
    ProductRepository productRepository;

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindByIdOnSecondProduct() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("First");
        product1.setProductQuantity(10);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Second");
        product2.setProductQuantity(20);
        productRepository.create(product2);

        Product found = productRepository.findById("id-2");
        assertNotNull(found);
        assertEquals("id-2", found.getProductId());
    }

    @Test
    void testUpdateProductSuccess() {
        Product product = new Product();
        product.setProductId("id-1");
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updated = new Product();
        updated.setProductId("id-1");
        updated.setProductName("New Name");
        updated.setProductQuantity(20);

        Product result = productRepository.update("id-1", updated);
        assertNotNull(result);
        assertEquals("New Name", result.getProductName());
        assertEquals(20, result.getProductQuantity());

        Product fetched = productRepository.findById("id-1");
        assertEquals("New Name", fetched.getProductName());
        assertEquals(20, fetched.getProductQuantity());
    }

    @Test
    void testUpdateProductNotFoundKeepsExistingData() {
        Product existing = new Product();
        existing.setProductId("id-1");
        existing.setProductName("Existing");
        existing.setProductQuantity(5);
        productRepository.create(existing);

        Product unknown = new Product();
        unknown.setProductId("missing");
        unknown.setProductName("Should Not Be Saved");
        unknown.setProductQuantity(99);

        Product result = productRepository.update("missing", unknown);
        assertNull(result);
        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
        Product stillThere = iterator.next();
        assertEquals("id-1", stillThere.getProductId());
        assertEquals("Existing", stillThere.getProductName());
        assertEquals(5, stillThere.getProductQuantity());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setProductId("id-1");
        product.setProductName("To Delete");
        product.setProductQuantity(1);
        productRepository.create(product);
        productRepository.delete("id-1");

        Iterator<Product> iterator = productRepository.findAll();
        assertFalse(iterator.hasNext());
        assertNull(productRepository.findById("id-1"));
    }

    @Test
    void testDeleteProductNonExistingDoesNothing() {
        Product product = new Product();
        product.setProductId("id-1");
        product.setProductName("Stay");
        product.setProductQuantity(1);
        productRepository.create(product);
        productRepository.delete("missing");

        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
        Product remaining = iterator.next();
        assertEquals("id-1", remaining.getProductId());
        assertEquals("Stay", remaining.getProductName());
        assertEquals(1, remaining.getProductQuantity());
        assertFalse(iterator.hasNext());
    }
}