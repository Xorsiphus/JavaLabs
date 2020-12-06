package ru.xorsiphus.dao.second.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.xorsiphus.dao.second.db.repositories.PersonalRepository;
import ru.xorsiphus.entity.IEntity;
import ru.xorsiphus.entity.Personal;

import java.util.List;
import java.util.Optional;

@Service("personalService")
public class PersonalServiceImpl implements AbstractService
{
    @Autowired
    @Qualifier("personalRepository")
    private PersonalRepository repository;

    public List<Personal> findAll()
    {
        return repository.findAll();
    }

    public Optional<Personal> findById(int id)
    {
        return repository.findById(id);
    }

    public <T extends IEntity> void updateById(int id, T entity)
    {
        repository.updateById(id, (Personal) entity);
    }

    public <T extends IEntity> void insert(T entity)
    {
        repository.insert((Personal) entity);
    }

    public void removeById(int id)
    {
        repository.removeById(id);
    }

}
