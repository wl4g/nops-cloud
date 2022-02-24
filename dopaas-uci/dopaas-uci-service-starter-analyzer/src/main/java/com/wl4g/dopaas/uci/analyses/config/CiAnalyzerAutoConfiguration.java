/*
 * Copyright 2017 ~ 2050 the original author or authors <Wanglsir@gmail.com, 983708408@qq.com>.
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
package com.wl4g.dopaas.uci.analyses.config;

import static com.wl4g.dopaas.common.constant.UciConstants.URL_ANALYZER_BASE_PATH;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wl4g.infra.core.web.mapping.PrefixHandlerMappingSupport;
import com.wl4g.dopaas.uci.analyses.coordinate.AnalysisCoordinator;
import com.wl4g.dopaas.uci.analyses.coordinate.SpotbugsAnalysisCoordinator;
import com.wl4g.dopaas.uci.analyses.model.SpotbugsAnalysingModel;
import com.wl4g.dopaas.uci.analyses.tasks.DefaultTaskManager;
import com.wl4g.dopaas.uci.analyses.tasks.TaskManager;
import com.wl4g.dopaas.uci.analyses.web.CodesAnalyzerController;

/**
 * CI analyzers auto configuration.
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2019年11月19日
 * @since
 */
@Configuration
public class CiAnalyzerAutoConfiguration extends PrefixHandlerMappingSupport {

	@Bean
	@ConfigurationProperties(prefix = "spring.cloud.devops.ci.analyzers")
	public CiAnalyzerProperties ciAnalyzerProperties() {
		return new CiAnalyzerProperties();
	}

	// --- Endpoint controller's. ---

	@Bean
	public CodesAnalyzerController codesAnalyzerController() {
		return new CodesAnalyzerController();
	}

	@Bean
	public Object codesAnalyzerControllerPrefixHandlerMapping() {
		return super.newPrefixHandlerMapping(URL_ANALYZER_BASE_PATH,
				com.wl4g.dopaas.uci.analyses.annotation.CodesAnalyzerController.class);
	}

	// --- Analysis corrdinator's. ---

	@Bean
	public AnalysisCoordinator<SpotbugsAnalysingModel> spotbugsCodesAnalyzer(CiAnalyzerProperties config) {
		return new SpotbugsAnalysisCoordinator(config.getExecutor());
	}

	// --- Tasks manager. ---

	@Bean
	public TaskManager defaultTaskManager(CiAnalyzerProperties config) {
		/**
		 * The initial capacity is equal to the maximum concurrent number, which
		 * can reduce the {@link ConcurrentHashMap} memory copy.
		 */
		return new DefaultTaskManager(config.getExecutor().getConcurrency());
	}

}