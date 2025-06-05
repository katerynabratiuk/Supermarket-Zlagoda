package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.repository.CustomerCardRepository;
import com.zlagoda.Zlagoda.service.CustomerCardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCardServiceImpl implements CustomerCardService {

    private final CustomerCardRepository customerCardRepository;

    public CustomerCardServiceImpl(CustomerCardRepository customerCardRepository) {
        this.customerCardRepository = customerCardRepository;
    }

    @Override
    public List<CustomerCard> findByPercentage(int percentage) {
        return customerCardRepository.findByPercentage(percentage);
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
        customerCardRepository.create(card);
    }

    @Override
    public void update(CustomerCard card) {
        customerCardRepository.update(card);
    }

    @Override
    public void delete(String id) {
        customerCardRepository.delete(id);
    }
}
