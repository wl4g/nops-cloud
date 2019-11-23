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

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalMap;

import com.wl4g.devops.iam.common.authc.IamAuthenticationInfo;
import com.wl4g.devops.iam.common.subject.IamPrincipalInfo;

public class EmptyOauth2AuthorizationInfo implements IamAuthenticationInfo {
	private static final long serialVersionUID = -1824494219125412412L;

	/**
	 * Default only instance
	 */
	final public static EmptyOauth2AuthorizationInfo EMPTY = new EmptyOauth2AuthorizationInfo();

	/**
	 * Empty principal collection
	 */
	final private static PrincipalCollection emptyPrincipalCollection = new SimplePrincipalMap();

	@Override
	public PrincipalCollection getPrincipals() {
		return emptyPrincipalCollection;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public IamPrincipalInfo getAccountInfo() {
		return null;
	}

}