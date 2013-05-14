package com.zeedoo.core.api;

import com.yammer.dropwizard.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceConfiguration {
	Class<? extends Service> value();
	String setting();
}