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
import isa.project.model.users.HotelAdmin;
import isa.project.repository.users.HotelAdminRepository;
import isa.project.security.TokenUtils;

/**
 * Proverava da li administartor hotela sme da menjati dati
 * hotel. 
 * VAŽNO: Podrazumeva da je prvi argument metode id komapnije nad kojom se vrši izmena.
 * 
 * 
 *
 */
@Aspect
@Component
public class HotelAdminCheckAspect {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private HotelAdminRepository hotelAdminRespository;

	@Before("@annotation(isa.project.aspects.HotelAdminCheck)")
	public void logBefore(JoinPoint joinPoint) throws AdminAccountException{
		System.out.println("AA");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		System.out.println("BB");
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		HotelAdmin user = hotelAdminRespository.findByEmail(email).get();
		
		//preuzmi prvi argument metode - podrazumevano id kompanije
		Object companyId = joinPoint.getArgs()[0];
		
		if(!user.getHotel().getId().equals(companyId)) {
			throw new AdminAccountException("You are not admin of this hotel. Please, edit your hotel " + user.getHotel().getName() + ".");
		}
	}
}
