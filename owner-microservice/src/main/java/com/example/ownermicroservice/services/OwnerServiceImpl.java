package com.example.ownermicroservice.services;

import com.example.jpa.Cat;
import com.example.jpa.Owner;
import com.example.ownermicroservice.exceptions.IncorrectArgumentsException;
import com.example.ownermicroservice.models.OwnerInfoDto;
import com.example.ownermicroservice.rabbitMQ.RabbitMQConfig;
import com.example.ownermicroservice.repositories.FindCriteria;
import com.example.ownermicroservice.repositories.OwnersDao;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableRabbit
public class OwnerServiceImpl implements OwnerService {
    private final OwnersDao ownersDao;
    @Autowired
    public OwnerServiceImpl(OwnersDao ownersDao) {
        this.ownersDao = ownersDao;
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATING_OWNER_QUEUE)
    public Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getUser() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an owner");
        if (ownersDao.findByUser(dto.getUser()) != null) throw new IncorrectArgumentsException("Incorrect data provided, owner with same userId already exists");
        List<Cat> cats = new ArrayList<>();
        return ownersDao.save(new Owner(dto.getBirthday(), cats, dto.getUser()));
    }
     @Override
     @RabbitListener(queues = RabbitMQConfig.FIND_OWNER_BY_CRITERIA_OWNER_QUEUE)
    public List<Owner> getOwnerByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return ownersDao.findByBirthday(criteria.getBirthday());
        if (criteria.getUser() != null) {
            List<Owner> ownersList = new ArrayList<>();
            var owner = ownersDao.findByUser(criteria.getUser());
            ownersList.add(owner);
            return ownersList;
        }
        return ownersDao.findAll();
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.FIND_OWNER_QUEUE)
    public Owner getOwnerById(long id) {
        return ownersDao.findById(id);
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.UPDATING_OWNER_QUEUE)
    public Owner update(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an owner");
        return ownersDao.save(new Owner(dto.getId(), dto.getBirthday(), dto.getCats(), dto.getUser()));
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETING_OWNER_QUEUE)
    public void delete(long id) {
        ownersDao.deleteById(id);
    }
}
