package com.efp.common.config;

import freemarker.template.Configuration;

public class FreemarkerConfiguration extends Configuration {
	private String basePackagePath;

	public FreemarkerConfiguration() {
		this("");
	}

	public FreemarkerConfiguration(String basePackagePath) {
		super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		setDefaultEncoding("UTF-8");
		setClassForTemplateLoading(getClass(), basePackagePath);
		setClassicCompatible(true);
	}
}
