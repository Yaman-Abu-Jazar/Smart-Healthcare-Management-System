package com.exalt.healthcare.domain.repository.mongodb;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.awt.print.Pageable;
import java.util.List;

@NoRepositoryBean
public interface DocumentRepository<T, ID> extends MongoRepository<T, ID> {
}
