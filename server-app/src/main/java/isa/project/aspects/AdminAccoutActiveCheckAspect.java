package isa.project.aspects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import isa.project.exception_handlers.AdminAccountException;
import isa.project.model.users.User;
import isa.project.repository.users.UserRepository;
import isa.project.security.TokenUtils;

/**
 * Proverava da li je administrator izvršio obaveznu promenu šifre, pre upotrebe
 * naloga.
 * 
 * @author Milica
 *
 */
@Aspect
@Component
public class AdminAccoutActiveCheckAspect {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private UserRepository userRepository;
	
	@Before("@annotation(isa.project.aspects.AdminAccountActiveCheck)")
	public void checkIfPasswordIsChanged(JoinPoint joinPoint) throws AdminAccountException{		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		User user = userRepository.findByEmail(email).get();
		if(user.getNeedsPasswordChange()) {
			throw new AdminAccountException("Password change neeeded before using system.");
		}
	}
}
