package com.meta.ticket.repository;

import com.meta.ticket.domain.Item;
import com.meta.ticket.domain.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        for(int i=1; i<=89; i++){
            Item item = new Item();
            item.setName("테스트 상품"+i);
            item.setPrice(10010+i);
            Item savedItem = itemRepository.save(item);
        }
    }

    //@Test
    @DisplayName("상품명 조회 테스트")
    public void findByIdTest(){
        this.createItemList();
        //List<Item> itemList = itemRepository.findByName("테스트 상품3");
        List<Item> itemList = itemRepository.findById(75);
        for(Item tmp:itemList){
            System.out.println(tmp.toString());
        }
    }

    //@Test
    @DisplayName("모든 상품 조회")
    public void findAllTest(){
        List<Item> list = itemRepository.findAll();
        for(Item item:list){
            System.out.println(item.toString());
        }
    }

    //@Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByNameTest(){
        List<Item> list = itemRepository.findByName("상품6");
        for(Item item : list){
            System.out.println(item.toString());
        }
    }

    //@Test
    @DisplayName("@NativeQuery를 이용한 상품 조회 테스트")
    public void findByNameByNativeTest(){
        List<Item> list = itemRepository.findByNameByNative("상품7");
        for(Item item : list){
            System.out.println(item.toString());
        }
    }

    @PersistenceContext
    EntityManager em;

    //@Test
    //repository 인터페이스에 미리 선언해놓지 않고 실행 시점에 쿼리할 수 있음
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // repository 유사
        QItem qItem = QItem.item; // entity 객체 유사
        JPAQuery<Item> query = queryFactory.selectFrom(qItem).
                where(qItem.name.eq("테스트 상품18")).
                orderBy(qItem.id.desc());

        List<Item> list = query.fetch(); //fetch() 메서드 실행 시점에 쿼리문이 실행됨

        for(Item item : list){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트 2")
    public void queryDslTest2(){
        /*
        Item item = new Item();
        item.setName("테스트 상품150");
        item.setPrice(10028);
        itemRepository.save(item);
        */

        //쿼리에 들어갈 조건을 만들어주는 빌더
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        int price = 10035;
        String name = "테스트 상품18";

        //booleanBuilder.and(qItem.name.eq(name));
        booleanBuilder.and(qItem.price.gt(price));

        Sort sort1 = Sort.by("id").descending();
        Sort sort2 = Sort.by("name").descending();
        Sort sort3 = sort1.and(sort2);

        //Pageable pageable = PageRequest.of(1,5);
        Pageable pageable = PageRequest.of(1,5, sort1);
        Page<Item> page = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println(page.getTotalElements());

        List<Item> list = page.getContent();
        for(Item temp:list){
            System.out.println(temp.toString());
        }

    }
}