package com.xfinance.xbank.services.creditservice;

import java.util.Optional;

import com.xfinance.xbank.models.credit.CreditCard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreditCardService {
    Page<CreditCard> listAllCreditByGroupCard(String credit_card_group_id);

    Page<CreditCard> listAll(Pageable pageable);

    Optional<CreditCard> findCreditCard(Long creditCard_id);

    CreditCard getCreditCardBySecurityId(String security_id);

    boolean saveCreditCard(CreditCard creditCard);

    void deleteCreditCard(String security_id);

    Optional<CreditCard> findOneWithCustomerID(Long customer_id);
}