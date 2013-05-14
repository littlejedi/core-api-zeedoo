package com.zeedoo.core.api.hmac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.zeedoo.core.api.dao.UserDao;
import com.zeedoo.core.api.domain.User;
import com.zeedoo.core.api.hmac.utils.HmacUtils;

/**
 * <p>Authenticator to provide the following to application:</p>
 * <ul>
 * <li>Verifies the provided credentials are valid</li>
 * </ul>
 *
 */
@Component
public class HmacServerAuthenticator implements Authenticator<HmacServerCredentials, User> {
	
  private final Logger LOGGER = LoggerFactory.getLogger(HmacServerAuthenticator.class);

  @Autowired
  private UserDao userDao;

  @Override
  public Optional<User> authenticate(HmacServerCredentials credentials) throws AuthenticationException {
     	    
    // Get the User referred to by the API key
    Optional<User> user = Optional.fromNullable(userDao.getUserByApiKey(credentials.getApiKey()));
    if (!user.isPresent()) {
      return Optional.absent();
    }
    
    // Locate their secret key
    Optional<String> secretKey = Optional.fromNullable(user.get().getSecretKey());
    if (!secretKey.isPresent()) {
    	LOGGER.warn("User {0} has an API but a null secret key", user.get().getUsername());
    	return Optional.absent();
    }

    String computedSignature = new String(
      HmacUtils.computeSignature(
        credentials.getAlgorithm(),
        credentials.getCanonicalRepresentation(),
        secretKey.get()));
       
    // Avoid timing attacks by verifying every byte every time
    if (isEqual(computedSignature.getBytes(), credentials.getDigest().getBytes())) {
      return user;
    }

    return Optional.absent();

  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * Performs a byte array comparison with a constant time
   *
   * @param a A byte array
   * @param b Another byte array
   * @return True if the byte array have equal contents
   */
  public static boolean isEqual(byte[] a, byte[] b) {
    if (a.length != b.length) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }

}
