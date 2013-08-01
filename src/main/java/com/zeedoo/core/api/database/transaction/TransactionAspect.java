package com.zeedoo.core.api.database.transaction;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.core.api.database.SqlService;

@Component
@Aspect
public class TransactionAspect {
		
	@Autowired
	private SqlService sqlService;
		
	@Around("@annotation(transactional)")
	public Object aroundPerformTransaction(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
		Logger underlyingLogger = LoggerFactory.getLogger(pjp.getSignature().getDeclaringType());
		SqlSessionFactory factory = sqlService.getSessionFactory();
		SqlSession session = factory.openSession();
		Object object = null;
		try {
			object = pjp.proceed();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			underlyingLogger.error("Error executing: " + pjp.getSignature() + ". Rolling back transaction...", t);
			throw t;
		} finally {
			session.close();
		}
		return object;
	}
}
