package br.com.herick.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.herick.dscatalog.dto.ProductDTO;
import br.com.herick.dscatalog.entities.Product;
import br.com.herick.dscatalog.repositories.ProductRepository;
import br.com.herick.dscatalog.services.exceptions.DataBaseException;
import br.com.herick.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = categoryRepository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = categoryRepository.findById(id);
		Product entity = obj
				.orElseThrow(() -> new ResourceNotFoundException("Recurso com id " + id + " não encontrado"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
	//	entity.setName(dto.getName());
		entity = categoryRepository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = categoryRepository.getReferenceById(id);
		//	entity.setName(dto.getName());
			entity = categoryRepository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso com id " + id + " não encontrado");
		}

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteById(Long id) {
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso com id " + id + " não encontrado");
		}
		try {
			categoryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial");
		}
	}
}
