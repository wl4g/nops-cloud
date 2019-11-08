/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.devops.iam.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;

import static com.wl4g.devops.iam.common.config.AbstractIamProperties.*;

import com.wl4g.devops.common.config.AbstractOptionalControllerAutoConfiguration;
import com.wl4g.devops.iam.web.DefaultViewController;

/**
 * Default view configuration
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年1月8日
 * @since
 */
@AutoConfigureAfter({ IamAutoConfiguration.class })
public class DefaultViewAutoConfiguration extends AbstractOptionalControllerAutoConfiguration {

	@Bean
	public DefaultViewController defaultViewController() {
		return new DefaultViewController();
	}

	@Override
	protected String getMappingPrefix() {
		return DEFAULT_VIEW_BASE_URI;
	}

	@Bean
	public PrefixHandlerMapping defaultViewControllerPrefixHandlerMapping() {
		return super.createPrefixHandlerMapping();
	}

	@Override
	protected Class<? extends Annotation> annotationClass() {
		return com.wl4g.devops.iam.annotation.DefaultViewController.class;
	}

}