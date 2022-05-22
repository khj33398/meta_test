package com.meta.ticket.repository;

import com.meta.ticket.domain.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    //@Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setRegTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList(){
        for(int i=1; i<=10; i++){
            Item item = new Item();
            item.setName("테스트 상품"+i);
            item.setPrice(10000+i);
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByIdTest(){
        //this.createItemList();
        //List<Item> itemList = itemRepository.findByName("테스트 상품3");
        List<Item> itemList = itemRepository.findById(4);
        for(Item tmp:itemList){
            System.out.println(tmp.toString());
        }
    }
}