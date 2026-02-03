package com.example.pm_web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pm_web.entity.Watchlist;

public interface watchlist extends JpaRepository<Watchlist, String>{
    List<Watchlist> findAll();
}
