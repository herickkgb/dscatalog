package br.com.herick.dscatalog.services;

import java.time.Instant;
import java.util.Optional;

import org.aspectj.weaver.patterns.TypeCategoryTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.herick.dscatalog.dto.CategoryDTO;
import br.com.herick.dscatalog.dto.ProductDTO;
import br.com.herick.dscatalog.entities.Category;
import br.com.herick.dscatalog.entities.Product;
import br.com.herick.dscatalog.repositories.CategoryRepository;
import br.com.herick.dscatalog.repositories.ProductRepository;
import br.com.herick.dscatalog.services.exceptions.DataBaseException;
import br.com.herick.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = productRepository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + id + " não encontrado"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = productRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso com id " + id + " não encontrado");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setimgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);
		}

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteById(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso com id " + id + " não encontrado");
		}
		try {
			productRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial");
		}
	}
}
