package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(int id);

    @Query(" select i from Item i " +
        "where i.available = true" +
        " and (upper(i.name) like upper(concat('%', ?1, '%'))" +
        " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItem(String text);

    List<Item> findAllByRequestId(int requestId);
}
