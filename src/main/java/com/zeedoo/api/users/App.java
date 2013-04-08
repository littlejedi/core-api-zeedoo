package com.zeedoo.api.users;

import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.nhuray.dropwizard.spring.SpringBundle;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.zeedoo.api.users.config.AppConfiguration;

public class App extends Service<AppConfiguration> {

	private static final String CONFIGURATION_FILE = "src/main/resources/com/zeedoo/api/users/config/config.yml";

	public static void main(String[] args) throws Exception {
		new App().run(new String[] { "server", CONFIGURATION_FILE });
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		SpringBundle bundle = new SpringBundle(applicationContext(), true, true);
		bootstrap.addBundle(bundle);
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment)
			throws Exception {
		// doing nothing
	}

	private ConfigurableApplicationContext applicationContext()
			throws BeansException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.zeedoo.api");
		return context;
	}

}
