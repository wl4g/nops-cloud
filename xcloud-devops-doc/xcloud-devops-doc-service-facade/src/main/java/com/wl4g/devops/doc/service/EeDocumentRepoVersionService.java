// Generated by XCloud DevOps for Codegen, refer: http://dts.devops.wl4g.com

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

package com.wl4g.devops.doc.service;

import com.wl4g.component.core.bean.model.PageHolder;
import com.wl4g.component.rpc.feign.core.annotation.FeignConsumer;
import com.wl4g.devops.common.bean.doc.EeDocumentRepoVersion;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *  service of {@link EeDocumentRepoVersion}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date 
 * @since v1.0
 */
@FeignConsumer(name = "${provider.serviceId.doc-facade:eeDocumentRepoVersionService}")
@RequestMapping("/eeDocumentRepoVersion")
public interface EeDocumentRepoVersionService {

    /**
     *  page query.
     *
     * @param pm
     * @param eeDocumentRepoVersion
     * @return 
     */
    @RequestMapping(value = "/page", method = POST)
    PageHolder<EeDocumentRepoVersion> page(@RequestBody EeDocumentRepoVersion eeDocumentRepoVersion);

    /**
     *  save.
     *
     * @param eeDocumentRepoVersion
     * @return 
     */
    @RequestMapping(value = "/save", method = POST)
    int save(@RequestBody EeDocumentRepoVersion eeDocumentRepoVersion);

    /**
     *  detail query.
     *
     * @param id
     * @return 
     */
    @RequestMapping(value = "/detail", method = POST)
    EeDocumentRepoVersion detail(@RequestParam(name = "id", required = false) Long id);

    /**
     *  delete.
     *
     * @param id
     * @return 
     */
    @RequestMapping(value = "/del", method = POST)
    int del(@RequestParam(name = "id", required = false) Long id);

}

