package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)  //operações e leitura lembra de colocar  =true para melhorar a performace do banco.
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		                                               /*
		                                               // 01 - podemos fazer assim da moda antiga
		                                               // uma lista de category para uma list categoryDTO , fazer um for.
		List<CategoryDTO> listDTO = new ArrayList<>(); // instanciando uma lista vazia
		for (Category cat : list) {                    // percorrendo minha lista de categorias(list) paraq cada eemento da lista (cat)
			listDTO.add(new CategoryDTO(cat));         // pego o elemento cat da lista e passo ele de argumento no construtor do CategoryDTO (cat)
		}                                              */
		
		                                               // 02 - podemos implementar da forma de expressão lambida, resumida e melhor
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList()); // e mando retornar meu listDTO
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

}
