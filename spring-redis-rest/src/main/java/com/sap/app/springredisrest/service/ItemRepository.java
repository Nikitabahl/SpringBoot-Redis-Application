package com.sap.app.springredisrest.service;

import com.sap.app.springredisrest.model.Item;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface ItemRepository {

    void save(Item item);

    Map<String, Item> findAll();

    Item findById(String id);

    void update(Item item);

    void delete(String id);

}
