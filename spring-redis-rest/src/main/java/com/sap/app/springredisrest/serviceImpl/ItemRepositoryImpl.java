package com.sap.app.springredisrest.serviceImpl;

import com.sap.app.springredisrest.model.Item;
import com.sap.app.springredisrest.service.ItemRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ItemRepositoryImpl implements ItemRepository {

    private RedisTemplate<String, Item> redisTemplate;
    private HashOperations hashOperations;

    public ItemRepositoryImpl(RedisTemplate<String, Item> redisTemplate){

        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(Item item) {
        hashOperations.put("ITEMS",item.getId(), item);
    }

    @Override
    public Map<String, Item> findAll() {
        return hashOperations.entries("ITEMS");
    }

    @Override
    public Item findById(String id) {
        return (Item)hashOperations.get("ITEMS", id);
    }

    @Override
    public void update(Item item) {
        save(item);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete("ITEMS", id);
    }
}
