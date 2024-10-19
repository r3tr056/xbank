package com.xfinance.xbank.controllers.public_banking.auth;

import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.AutoWired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(path="/api/session")
public class SessionController {

	@AutoWired
	private SessionService sessionService;

	@PreAuthorize("hasAnyAuthority('P003')")
	@GetMapping(path="/countuserlogged")
	public Integer countUserLogger() {
		return sessionService.countSessions();
	}

	@PreAuthorize("hasAnyAuthority('P003')")
	@PostMapping(path="/alluserslogged")
	public DeferredResult<UserLoggedDetail> allUsersLogged() {
		DeferredResult<UserLoggedDetail> deferredResult = new DeferredResult<>(60000L);
		ForkJoinPool.commonPool().submit(() -> {
			try {
				UserLoggedDetail userLoggedDetail = sessionService.allUsersLoggedDetail();
				deferredResult.setResult(userLoggedDetail);
				
			} catch (Exception ex) {
				deferredResult.setErrorResult(ex);
			}
		})
	}
}