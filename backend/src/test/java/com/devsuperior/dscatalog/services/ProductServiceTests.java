package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {                                                                               // teste de unidade - simular comportamaneto em um objeto mocado.
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository catwgoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	ProductDTO productDTO;
	
	@BeforeEach
	void setUp()throws Exception{
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createproduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page); 
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(catwgoryRepository.getOne(existingId)).thenReturn(category);
		
		Mockito.when(catwgoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repository).deleteById(existingId);                                            // quando eu chamadr o dele byId com id existente esse método não vai fazer nada.
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionwhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() ->{
			service.update(nonExistingId, productDTO);
		});

	}
	
	
	@Test
	public void updateShouldReturnProductDTOwhenIdExists() {
		
		//ProductDTO productDTO = Factory.createProductDTO();
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
	}
	
	

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionwhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() ->{
			service.findById(nonExistingId);
		});

	}
	
	
	@Test
	public void findByIdShouldReturnProductDTOwhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	
	@Test
	public void findAllPageShouldReturPage() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}
	
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {                                          // chamar do service um id com dependen~encia de chave estrangeira vai ter que lançar a 
			
		Assertions.assertThrows(DataBaseException.class,() ->{
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId); 
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {                               // chamar do service um id que não existe vai ter que labnçar uma execessão.
			
		Assertions.assertThrows(ResourceNotFoundException.class,() ->{
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId); 
	}
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {                                                           // não deve fazer nada se o id existis apenas testar o service.
			
		Assertions.assertDoesNotThrow(() ->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);                                    // com times pode ter sido chamdo pelo menos uma vez.
	}

}
