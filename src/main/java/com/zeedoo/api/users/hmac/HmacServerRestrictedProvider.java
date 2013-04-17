package com.zeedoo.api.users.hmac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Authenticator;

/**
 * <p>Authentication provider to provide the following to Jersey:</p>
 * <ul>
 * <li>Bridge between Dropwizard and Jersey for HMAC authentication</li>
 * <li>Provides additional {@link org.multibit.mbm.auth.Authority} information</li>
 * </ul>
 *
 * @param <T>    the principal type.
 * @since 0.0.1
 */
public class HmacServerRestrictedProvider<T> implements InjectableProvider<Restricted, Parameter> {
  
  static final Logger LOGGER = LoggerFactory.getLogger(HmacServerRestrictedProvider.class);

  private final Authenticator<HmacServerCredentials, T> authenticator;
  private final String realm;

  /**
   * Creates a new {@link HmacServerRestrictedProvider} with the given {@link com.yammer.dropwizard.auth.Authenticator} and realm.
   *
   * @param authenticator the authenticator which will take the {@link HmacServerCredentials} and
   *                      convert them into instances of {@code T}
   * @param realm         the name of the authentication realm
   */
  public HmacServerRestrictedProvider(Authenticator<HmacServerCredentials, T> authenticator, String realm) {
    this.authenticator = authenticator;
    this.realm = realm;
  }

  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

  @Override
  public Injectable<?> getInjectable(ComponentContext ic,
                                     Restricted a,
                                     Parameter c) {
    return new HmacServerRestrictedInjectable<T>(authenticator, realm);
  }
}

