/*
 * Copyright (C) 2017 ~ 2025 the original author or authors.
 * <Wanglsir@gmail.com, 983708408@qq.com> Technology CO.LTD.
 * All rights reserved.
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
 * 
 * Reference to website: http://wl4g.com
 */
package com.wl4g.devops.ci.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl4g.devops.ci.core.orchestration.OrchestrationManager;
import com.wl4g.devops.ci.service.OrchestrationManagerAdapterService;
import com.wl4g.devops.common.bean.ci.Orchestration;
import com.wl4g.devops.common.bean.ci.model.PipelineModel;

/**
 * {@link FlowPipelineManagerServiceImpl}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2021-01-22
 * @sine v1.0
 * @see
 */
@Service
public class FlowPipelineManagerServiceImpl implements OrchestrationManagerAdapterService {

	@Autowired
	private OrchestrationManager flowManager;

	@Override
	public void runOrchestration(Orchestration orchestration, String remark, String taskTraceId, String taskTraceType,
			String annex) {
		flowManager.runOrchestration(orchestration, remark, taskTraceId, taskTraceType, annex);
	}

	@Override
	public PipelineModel buildPipeline(Long pipelineId) {
		return flowManager.buildPipeline(pipelineId);
	}

}
