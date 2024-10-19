package com.xfinance.xbank.services.creditservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CreditCardQueryService extends QueryService<CreditCard> {

	private final Logger logger = LoggerFactory.getLogger(CreditCardQueryService.class);
	private final CreditCardRepository creditCardRepo;

	public CreditCardQueryService(CreditCardRepository creditCardRepo) {
		this.creditCardRepo = creditCardRepo;
	}

	@Transactional(readOnly=true)
	public List<CreditCard> findByCriteria(CreditCardCriteria criteria) {
		logger.debug("Find By Criteria : {}", criteria);
		final Specification<CreditCard> specification = createSpecification(criteria);
		return creditCardRepo.findAll(specification);
	}

	@Transactional(readOnly=true)
	public Page<CreditCard> findByCriteria(CreditCardCriteria criteria, Pageable page) {
		logger.debug("Find by Criteria : {}, Page : {}", criteria, page);
		final Specification<CreditCard> specification = createSpecification(criteria);
		return creditCardRepo.findAll(specification, page);
	}

	@Transactional(readOnly=true)
	public long countByCriteria(CreditCardCriteria criteria) {
		logger.debug("Count by Criteria : {}", criteria);
		final Specification<CreditCard> specification = createSpecification(criteria);
		return creditCardRepo.count(specification);
	} 

	protected Specification<CreditCard> createSpecification(CreditCardCriteria criteria) {
		Specification<CreditCard> specification = Specification.where(null);

		if (criteria != null) {
			if (criteria.getId() != null) {
				specification = specification.and(buildSpecification(criteria.getId(), CreditCard_.id));
			}
			if (criteria.getAccountNumber() != null) {
				specification = specification.and(buildStringSpecification(criteria.getAccountNumber(), CreditCard_.accountNumber));
			}
			if (criteria.getBalance() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getBalance(), CreditCard_.balance));
			}
			if (criteria.getRewardPoints() != null) {
				specification = specification.and(buildRangeSpecification(criteria.getRewardPoints(), CreditCard_.rewardPoints));
			}
			if (criteria.getUserID() != null) {
				specification = specification.and(buildStringSpecification(criteria.getUserID(), CreditCard_.userID));
			}
		}

		return specification;
	}
}