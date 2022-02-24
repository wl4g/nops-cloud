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

package com.wl4g.dopaas.common.bean.udm;

import com.wl4g.infra.core.bean.BaseBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * {@link EeDocumentRepo}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date Feb 3, 2021
 * @since v1.0
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EeDocumentRepo extends BaseBean {
    private static final long serialVersionUID = 348129690852034560L;

    /**
     * 
     */
    @NotNull
    private Long groupId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String gitUrl;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String logo;

    /**
     * 
     */
    private String token;

    /**
     * 可见性：1公开，2私有(team_id)
     */
    @NotNull
    private String visibility;

    /**
     * 
     */
    private String canUserEdit;

    /**
     * 
     */
    private Long lockerId;

    /**
     * 
     */
    private String locker;

    /**
     * 组织编码
     */
    private String organizationCode;

    public EeDocumentRepo() {
    }

    public EeDocumentRepo withGroupId(Long groupId) {
        setGroupId(groupId);
        return this;
    }

    public EeDocumentRepo withName(String name) {
        setName(name);
        return this;
    }

    public EeDocumentRepo withGitUrl(String gitUrl) {
        setGitUrl(gitUrl);
        return this;
    }

    public EeDocumentRepo withDescription(String description) {
        setDescription(description);
        return this;
    }

    public EeDocumentRepo withLogo(String logo) {
        setLogo(logo);
        return this;
    }

    public EeDocumentRepo withToken(String token) {
        setToken(token);
        return this;
    }

    public EeDocumentRepo withVisibility(String visibility) {
        setVisibility(visibility);
        return this;
    }

    public EeDocumentRepo withCanUserEdit(String canUserEdit) {
        setCanUserEdit(canUserEdit);
        return this;
    }

    public EeDocumentRepo withLockerId(Long lockerId) {
        setLockerId(lockerId);
        return this;
    }

    public EeDocumentRepo withLocker(String locker) {
        setLocker(locker);
        return this;
    }

    public EeDocumentRepo withOrganizationCode(String organizationCode) {
        setOrganizationCode(organizationCode);
        return this;
    }
}