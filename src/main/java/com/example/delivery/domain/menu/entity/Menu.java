package com.example.delivery.domain.menu.entity;
import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "menus")
public class Menu extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String menuName;
  private Long price;
  //가게와의 연관 관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="store_id", nullable = false)
  private Store store;

  private LocalDateTime deletedAt = null;

  public Menu(String menuName, Long price,Store store){
    this.menuName = menuName;
    this.price = price;
    this.store = store;
    this.deletedAt = null;
  }

  // 소프트 딜리트 메서드
  public void delete(){
    this.deletedAt = LocalDateTime.now();
  }
  // 메뉴 수정 메서드
  public void update(String menuName, Long price){
    this.menuName = menuName;
    this.price = price;
  }

}