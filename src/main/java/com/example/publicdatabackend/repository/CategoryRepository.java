package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Category;
import com.example.publicdatabackend.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.categoryName, c.hashTags FROM Category c WHERE c.restaurant = :restaurant")
    List<Object[]> findCategoryDetailsByRestaurant(@Param("restaurant") Restaurant restaurant);
}
