package com.redis.repository;

import com.redis.dao.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for Crud Repository
 */

@Repository
public interface ItemRepository extends CrudRepository<Item, String> {}

