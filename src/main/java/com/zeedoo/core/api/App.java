package com.zeedoo.core.api;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.nhuray.dropwizard.spring.SpringBundle;
import com.google.common.cache.CacheBuilderSpec;
import com.sun.jersey.core.impl.provider.entity.StreamingOutputProvider;
import com.sun.jersey.multipart.impl.MultiPartConfigProvider;
import com.sun.jersey.multipart.impl.MultiPartReaderServerSide;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.FilterBuilder;
import com.zeedoo.commons.domain.ApiToken;
import com.zeedoo.core.api.config.AppConfiguration;
import com.zeedoo.core.api.constants.Constants;
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
		CachingAuthenticator<HmacServerCredentials, ApiToken> cachingAuthenticator = CachingAuthenticator
				.wrap(hmacAuthenticator, CacheBuilderSpec.parse("maximumSize=10000, expireAfterAccess=10m"));
	    // Providers
		environment.addProvider(new HmacServerRestrictedProvider<ApiToken>(cachingAuthenticator, "REST"));
		environment.addProvider(MultiPartConfigProvider.class); // for multi-part upload
		environment.addProvider(MultiPartReaderServerSide.class);
		environment.addProvider(StreamingOutputProvider.class);
		environment.addProvider(new TimedResourceMethodDispatchAdapter());
		// Important - This makes Jackson writes out actual dates rather than timestamps for Joda DateTime objects
		environment.getObjectMapperFactory().disable(
		        com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// Filter, for Javascript cross-origin filter
		FilterBuilder filterConfig = environment.addFilter(CrossOriginFilter.class, "/*");
		filterConfig.setInitParam(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filterConfig.setInitParam(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
		filterConfig.setInitParam(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
		filterConfig.setInitParam(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
		filterConfig.setInitParam(CrossOriginFilter.PREFLIGHT_MAX_AGE_PARAM, String.valueOf(60*60*24));
	}

	private ConfigurableApplicationContext applicationContext()
			throws BeansException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan(Constants.SPRING_BASE_PACKAGE);
		return context;
	}

}
