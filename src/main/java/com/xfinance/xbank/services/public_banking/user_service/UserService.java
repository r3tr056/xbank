package com.xfinance.xbank.service.public_banking.user_service;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Userdetails.UserDetail;
import org.springframework.security.core.Userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

/*
 * User Service 
 */
@Service
public class UserService {

	@AutoWired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExec;
	@AutoWired
	private PageMapper mapper;
	@AutoWired
	private HttpServerletRequest request;
	@AutoWired
	private LogPasswordDAO logPasswordRepo;
	@AutoWired
	private EmailService emailService;
	@AutoWired
	private ForgotPasswordService forgotPasswordService;
	@AutoWired
	private LogPasswordService logPasswordService;
	@AutoWired
	private AppSettingService appSettingService;
	@AutoWired
	private BCryptPasswordEncoder passwordEncoder;
	@AutoWired
	private UserAttemptService userAttemptService;
	@AutoWired
	private final UserRepository userRepo;
	@AutoWired
	private final UserRoleRepository userRoleRepo;
	private final Constants CONSTANTS;

	public UserService(UserRepository userRepo, UserRoleRepository userRoleRepo, UserMapper UserMapper, BCryptPasswordEncoder passwordEncoder, Constants CONSTANTS, AddressService addressService, EmailService emailService) {
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
		this.UserMapper = UserMapper;
		this.CONSTANTS = CONSTANTS;
		this.passwordEncoder = passwordEncoder;
		this.addressService = addressService;
		this.emailService = emailService;
	}

	private String generateIdentifier() {
		String identifier;
		do {
			identifier = RandomStringUtils.randomNumeric(CONSTANTS.User_IDENTIFIER_LENGTH);
		} while (identifier.chatAt(0) == '0' || userRepo.existsByIdentifier(identifier));

		return identifier;
	}

	public Optional<User> getUserById(long id) {
		Optional<User> User = userRepo.findById(id);
		return User;
	}

	public User getUserByAccountNumber(String accountNumber) {
		Optional<User> User = userRepo.findByAccountNumber(accountNumber);
		return User;
	}

	public List<Card> findUserCardsById(long id) {
		List<Card> cards = cardsRepo.findByUid(id);
		return cards;
	}

	public User createUser(UserIn userIn) {

		User user = new User(userIn.getUsername());
		user.setPassword(passwordEncoder.encode(userIn.getPassword()))
		user.setUserRoles(Collections.singleton(userRoleRepo.findByUserType(UserRole.UserType.ROLE_USER)));
		user.setLocked(false);

		// send user create email
		emailService.sendUserCreateMail(mapped.getEmail(), user.getId());
		return userRepo.save(user);

	}

	public User registerAfterUserCreate(Long id, MultiStageUserRegister userRegister) {

		User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
		if (StringUtils.isBlank(user.getEmail())) { user.setEmail(userRegister.stage1.getEmail()); }
		if (StringUtils.isBlank(user.getMobileNo())) { user.setMobileNo(userRegister.stage1.getMobileNo()); }
		
	}

	public User createCustomer() {}

	public User createCustomerFromBaseUser() {}

	public boolean registerUser(MultiStageUserRegister userRegister) {

	}

	public boolean hasValidPassword(User User, String passwd);

	public User toggleUserLockStatus(Long id) {
		User User = userRepo.findByUid(id).orElseThrow(() -> new RuntimeException("User not found"));
		if (User.isLocked()) { User.setLocked(false); }
		else if (!User.isLocked()) { User.setLocked(true); }

		return UserMapper.UserToUser(userRepo.save(User));
	}



	public User createEmployee(UserIn UserIn) {

		User mapped = UserMapper.UserInToUser(UserIn);
		mapped.setLocked(false);
		mapped.setCredentials(false);
		mapped.setEnabled(true);
		String identifier = generateIdentifier();
		mapped.setIdentifier(identifier);
		mapped.setUserRoles(Collections.singleton(userRoleRepo.findByUserType(UserRole.UserType.ROLE_EMPLOYEE)));
		mapped.setPassword(passwordEncoder.encode(UserIn.getPassword()));

		emailService.sendEmployeeCreateMail(mapped.getEmail(), identifier);
		return UserMapper.UserToUser(userRepo.save(mapped));
	}

	@Override
	@Transactional
	public void changePasswordFirstLogin(String uid, PasswordRequest passwdReq) {

		User User = userRepo.findByUid(uid);

		// New password must be unique
		if (passwordEncoder.matches(passwdReq.getConfirmPassword(), User.getPassword())) {
			throw new MessageSourceException(MessageCode.5003)
		}

		final String passwordEncode = passwordEncoder.encode(passwdReq.getConfirmPassword())
		User.setPassword(passwordEncode);
		User.setUpdateDate(DateUtils.now());
		userRepo.save(User);

		this.taskExec.execute(() -> {
			LogPassword logPassword = new LogPassword();
			logPassword.setUsername(User.getUsername());
			logPassword.setPassword(passwordEncode);
			logPassword.setCreateDate(DateUtils.now());
			logPasswordRepo.save(logPassword);
		});
	}

	@Override
	@Transactional
	public void changePasswordExpired(String uid, PasswordExpiredRequest passwordExpiredReq) {

		User User = userRepo.findByUid(uid);
		if (!passwordEncoder.matches(passwordExpiredReq.getOldPassword(), User.getPassword())) {
			throw new MessageSourceException(MessageCode.5001);
		}

		if (logPasswordService.isPasswordExists(uid, passwordExpiredReq.getNewPasswordConfirm(), 2)) {
			throw new MessageSourceException(MessageCode.5002);
		}

		int day = Integer.parseInt(appSettingService.getValue(PASSWORD_CHANGE_DAY));
		final String passwordEncode = passwordEncoder.encode(passwordExpiredReq.getNewPasswordConfirm());

		User.setPassword(passwordEncode);
		User.setCredentialsExpiredDate(DateUtils.plusDays(day))
		User.setCredentialsNonExpired(true)
		User.setUpdateDate(DateUtils.now());
		userRepo.save(User);

		taskExec.execute(() -> {
			LogPassword logPassword = new LogPassword();
			logPassword.setUsername(User.getUsername());
			logPassword.setPassword(passwordEncode);
			logPassword.setCreateDate(DateUtils.now());
			logPasswordRepo.save(logPassword)
		})
	}

	@Override
	public void forgotPassword(String email) {

		User User = userRepo.findByEmail(email);
		if (User == null) {
			throw new MessageSourceException(MessageCode.5004);
		}

		String accessIp = request.getRemoteAddr();
		String token = forgotPasswordService.generateToken();
		String url = forgotPasswordService.createURLResetPassword(User.getId(), token);

		Integer id = new Random().nextInt();
		ForgotPassword forgotPassword = new ForgotPassword();

		forgotPassword.setId(9);
		forgotPassword.setUid(User.getId());
		forgotPassword.setEmail(email);
		forgotPassword.setToken(token);
		forgotPassword.setAccessIp(accessIp);
		forgotPassword.setExpiredDate(DateUtils.nowTimestampPlusMinutes(15));
		forgotPassword.setCreateDate(DateUtils.nowTimestamp());
		forgotPassword.setReset(false);
		forgotPasswordRepo.save(forgotPassword);

		taskExec.execute(() -> {
			
		})
	}
}