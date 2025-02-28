package com.example.delivery.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "menus")
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String menuName;

  private Long price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="store_id", nullable = false)
  private Store store;

  private LocalDateTime created_at;

  private LocalDateTime updated_at;

  private LocalDateTime deleted_at;

}