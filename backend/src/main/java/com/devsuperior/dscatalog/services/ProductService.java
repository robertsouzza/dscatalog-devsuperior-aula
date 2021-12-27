package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true) // operações e leitura lembra de colocar =true para melhorar a performace do
									// banco.
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
		/*
		 * // 01 - podemos fazer assim da moda antiga // uma lista de category para uma
		 * list categoryDTO , fazer um for. List<ProductDTO> listDTO = new
		 * ArrayList<>(); // instanciando uma lista vazia for (Product cat : list) { //
		 * percorrendo minha lista de categorias(list) paraq cada eemento da lista (cat)
		 * listDTO.add(new ProductDTO(cat)); // pego o elemento cat da lista e passo
		 * ele de argumento no construtor do ProductDTO (cat) }
		 */

		// 02 - podemos implementar da forma de expressão lambida, resumida e melhor

		return list.map(x -> new ProductDTO(x)); // e mando retornar meu listDTO
	}

	// busca paginada.

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id); // OBS 01 - efetiva o acesso ao banco de dados, ele vai lá no
															// banco de dados e trás os dados
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());  // retorna a lista de Categorias.
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}


	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id); // OBS 02 - não toca no banco de dados vai instanciar um objeto
														// provisorio, com os dados e esse id daquele objeto, só quando
														// mandar salvar ai sim que ele vai no banco de dados.
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			// não pode excluir uma categoria por causa da integridade.
			throw new DataBaseException("Integrety violation ");
		}

	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		// TODO Auto-generated method stub
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}	

}
