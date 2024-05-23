package com.example.catmicroservice.services;
import com.example.catmicroservice.exceptions.IncorrectArgumentsException;
import com.example.catmicroservice.models.CatInfoDto;
import com.example.catmicroservice.repositories.CatsDao;
import com.example.catmicroservice.valueObjects.FindCriteria;
import com.example.jpa.Cat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    private final CatsDao catsDao;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public CatServiceImpl(CatsDao catsDao, RabbitTemplate rabbitTemplate) {
        this.catsDao = catsDao;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    public Cat saveCat(CatInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwner() != null) {
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            return catsDao.save(new Cat(dto.getName(), dto.getBreed(), dto.getColor(), dto.getOwner(), dto.getBirthday(), friends));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to save a cat");
    }
    @Override
    public List<Cat> getCatByCriteria(FindCriteria criteria) {
        if (criteria.getColor() != null) return catsDao.findByColor(criteria.getColor());
        if (criteria.getBirthday() != null) return catsDao.findByBirthday(criteria.getBirthday());
        if (criteria.getName() != null) return catsDao.findByName(criteria.getName());
        if (criteria.getBreed() != null) return catsDao.findByBreed(criteria.getBreed());
        if (criteria.getOwnerId() != null) return catsDao.findByOwnerId(criteria.getOwnerId());
        return catsDao.findAll();
    }

    @Override
    public Cat getCatById(long id) {
        return catsDao.findById(id);
    }
    @Override
    public void delete(long id) {
        catsDao.deleteById(id);
    }
    @Override
    public Cat update(CatInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getId() != null && dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwner() != null) {
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            return catsDao.save(new Cat(dto.getId(), dto.getName(), dto.getBreed(), dto.getColor(), dto.getOwner(), dto.getBirthday(), friends));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to update a cat");
    }
}
