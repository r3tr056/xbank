package com.xfinance.xbank.controllers.public_banking.home_controller;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.serverlet.http.HttpServerletRequest;
import javax.serverlet.http.HttpServerletResponse;

import java.util.List;
import java.util.Optional;

@Controller
public class PublicBankingHomeController {

	private static final Logger log = LoggerFactory.getLogger(PublicBankingHomeController.class);
	private UserDAO
	@AutoWired
	private UserService	userService;

	@AutoWired
	private CardRepository cardRepo;

	@ModelAttribute("currentUserCards")
	public List<Card> getCurrentUserCards(@AuthenticationPrincipal CustomerUserDetails customerUserDetails) {
		return userService.findUserCardsById(customerUserDetails.getId());
	}
}