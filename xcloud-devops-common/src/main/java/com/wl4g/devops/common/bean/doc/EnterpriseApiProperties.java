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

package com.wl4g.devops.common.bean.doc;

import com.wl4g.components.core.bean.BaseBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link EnterpriseApiProperties}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date Dec 14, 2020
 * @since v1.0
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EnterpriseApiProperties extends BaseBean {
    private static final long serialVersionUID = 986012073648842752L;

    /**
     * 
     */
    private Long apiId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String scope;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private Long pos;

    /**
     * 
     */
    private String rule;

    /**
     * 
     */
    private String value;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Long parentid;

    /**
     * 
     */
    private Long priority;

    /**
     * 
     */
    private String required;

    public EnterpriseApiProperties() {
    }

    public EnterpriseApiProperties withApiId(Long apiId) {
        setApiId(apiId);
        return this;
    }

    public EnterpriseApiProperties withName(String name) {
        setName(name);
        return this;
    }

    public EnterpriseApiProperties withScope(String scope) {
        setScope(scope);
        return this;
    }

    public EnterpriseApiProperties withType(String type) {
        setType(type);
        return this;
    }

    public EnterpriseApiProperties withPos(Long pos) {
        setPos(pos);
        return this;
    }

    public EnterpriseApiProperties withRule(String rule) {
        setRule(rule);
        return this;
    }

    public EnterpriseApiProperties withValue(String value) {
        setValue(value);
        return this;
    }

    public EnterpriseApiProperties withDescription(String description) {
        setDescription(description);
        return this;
    }

    public EnterpriseApiProperties withParentid(Long parentid) {
        setParentid(parentid);
        return this;
    }

    public EnterpriseApiProperties withPriority(Long priority) {
        setPriority(priority);
        return this;
    }

    public EnterpriseApiProperties withRequired(String required) {
        setRequired(required);
        return this;
    }
}