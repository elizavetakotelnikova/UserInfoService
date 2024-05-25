package com.example.ownermicroservice.services;

import com.example.jpa.Cat;
import com.example.jpa.Owner;
import com.example.jpa.OwnerDto;
import com.example.jpa.RabbitMQConfig;
import com.example.ownermicroservice.exceptions.IncorrectArgumentsException;
import com.example.ownermicroservice.models.OwnerInfoDto;
import com.example.ownermicroservice.repositories.FindCriteria;
import com.example.ownermicroservice.repositories.OwnersDao;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan("com.example.jpa.*")
@EnableRabbit
public class OwnerServiceImpl implements OwnerService {
    private final OwnersDao ownersDao;
    @Autowired
    public OwnerServiceImpl(OwnersDao ownersDao) {
        this.ownersDao = ownersDao;
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATING_OWNER_QUEUE, errorHandler = "rabErrorHandler")
    public OwnerDto saveOwner(OwnerDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getUser() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an owner");
        if (ownersDao.findByUser(dto.getUser()) != null) throw new IncorrectArgumentsException("Incorrect data provided, owner with same userId already exists");
        List<Cat> cats = new ArrayList<>();
        var owner = ownersDao.save(new Owner(dto.getBirthday(), cats, dto.getUser()));
        return new OwnerDto(owner.getId(), owner.getBirthday(), owner.getCats(),owner.getUser());
    }
     @Override
     @RabbitListener(queues = RabbitMQConfig.FIND_OWNER_BY_CRITERIA_OWNER_QUEUE, errorHandler = "rabErrorHandler")
    public List<OwnerDto> getOwnerByCriteria(FindCriteria criteria) {
        List<Owner> owners = new ArrayList<>();
        if (criteria.getBirthday() != null) {
            owners = ownersDao.findByBirthday(criteria.getBirthday());
        }
        else if (criteria.getUser() != null) {
            var owner = ownersDao.findByUser(criteria.getUser());
            owners.add(owner);
        }
        else {
            owners = ownersDao.findAll();
        }
         return owners.stream().map(x -> new OwnerDto(x.getId(), x.getBirthday(),
                x.getCats(), x.getUser())).toList();
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.FIND_OWNER_QUEUE)
    public OwnerDto getOwnerById(long id) {
        var owner = ownersDao.findById(id);
        if (owner == null) return null;
        return new OwnerDto(owner.getId(), owner.getBirthday(), owner.getCats(), owner.getUser());
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.UPDATING_OWNER_QUEUE)
    public OwnerDto update(OwnerDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an owner");
        var owner = ownersDao.save(new Owner(dto.getId(), dto.getBirthday(), dto.getCats(), dto.getUser()));
        return new OwnerDto(owner.getId(), owner.getBirthday(), owner.getCats(), owner.getUser());
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETING_OWNER_QUEUE)
    public void delete(long id) {
        ownersDao.deleteById(id);
    }
}
