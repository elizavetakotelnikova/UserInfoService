package com.example.catmicroservice.services;
import com.example.catmicroservice.exceptions.IncorrectArgumentsException;
import com.example.catmicroservice.models.CatInfoDto;
import com.example.catmicroservice.repositories.CatsDao;
import com.example.catmicroservice.valueObjects.FindCriteria;
import com.example.jpa.Cat;
import com.example.jpa.CatDto;
import com.example.jpa.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @RabbitListener(queues = RabbitMQConfig.CREATING_CAT_QUEUE, errorHandler = "rabErrorHandler")
    public CatDto saveCat(CatDto dto) throws IncorrectArgumentsException {
        if (dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwner() != null) {
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            var returnedCat = catsDao.save(new Cat(dto.getName(), dto.getBreed(), dto.getColor(), dto.getOwner(), dto.getBirthday(), friends));
            return new CatDto(returnedCat.getId(), returnedCat.getName(),
                    returnedCat.getBreed(), returnedCat.getColor(), returnedCat.getOwner(),
                    returnedCat.getBirthday(), returnedCat.getFriends().stream().map(Cat::getId).toList());
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to save a cat");
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.FIND_CAT_BY_CRITERIA_QUEUE, errorHandler = "rabErrorHandler")
    public List<CatDto> getCatByCriteria(FindCriteria criteria) {
        List<Cat> cats;
        if (criteria.getColor() != null) cats = catsDao.findByColor(criteria.getColor());
        else if (criteria.getBirthday() != null) cats = catsDao.findByBirthday(criteria.getBirthday());
        else if (criteria.getName() != null) cats = catsDao.findByName(criteria.getName());
        else if (criteria.getBreed() != null) cats = catsDao.findByBreed(criteria.getBreed());
        else if (criteria.getOwnerId() != null) cats = catsDao.findByOwnerId(criteria.getOwnerId());
        else cats = catsDao.findAll();
        return cats.stream().map(x -> new CatDto(x.getId(), x.getName(),
                x.getBreed(), x.getColor(), x.getOwner(), x.getBirthday(),
                x.getFriends().stream().map(Cat::getId).toList())).toList();
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.FIND_CAT_QUEUE, errorHandler = "rabErrorHandler")
    public CatDto getCatById(long id) {
        var returnedCat = catsDao.findById(id);
        return new CatDto(returnedCat.getId(), returnedCat.getName(),
                returnedCat.getBreed(), returnedCat.getColor(),
                returnedCat.getOwner(), returnedCat.getBirthday(),
                returnedCat.getFriends().stream().map(Cat::getId).toList());
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETING_CAT_QUEUE, errorHandler = "rabErrorHandler")
    public void delete(long id) {
        catsDao.deleteById(id);
    }
    @Override
    @RabbitListener(queues = RabbitMQConfig.UPDATING_CAT_QUEUE, errorHandler = "rabErrorHandler")
    public CatDto update(CatDto dto) throws IncorrectArgumentsException {
        if (dto.getId() != null && dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwner() != null) {
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            var returnedCat = catsDao.save(new Cat(dto.getId(), dto.getName(), dto.getBreed(), dto.getColor(), dto.getOwner(), dto.getBirthday(), friends));
            return new CatDto(returnedCat.getId(), returnedCat.getName(),
                    returnedCat.getBreed(), returnedCat.getColor(),
                    returnedCat.getOwner(), returnedCat.getBirthday(),
                    returnedCat.getFriends().stream().map(Cat::getId).toList());
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to update a cat");
    }
}
