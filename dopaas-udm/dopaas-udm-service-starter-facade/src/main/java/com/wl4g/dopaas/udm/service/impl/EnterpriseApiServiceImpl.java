// Generated by DoPaaS for Codegen, refer: http://dts.devops.wl4g.com

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

package com.wl4g.dopaas.udm.service.impl;

import static com.wl4g.infra.common.lang.Assert2.notNullOf;
import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.wl4g.infra.common.id.SnowflakeIdGenerator;
import com.wl4g.infra.common.lang.Assert2;
import com.wl4g.infra.core.bean.BaseBean;
import com.wl4g.infra.core.page.PageHolder;
import com.wl4g.dopaas.common.bean.udm.EnterpriseApi;
import com.wl4g.dopaas.common.bean.udm.EnterpriseApiModule;
import com.wl4g.dopaas.common.bean.udm.EnterpriseApiProperties;
import com.wl4g.dopaas.common.bean.udm.EnterpriseRepositoryVersion;
import com.wl4g.dopaas.common.bean.udm.model.XCloudDocumentModel;
import com.wl4g.dopaas.udm.data.EnterpriseApiDao;
import com.wl4g.dopaas.udm.data.EnterpriseApiModuleDao;
import com.wl4g.dopaas.udm.data.EnterpriseApiPropertiesDao;
import com.wl4g.dopaas.udm.data.EnterpriseRepositoryVersionDao;
import com.wl4g.dopaas.udm.service.EnterpriseApiService;
import com.wl4g.dopaas.udm.service.conversion.DocumentConverter;
import com.wl4g.dopaas.udm.service.conversion.DocumentConverterAdapter;
import com.wl4g.dopaas.udm.service.model.EnterpriseApiPageRequest;

/**
 * service implements of {@link EnterpriseApi}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date
 * @since v1.0
 */
@Service
public class EnterpriseApiServiceImpl implements EnterpriseApiService {

	private @Autowired EnterpriseApiDao enterpriseApiDao;

	private @Autowired EnterpriseApiPropertiesDao enterpriseApiPropertiesDao;

	private @Autowired EnterpriseApiModuleDao enterpriseApiModuleDao;

	private @Autowired EnterpriseRepositoryVersionDao enterpriseRepositoryVersionDao;

	private @Autowired DocumentConverterAdapter documentConverterAdapter;
	// private GenericOperatorAdapter<DocumentConverter.ConverterProviderKind,
	// Rap2DocumentConverter> documentConverterAdapter;

	@Override
	public PageHolder<EnterpriseApi> page(EnterpriseApiPageRequest enterpriseApiPageRequest) {
		PageHolder<EnterpriseApi> pm = enterpriseApiPageRequest.getPm();
		pm.useCount().bind();
		EnterpriseApi enterpriseApi = new EnterpriseApi();
		BeanUtils.copyProperties(enterpriseApiPageRequest, enterpriseApi);
		pm.setRecords(enterpriseApiDao.list(enterpriseApi));
		return pm;
	}

	@Override
	public List<EnterpriseApi> getByModuleId(Long moduleId) {
		return enterpriseApiDao.getByModuleId(moduleId);
	}

	@Override
	public int save(EnterpriseApi enterpriseApi) {

		// insert or update Properties
		List<EnterpriseApiProperties> properties = enterpriseApi.getProperties();
		List<EnterpriseApiProperties> list = new ArrayList<>();
		tree2List(properties, list, 0L);

		int result = 0;
		if (isNull(enterpriseApi.getId())) {
			enterpriseApi.preInsert();
			result = enterpriseApiDao.insertSelective(enterpriseApi);
		} else {
			enterpriseApi.preUpdate();
			result = enterpriseApiDao.updateByPrimaryKeySelective(enterpriseApi);
		}

		enterpriseApiPropertiesDao.deleteByApiId(enterpriseApi.getId());
		if (list.size() > 0) {
			enterpriseApiPropertiesDao.insertBatch(list, enterpriseApi.getId());
		}
		return result;
	}

	private void tree2List(List<EnterpriseApiProperties> tree, List<EnterpriseApiProperties> list, Long parentId) {
		if (CollectionUtils.isEmpty(tree)) {
			return;
		}
		for (EnterpriseApiProperties enterpriseApiProperties : tree) {
			enterpriseApiProperties.setId(SnowflakeIdGenerator.getDefault().nextId());
			enterpriseApiProperties.preInsert();
			enterpriseApiProperties.setParentId(parentId);
			list.add(enterpriseApiProperties);
			if (!CollectionUtils.isEmpty(enterpriseApiProperties.getChildren())) {
				tree2List(enterpriseApiProperties.getChildren(), list, enterpriseApiProperties.getId());
			}
		}
	}

	@Override
	public EnterpriseApi detail(Long id) {
		notNullOf(id, "id");
		EnterpriseApi enterpriseApi = enterpriseApiDao.selectByPrimaryKey(id);
		enterpriseApi.setProperties(getApiProperties(id));
		return enterpriseApi;
	}

	private List<EnterpriseApiProperties> getApiProperties(Long apiId) {
		List<EnterpriseApiProperties> enterpriseApiProperties = enterpriseApiPropertiesDao.selectByApiId(apiId);

		List<EnterpriseApiProperties> tops = enterpriseApiProperties.stream().filter(properties -> {
			return properties.getParentId() <= 0;
		}).collect(Collectors.toList());

		for (EnterpriseApiProperties top : tops) {
			top.setChildren(getChildren(enterpriseApiProperties, top.getId()));
		}
		return tops;
	}

	private List<EnterpriseApiProperties> getChildren(List<EnterpriseApiProperties> list, Long parentId) {
		List<EnterpriseApiProperties> children = list.stream().filter(properties -> {
			return properties.getParentId().equals(parentId);
		}).collect(Collectors.toList());
		for (EnterpriseApiProperties child : children) {
			child.setChildren(getChildren(list, child.getId()));
		}
		return children;
	}

	@Override
	public int del(Long id) {
		notNullOf(id, "id");
		EnterpriseApi enterpriseApi = new EnterpriseApi();
		enterpriseApi.setId(id);
		enterpriseApi.setDelFlag(BaseBean.DEL_FLAG_DELETE);
		return enterpriseApiDao.updateByPrimaryKeySelective(enterpriseApi);
	}

	@Override
	public List<String> getConverterProviderKind() {
		return DocumentConverter.ConverterProviderKind.getNames();
	}

	@Override
	public void importApi(String kind, String json, Long moduleId) {
		Assert2.hasTextOf(kind, "kind");
		Assert2.hasTextOf(json, "json");
		Assert2.notNullOf(moduleId, "moduleId");

		XCloudDocumentModel xCloudDocumentModel = documentConverterAdapter.forOperator(kind).convertFrom(json);
		// TODO save into db -- xCloudDocumentModel
		List<EnterpriseApi> enterpriseApis = xCloudDocumentModel.getEnterpriseApis();
		if (CollectionUtils.isEmpty(enterpriseApis)) {
			return;
		}
		for (EnterpriseApi enterpriseApi : enterpriseApis) {
			EnterpriseApi enterpriseApiFormDB = enterpriseApiDao.selectByModuleIdAndUrl(moduleId, enterpriseApi.getUrl());
			enterpriseApi.setModuleId(moduleId);
			if (enterpriseApiFormDB != null) {// update
				enterpriseApi.setId(enterpriseApiFormDB.getId());
			}
			if (StringUtils.isBlank(enterpriseApi.getName())) {
				if (StringUtils.isNotBlank(enterpriseApi.getDescription())) {
					enterpriseApi.setName(enterpriseApi.getDescription());
				} else {
					enterpriseApi.setName(enterpriseApi.getUrl());
				}
			}
			save(enterpriseApi);
		}
	}

	@Override
	public String exportApi(String kind, Long moduleId) throws IOException {
		List<EnterpriseApi> enterpriseApis = enterpriseApiDao.getByModuleId(moduleId);
		List<EnterpriseApi> enterpriseApiList = new ArrayList<>();
		for (EnterpriseApi enterpriseApi : enterpriseApis) {
			EnterpriseApi detail = detail(enterpriseApi.getId());
			enterpriseApiList.add(detail);
		}
		XCloudDocumentModel xCloudDocumentModel = new XCloudDocumentModel(enterpriseApiList);
		return documentConverterAdapter.forOperator(kind).convertToJson(xCloudDocumentModel);
	}

	@Override
	public void importApiAndUpdateVersion(String kind, String json, Long repositoryId) {
		List<EnterpriseRepositoryVersion> versionsByRepositoryId = enterpriseRepositoryVersionDao
				.getVersionsByRepositoryId(repositoryId);

		String newVersion = getNewVersion(versionsByRepositoryId);

		EnterpriseRepositoryVersion enterpriseRepositoryVersion = new EnterpriseRepositoryVersion();
		enterpriseRepositoryVersion.setVersion(newVersion);
		enterpriseRepositoryVersion.setRepositoryId(repositoryId);
		enterpriseRepositoryVersion.preInsert();
		enterpriseRepositoryVersionDao.insertSelective(enterpriseRepositoryVersion);

		EnterpriseApiModule enterpriseApiModule = new EnterpriseApiModule();
		enterpriseApiModule.setName("default");
		enterpriseApiModule.setParentId(0L);
		enterpriseApiModule.setVersionId(enterpriseRepositoryVersion.getId());
		enterpriseApiModule.preInsert();
		enterpriseApiModuleDao.insertSelective(enterpriseApiModule);

		importApi(kind, json, enterpriseApiModule.getId());
	}

	// TODO 暂时版本定死3级，两个点,例如(1.0.1)
	private String getNewVersion(List<EnterpriseRepositoryVersion> enterpriseRepositoryVersions) {
		List<EnterpriseRepositoryVersion> biggestVersion = new ArrayList<>();
		int biggestA = 0;
		int biggestB = 0;
		int biggestC = 0;
		for (EnterpriseRepositoryVersion enterpriseRepositoryVersion : enterpriseRepositoryVersions) {
			String version = enterpriseRepositoryVersion.getVersion();
			if (StringUtils.isBlank(version)) {
				continue;
			}
			String[] split = version.split("\\.");
			if (split.length < 3) {
				continue;
			}
			int a = Integer.parseInt(split[0]);
			int b = Integer.parseInt(split[1]);
			int c = Integer.parseInt(split[2]);
			if (a > biggestA) {
				biggestA = a;
				biggestB = b;
				biggestC = c;
			} else if (a == biggestA && b > biggestB) {
				biggestB = b;
				biggestC = c;
			} else if (a == biggestA && b == biggestB && c > biggestC) {
				biggestC = c;
			}
		}
		return String.format("%d.%d.%d", biggestA, biggestB, biggestC + 1);
	}

}
