package com.zeedoo.core.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.nhuray.dropwizard.spring.SpringBundle;
import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.zeedoo.core.api.config.AppConfiguration;
import com.zeedoo.core.api.config.ConfigConstants;
import com.zeedoo.core.api.domain.User;
import com.zeedoo.core.api.hmac.HmacServerAuthenticator;
import com.zeedoo.core.api.hmac.HmacServerCredentials;
import com.zeedoo.core.api.hmac.HmacServerRestrictedProvider;
import com.zeedoo.core.api.resources.aspect.TimedResourceMethodDispatchAdapter;

public class App extends Service<AppConfiguration> {
		
	private ApplicationContext context;

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		context = applicationContext();
		SpringBundle bundle = new SpringBundle((ConfigurableApplicationContext) context, true, true);
		bootstrap.addBundle(bundle);
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment)
			throws Exception {
		// Configure the authenticator
		HmacServerAuthenticator hmacAuthenticator = context.getBean(HmacServerAuthenticator.class);
		CachingAuthenticator<HmacServerCredentials, User> cachingAuthenticator = CachingAuthenticator
				.wrap(hmacAuthenticator, CacheBuilderSpec.parse("maximumSize=10000, expireAfterAccess=10m"));
	    // Providers
		environment.addProvider(new HmacServerRestrictedProvider<User>(cachingAuthenticator, "REST"));
		environment.addProvider(new TimedResourceMethodDispatchAdapter());
	}

	private ConfigurableApplicationContext applicationContext()
			throws BeansException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan(ConfigConstants.SPRING_BASE_PACKAGE);
		return context;
	}

}
