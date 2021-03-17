// Generated by XCloud DoPaaS for Codegen, refer: http://dts.devops.wl4g.com

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
package com.wl4g.dopaas.udm.web;

import com.wl4g.component.common.web.rest.RespBase;
import com.wl4g.component.core.page.PageHolder;
import com.wl4g.component.core.web.BaseController;
import com.wl4g.component.rpc.feign.core.context.RpcContextHolder;
import com.wl4g.dopaas.common.bean.udm.EeDocumentRepo;
import com.wl4g.dopaas.udm.service.EeDocumentRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
* {@link EeDocumentRepo}
*
* @author root
* @version 0.0.1-SNAPSHOT
* @Date 
* @since v1.0
*/
@RestController
@RequestMapping("/eedocumentrepo")
public class EeDocumentRepoController extends BaseController {

    @Autowired
    private EeDocumentRepoService eeDocumentRepoService;

    @RequestMapping(value = "/list", method = { GET })
    public RespBase<PageHolder<EeDocumentRepo>> list(PageHolder<EeDocumentRepo> pm, EeDocumentRepo eeDocumentRepo) {
        RespBase<PageHolder<EeDocumentRepo>> resp = RespBase.create();
        RpcContextHolder.get().set("pm",pm);
        resp.setData(eeDocumentRepoService.page(eeDocumentRepo));
        return resp;
    }

    @RequestMapping(value = "/save", method = { POST, PUT })
    public RespBase<?> save(@RequestBody EeDocumentRepo eeDocumentRepo) {
        RespBase<Object> resp = RespBase.create();
        eeDocumentRepoService.save(eeDocumentRepo);
        return resp;
    }

    @RequestMapping(value = "/detail", method = { GET })
    public RespBase<EeDocumentRepo> detail(@RequestParam(required = true) Long id) {
        RespBase<EeDocumentRepo> resp = RespBase.create();
        resp.setData(eeDocumentRepoService.detail(id));
        return resp;
    }

    @RequestMapping(value = "/del", method = { POST, DELETE })
    public RespBase<?> del(@RequestParam(required = true) Long id) {
        RespBase<Object> resp = RespBase.create();
        eeDocumentRepoService.del(id);
        return resp;
    }

}
