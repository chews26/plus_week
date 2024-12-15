package com.example.demo.entity;

import com.example.demo.config.JPAConfiguration;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Import(JPAConfiguration.class)
public class ItemEntityTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void itemStatusCannotBeNull() {
        // Given
        Item item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .itemStatus(null) // 명시적으로 null 설정
                .build();

        // When & Then
        assertThrows(Exception.class, () -> itemRepository.save(item),
                "itemStatus가 null이면 저장할 수 없어야 합니다.");
    }

}
