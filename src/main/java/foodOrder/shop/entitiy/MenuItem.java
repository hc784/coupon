package foodOrder.shop.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;          // 음식 이름

    @Column(nullable = false)
    private Integer price;        // 가격 (원 단위)

    @Column(length = 255)
    private String description;   // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;            // 어떤 가게의 메뉴인지
}
