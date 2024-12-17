package com.example.demo.entity;

import com.example.demo.config.JPAConfiguration;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JPAConfiguration.class)
public class ItemEntityTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void itemStatusCannotBeNull() {
        // Given
        Item item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .build();

        // When
        Item savedItem = itemRepository.save(item); // 저장
        testEntityManager.flush();
        testEntityManager.clear(); // 영속성 컨텍스트 초기화

        // Then
        Item retrievedItem = itemRepository.findById(savedItem.getId())
                .orElseThrow(() -> new AssertionError("Item not found"));

        assertEquals(ItemStatus.PENDING, retrievedItem.getItemStatus(),
                "itemStatus의 기본값은 PENDING.");
    }
}
