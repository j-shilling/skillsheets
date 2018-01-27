package com.shilling.skillsheets.dao.local;

import java.nio.file.Paths;

import org.springframework.beans.factory.FactoryBean;

public class LocalSkillSheetDaoFactory implements FactoryBean<LocalSkillSheetDao> {

	@Override
	public LocalSkillSheetDao getObject() throws Exception {
		return new LocalSkillSheetDao (Paths.get(System.getProperty("user.home"), "skillsheets").toFile());
	}

	@Override
	public Class<?> getObjectType() {
		return LocalSkillSheetDao.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
