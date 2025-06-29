package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.repository.CustomerCardRepository;
import com.zlagoda.Zlagoda.repository.implementation.CustomerCardRepositoryImpl;
import com.zlagoda.Zlagoda.service.CustomerCardService;
import com.zlagoda.Zlagoda.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCardServiceImpl implements CustomerCardService {

    private final CustomerCardRepository customerCardRepository;

    @Autowired
    private IdGenerator idGenerator;

    public CustomerCardServiceImpl(CustomerCardRepository customerCardRepository) {
        this.customerCardRepository = customerCardRepository;
    }

    @Override
    public List<CustomerCard> findAll() {
        return customerCardRepository.findAll();
    }

    @Override
    public List<CustomerCard> findByName(String name) {
        return customerCardRepository.findByName(name);
    }

    @Override
    public CustomerCard findById(String id) {
        return customerCardRepository.findById(id);
    }

    @Override
    public void create(CustomerCard card) {
        if (card.getCardNumber() == null) {
            card.setCardNumber(idGenerator.generate(IdGenerator.Option.Customer));
        }
        if (card.getPatronymic() != null && card.getPatronymic().isBlank()) {
            card.setPatronymic(null);
        }
        if (card.getCity() != null && card.getCity().isBlank()) {
            card.setCity(null);
        }
        if (card.getStreet() != null && card.getStreet().isBlank()) {
            card.setStreet(null);
        }
        if (card.getZipCode() != null && card.getZipCode().isBlank()) {
            card.setZipCode(null);
        }
        customerCardRepository.create(card);
    }

    @Override
    public void update(CustomerCard card) {
        if (card.getPatronymic() != null && card.getPatronymic().isBlank()) {
            card.setPatronymic(null);
        }
        if (card.getCity() != null && card.getCity().isBlank()) {
            card.setCity(null);
        }
        if (card.getStreet() != null && card.getStreet().isBlank()) {
            card.setStreet(null);
        }
        if (card.getZipCode() != null && card.getZipCode().isBlank()) {
            card.setZipCode(null);
        }
        customerCardRepository.update(card);
    }

    @Override
    public void delete(String id) {
        customerCardRepository.delete(id);
    }

    public List<CustomerCard> filter(Integer percentage, List<String> sortParams) {
        return customerCardRepository.filter(percentage, sortParams);
    }
}
