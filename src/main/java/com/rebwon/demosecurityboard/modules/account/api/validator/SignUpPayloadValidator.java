package com.rebwon.demosecurityboard.modules.account.api.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.rebwon.demosecurityboard.modules.account.api.exception.EmailDuplicateException;
import com.rebwon.demosecurityboard.modules.account.api.exception.NicknameDuplicateException;
import com.rebwon.demosecurityboard.modules.account.domain.AccountRepository;
import com.rebwon.demosecurityboard.modules.account.api.payload.SignUpPayload;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpPayloadValidator {
	private final AccountRepository accountRepository;

	public void validate(SignUpPayload payload) {
		if (accountRepository.existsByEmail(payload.getEmail())) {
			throw new EmailDuplicateException(payload.getEmail());
			/*errors.rejectValue("email", "invalid.email",
				new Object[]{payload.getEmail()}, "This Email has using in application");*/
		}
		if (accountRepository.existsByNickname(payload.getNickname())) {
			throw new NicknameDuplicateException(payload.getNickname());
			/*errors.rejectValue("nickname", "invalid.nickname",
				new Object[]{payload.getNickname()}, "This nickname has using in application");*/
		}
	}
}
