package br.com.herick.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.herick.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	private Long existingId;
	private Long noexistingId;

	@Autowired
	private ProductRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		this.existingId = 1L;
		this.noexistingId = 1000L;
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// Arrange

		// Act
		repository.deleteById(this.existingId);
		Optional<Product> result = this.repository.findById(this.existingId);

		// Assert
		Assertions.assertFalse(result.isPresent());
	}

}
