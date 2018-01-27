package com.shilling.skillsheets.dao.local;

import java.nio.file.Paths;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Jake Shilling
 *
 */
public class LocalUserDaoFactory implements FactoryBean<LocalUserDao> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocalUserDao getObject() throws Exception {
		return new LocalUserDao (Paths.get(System.getProperty("user.home"), "users").toFile());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getObjectType() {
		return LocalUserDao.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
