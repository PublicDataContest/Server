package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    List<Menu> findByRestaurantId(Long restaurantId);
}
