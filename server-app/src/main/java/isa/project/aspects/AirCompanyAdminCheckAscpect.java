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
import isa.project.model.users.AirCompanyAdmin;
import isa.project.repository.users.AirCompanyAdminRepository;
import isa.project.security.TokenUtils;

/**
 * Proverava da li administartor aviokompanije sme da menjati datu
 * aviokompaniju. 
 * VAŽNO: Podrazumeva da je prvi argument metode id komapnije nad kojom se vrši izmena.
 * 
 * @author Milica
 *
 */
@Aspect
@Component
public class AirCompanyAdminCheckAscpect {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private AirCompanyAdminRepository airComanpanyAdminRespository;

	@Before("@annotation(isa.project.aspects.AirCompanyAdminCheck)")
	public void logBefore(JoinPoint joinPoint) throws AdminAccountException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		AirCompanyAdmin user = airComanpanyAdminRespository.findByEmail(email).get();
		
		//preuzmi prvi argument metode - podrazumevano id kompanije
		Object companyId = joinPoint.getArgs()[0];
		
		if(!user.getAirCompany().getId().equals(companyId)) {
			throw new AdminAccountException("You are not admin of this air company. Please, edit your company " + user.getAirCompany().getName() + ".");
		}
	}
}
