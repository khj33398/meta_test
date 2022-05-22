package com.meta.ticket.repository;

import com.meta.ticket.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    //List<Item> findByName(String name);
    List<Item> findById(long id);

    //Entity 객체를 대상으로 쿼리를 수행하므로 데이터베이스에 독립적
    @Query("select i from Item i where i.name like %:testname% order by i.id desc")
    List<Item> findByName(@Param("testname") String testname);

    //nativeQuery : 특정 데이터베이스에 종속적
    @Query(value = "select * from item i where i.name like %:testname% order by i.id desc ", nativeQuery = true)
    List<Item> findByNameByNative(@Param("testname") String testname);
}
