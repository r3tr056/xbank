package com.xfinance.xbank.services.creditservice;

import com.xfinance.xbank.models.credit.CreditCard;
import com.xfinance.xbank.repo.CreditCardRepository;
import com.xfinance.xbank.services.creditservice.CreditCardService;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * CreditCard Service Implementation
 */

@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    @AutoWired
    private CreditCardRepository creditCardRepo;
    private final Logger logger = LoggerFactory.getLogger(CreditCardServiceImpl.class);
    private final CreditCardRepository creditCardRepo;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepo) {
        this.creditCardRepo = creditCardRepo;
    }

    @Override
    public Page<CreditCard> listAllCreditByGroupCard(String credit_card_group_id) {
        return creditCardRepo.findAll(credit_card_group_id);
    }

    @Override
    public Page<CreditCard> listAll(Pageable pageable) {
        logger.debug("Request to get all CreditCards");
        return creditCardRepo.findAll(pageable);
    }

    @Override
    public Optional<CreditCard> findCreditCard(Long creditCard_id) {
        logger.debug("Request to get CreditCard : {}", creditCard_id);
        return creditCardRepo.findById(id);
    }

    @Override
    public Optional<CreditCard> findOneWithCustomerID(Long customer_id) {
        logger.debug("Request to find CreditCard with Customer ID : {}", customer_id);
        return creditCardRepo.findByCustomerId(customer_id);
    }

    @Override
    public CreditCard getCreditCardBySecurityId(String security_id) {
        return creditCardRepo.findBySecID(security_id).get();
    }

    @Override
    public CreditCard saveCreditCard(CreditCard credit_card) {
        logger.debug("Request to save CreditCard : {}", credit_card);
        return creditCardRepo.saveCard(credit_card);
    }

    @Override
    public void deleteCreditCard(String creditCard_id, String security_id) {
        logger.debug("Request to delete CreditCard");
        creditCardRepo.deleteBySecID(creditCard_id, security_id);
    }
}