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
import isa.project.model.users.RentACarAdmin;
import isa.project.repository.users.RentACarAdminRepository;
import isa.project.security.TokenUtils;

/**
 * Proverava da li administartor rentacar kompanije sme da menjati datu
 * rentacar kompaniju. 
 * VAŽNO: Podrazumeva da je prvi argument metode id komapnije nad kojom se vrši izmena.
 * 
 * 
 *
 */
@Aspect
@Component
public class RentACarCompanyAdminCheckAspect {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private RentACarAdminRepository rentACarAdminRepository;

	@Before("@annotation(isa.project.aspects.RentACarCompanyAdminCheck)")
	public void logBefore(JoinPoint joinPoint) throws AdminAccountException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		RentACarAdmin user = rentACarAdminRepository.findByEmail(email).get();
		
		//preuzmi prvi argument metode - podrazumevano id kompanije
		Object companyId = joinPoint.getArgs()[0];
		
		if(!user.getRentACarCompany().getId().equals(companyId)) {
			throw new AdminAccountException("You are not admin of this rent a car company. Please, edit your company " + user.getRentACarCompany().getName() + ".");
		}
	}
}
