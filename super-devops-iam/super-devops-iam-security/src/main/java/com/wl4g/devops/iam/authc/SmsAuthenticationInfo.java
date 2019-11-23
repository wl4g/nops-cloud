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
package com.wl4g.devops.iam.authc;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.wl4g.devops.iam.common.authc.IamAuthenticationInfo;
import com.wl4g.devops.iam.common.subject.IamPrincipalInfo;

public class SmsAuthenticationInfo extends SimpleAuthenticationInfo implements IamAuthenticationInfo {
	private static final long serialVersionUID = 1558934819432102687L;

	/**
	 * IAM account information.
	 */
	final private IamPrincipalInfo accountInfo;

	public SmsAuthenticationInfo(IamPrincipalInfo accountInfo, Object principal, Object credentials, String realmName) {
		this(accountInfo, principal, credentials, null, realmName);
	}

	public SmsAuthenticationInfo(IamPrincipalInfo accountInfo, Object principal, Object credentials, ByteSource credentialsSalt,
			String realmName) {
		this.accountInfo = accountInfo;
		this.principals = new SimplePrincipalCollection(principal, realmName);
		this.credentials = credentials;
		this.credentialsSalt = credentialsSalt;
	}

	@Override
	public IamPrincipalInfo getAccountInfo() {
		return accountInfo;
	}

}