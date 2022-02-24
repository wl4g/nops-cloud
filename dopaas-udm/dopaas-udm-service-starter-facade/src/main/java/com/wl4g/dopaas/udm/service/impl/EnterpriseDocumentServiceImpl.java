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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl4g.infra.core.bean.BaseBean;
import com.wl4g.infra.core.page.PageHolder;
import com.wl4g.dopaas.common.bean.udm.EnterpriseDocument;
import com.wl4g.dopaas.udm.data.EnterpriseDocumentDao;
import com.wl4g.dopaas.udm.service.EnterpriseDocumentService;
import com.wl4g.dopaas.udm.service.model.EnterpriseDocumentPageRequest;

/**
 * service implements of {@link EnterpriseDocument}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date
 * @since v1.0
 */
@Service
public class EnterpriseDocumentServiceImpl implements EnterpriseDocumentService {

	private @Autowired EnterpriseDocumentDao enterpriseDocumentDao;

	@Override
	public PageHolder<EnterpriseDocument> page(EnterpriseDocumentPageRequest enterpriseDocumentPageRequest) {
		PageHolder<EnterpriseDocument> pm = enterpriseDocumentPageRequest.getPm();
		pm.useCount().bind();
		EnterpriseDocument enterpriseDocument = new EnterpriseDocument();
		BeanUtils.copyProperties(enterpriseDocumentPageRequest, enterpriseDocument);
		pm.setRecords(enterpriseDocumentDao.list(enterpriseDocument));
		return pm;
	}

	@Override
	public int save(EnterpriseDocument enterpriseDocument) {
		if (isNull(enterpriseDocument.getId())) {
			enterpriseDocument.preInsert();
			return enterpriseDocumentDao.insertSelective(enterpriseDocument);
		} else {
			enterpriseDocument.preUpdate();
			return enterpriseDocumentDao.updateByPrimaryKeySelective(enterpriseDocument);
		}
	}

	@Override
	public EnterpriseDocument detail(Long id) {
		notNullOf(id, "id");
		return enterpriseDocumentDao.selectByPrimaryKey(id);
	}

	@Override
	public int del(Long id) {
		notNullOf(id, "id");
		EnterpriseDocument enterpriseDocument = new EnterpriseDocument();
		enterpriseDocument.setId(id);
		enterpriseDocument.setDelFlag(BaseBean.DEL_FLAG_DELETE);
		return enterpriseDocumentDao.updateByPrimaryKeySelective(enterpriseDocument);
	}

}
