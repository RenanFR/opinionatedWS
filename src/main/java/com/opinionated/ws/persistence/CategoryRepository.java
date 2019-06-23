package com.opinionated.ws.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.opinionated.ws.domain.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String>{

}
