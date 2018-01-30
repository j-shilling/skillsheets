package com.shilling.skillsheets.dao.local;

import java.nio.file.Paths;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Jake Shilling
 *
 */
public class LocalUserGroupDaoFactory implements FactoryBean<LocalUserGroupDao> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocalUserGroupDao getObject() throws Exception {
		return new LocalUserGroupDao (Paths.get(System.getProperty("user.home"), "users").toFile());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getObjectType() {
		return LocalUserGroupDao.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
