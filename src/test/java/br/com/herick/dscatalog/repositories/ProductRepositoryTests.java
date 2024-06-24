package br.com.herick.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import br.com.herick.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	@Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        // Arrange
        Long existingId = 1L;

        // Act
        repository.deleteById(existingId);
        Optional<Product> result = this.repository.findById(existingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
