package com.shilling.skillsheets.dao.local;

import java.nio.file.Paths;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Jake Shilling
 *
 */
public class LocalSkillSheetDaoFactory implements FactoryBean<LocalSkillSheetDao> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocalSkillSheetDao getObject() throws Exception {
		return new LocalSkillSheetDao (Paths.get(System.getProperty("user.home"), "sheets").toFile());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getObjectType() {
		return LocalSkillSheetDao.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
