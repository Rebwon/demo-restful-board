package com.rebwon.demosecurityboard.modules.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.domain.UserAccount;
import com.rebwon.demosecurityboard.modules.account.api.payload.AccountUpdatePayload;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(emailOrNickname);
		if(account == null) {
			account = accountRepository.findByNickname(emailOrNickname);
		}

		if(account == null) {
			throw new UsernameNotFoundException(emailOrNickname);
		}
		return new UserAccount(account);
	}

	@Override
	public Account register(SignUpPayload payload) {
		Assert.notNull(payload, "Input Payload is Null!");
		Account account = Account.of(payload.getEmail(),
			this.passwordEncoder.encode(payload.getPassword()), payload.getNickname());
		return this.accountRepository.save(account);
	}

	@Transactional(readOnly = true)
	@Override
	public Account getAccount(Long id, Account account) {
		account.isNowOwner(id);
		return this.accountRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public void update(Long id, Account account, AccountUpdatePayload payload) {
		account.isNowOwner(id);
		account.update(payload.getNickname(), this.passwordEncoder.encode(payload.getNewPassword()));
	}
}
