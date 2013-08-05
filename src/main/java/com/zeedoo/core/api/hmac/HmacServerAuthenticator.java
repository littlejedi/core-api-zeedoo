package com.zeedoo.core.api.hmac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.zeedoo.core.api.database.dao.ApiTokenDao;
import com.zeedoo.core.api.hmac.utils.HmacUtils;
import com.zeedoo.commons.domain.ApiToken;

/**
 * <p>Authenticator to provide the following to application:</p>
 * <ul>
 * <li>Verifies the provided credentials are valid</li>
 * </ul>
 *
 */
@Component
public class HmacServerAuthenticator implements Authenticator<HmacServerCredentials, ApiToken> {
	
  private final Logger LOGGER = LoggerFactory.getLogger(HmacServerAuthenticator.class);

  @Autowired
  private ApiTokenDao apiTokenDao;

  @Override
  public Optional<ApiToken> authenticate(HmacServerCredentials credentials) throws AuthenticationException {
     	    
    // Get the API token referred to by the API key
    Optional<ApiToken> apiToken = Optional.fromNullable(apiTokenDao.getApiToken(credentials.getApiKey()));
    if (!apiToken.isPresent()) {
      return Optional.absent();
    }
    
    // Check their secret key
    if (apiToken.get().getApiSecret() == null) {
    	LOGGER.error("App {0} has a null secret key!", apiToken.get().getApiKey());
    	return Optional.absent();
    }

    String computedSignature = new String(
      HmacUtils.computeSignature(
        credentials.getAlgorithm(),
        credentials.getCanonicalRepresentation(),
        apiToken.get().getApiSecret()));
       
    // Avoid timing attacks by verifying every byte every time
    if (isEqual(computedSignature.getBytes(), credentials.getDigest().getBytes())) {
      return apiToken;
    }

    return Optional.absent();

  }

  public void setApiTokenDao(ApiTokenDao dao) {
    this.apiTokenDao = dao;
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
