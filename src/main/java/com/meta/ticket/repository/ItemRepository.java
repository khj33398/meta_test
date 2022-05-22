package com.meta.ticket.repository;

import com.meta.ticket.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);
    List<Item> findById(long id);


}
