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
package com.wl4g.devops.dts.codegen.bean;

import com.wl4g.components.core.bean.BaseBean;
import com.wl4g.devops.dts.codegen.utils.RenderPropertyUtils.RenderingProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@link GenTable}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-09-10
 * @since
 */
@Getter
@Setter
public class GenTable extends BaseBean {
	private static final long serialVersionUID = 6815608076300843748L;

	private Integer projectId;

	@RenderingProperty
	private String tableName;

	@RenderingProperty
	private String entityName;

	@RenderingProperty
	private String comments;

	@RenderingProperty
	private String parentTableName;

	@RenderingProperty
	private String parentTableFkName;

	@RenderingProperty
	private String moduleName;

	@RenderingProperty
	private String subModuleName;

	@RenderingProperty
	private String functionName;

	@RenderingProperty
	private String functionNameSimple;

	@RenderingProperty
	private String functionAuthor;

	@RenderingProperty
	private String options;

	@RenderingProperty
	private String status;

	// Extends
	@RenderingProperty
	private List<GenTableColumn> genTableColumns;

	@RenderingProperty(describeForObjField = "No")
	private GenTableColumn pk;

}