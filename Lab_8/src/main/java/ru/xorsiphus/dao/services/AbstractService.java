package ru.xorsiphus.dao.services;

import ru.xorsiphus.entity.IEntity;

import java.util.List;
import java.util.Optional;

public interface AbstractService
{
    List<? extends IEntity> findAll();

    List<? extends IEntity> findByAuthor(String author);

    Optional<? extends IEntity> findById(int id);

    <T extends IEntity> void updateById(int id, T entity);

    <T extends IEntity> void insert(T entity);

    long count();

    void removeById(int id);
}
