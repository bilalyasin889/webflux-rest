package com.example.webfluxrest.repositories;

import com.example.webfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Bilal Yasin
 */
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
