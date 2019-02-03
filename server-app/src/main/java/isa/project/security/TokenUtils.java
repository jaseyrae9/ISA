package isa.project.security;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import isa.project.model.users.AirCompanyAdmin;
import isa.project.model.users.HotelAdmin;
import isa.project.model.users.RentACarAdmin;
import isa.project.model.users.security.CustomUserDetails;
import isa.project.repository.users.AirCompanyAdminRepository;
import isa.project.repository.users.HotelAdminRepository;
import isa.project.repository.users.RentACarAdminRepository;



@Component
public class TokenUtils {
	
	@Autowired
	private RentACarAdminRepository rentACarRepository;

	@Autowired
	private HotelAdminRepository hotelAdminRepository;
	
	@Autowired
	private AirCompanyAdminRepository airCompanyAdminRepository;
	
	private final String AUDIENCE_UNKNOWN = "unknown";
	private final String AUDIENCE_WEB = "web";
	private final String AUDIENCE_MOBILE = "mobile";
	private final String AUDIENCE_TABLET = "tablet";
	@Value("Authorization")
	private String AUTH_HEADER;
	protected final Log LOGGER = LogFactory.getLog(getClass());

	private String secret = "tajna!";

	private Long expiration = new Long(604800);

	/**
	 * @param token
	 * @return email hidden in token
	 */
	public String getEmailFromToken(String token) {
		String email;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			email = claims.getSubject();
		} catch (Exception e) {
			email = null;
		}
		return email;
	}

	/**
	 * @param token
	 * @return date when token was created
	 */
	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			created = new Date((Long) claims.get("created"));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	/**
	 * @param token
	 * @return date when token will expire
	 */
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	/**
	 * @param token
	 * @return for which device is token
	 */
	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			audience = (String) claims.get("audience");
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	/**
	 * @param token
	 * @return what is stored in token
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(this.secret.getBytes("UTF-8")).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + this.expiration * 1000);
	}

	/**
	 * Checks if token is expired. Compares current date to expiration date from token.
	 * @param token
	 * @return 
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(this.generateCurrentDate());
	}

	/**
	 * Checks if token was created before last password change. Token contains password, and is no
	 * longer valid if password was changed.
	 * @param created
	 * @param lastPasswordReset
	 * @return
	 */
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	/**
	 * Returns device for which token is being used.
	 * @param device
	 * @return
	 */
	private String generateAudience(Device device) {
		String audience = this.AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = this.AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	private Boolean ignoreTokenExpiration(String token) {
		String audience = this.getAudienceFromToken(token);
		return (this.AUDIENCE_TABLET.equals(audience) || this.AUDIENCE_MOBILE.equals(audience));
	}

	/**
	 * Creates new token for user.
	 * @param userDetails basic user details containing username and password
	 * @param device for which token is being created
	 * @return
	 */
	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("roles", userDetails.getAuthorities());
		
		Optional<RentACarAdmin> rentACarAdmin = rentACarRepository.findByEmail(userDetails.getUsername());
		if(rentACarAdmin.isPresent()) {
			claims.put("companyId", rentACarAdmin.get().getRentACarCompany().getId());
		}
		
		Optional<HotelAdmin> hotelAdmin = hotelAdminRepository.findByEmail(userDetails.getUsername());
		if(hotelAdmin.isPresent()) {
			claims.put("companyId", hotelAdmin.get().getHotel().getId());
		}
		
		Optional<AirCompanyAdmin> airCompanyAdmin = airCompanyAdminRepository.findByEmail(userDetails.getUsername());
		if(airCompanyAdmin.isPresent()) {
			claims.put("companyId", airCompanyAdmin.get().getAirCompany().getId());
		}
		
		claims.put("sub", userDetails.getUsername());
		claims.put("audience", this.generateAudience(device));
		claims.put("created", this.generateCurrentDate());
		return this.generateToken(claims);
	}

	/**
	 * Adds claims to token being created.
	 * @param claims
	 * @return
	 */
	private String generateToken(Map<String, Object> claims) {
		try {
			return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
					.signWith(SignatureAlgorithm.HS512, this.secret.getBytes("UTF-8")).compact();
		} catch (UnsupportedEncodingException ex) {
			// didn't want to have this method throw the exception, would rather log it and
			// sign the token like it was before
			LOGGER.warn(ex.getMessage());
			return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
					.signWith(SignatureAlgorithm.HS512, this.secret).compact();
		}
	}

	/**
	 * Checks if token can be refreshed. Token can only be refreshed if password wasn't changed.
	 * @param token
	 * @param lastPasswordReset
	 * @return
	 */
	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = this.getCreatedDateFromToken(token);
		return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
				&& (!(this.isTokenExpired(token)) || this.ignoreTokenExpiration(token)));
	}

	/**
	 * Changes token created date and expiration date. Token can only be refreshed if password wasn't changed.
	 * @param token
	 * @return refreshed token or null if token can not be refreshed.
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getClaimsFromToken(token);
			claims.put("created", this.generateCurrentDate());
			refreshedToken = this.generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	/**
	 * Checks if token is valid. 
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		CustomUserDetails user = (CustomUserDetails) userDetails;
		final String email = this.getEmailFromToken(token);
		final Date created = this.getCreatedDateFromToken(token);
		return (email.equals(user.getUsername()) && !(this.isTokenExpired(token))
				&& !(this.isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())));
	}

	/**
	 * Finds token in HTTP request.
	 * @param request
	 * @return
	 */
	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
}