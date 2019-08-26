/*
 * Copyright 2017 ~ 2025 the original author or authors.
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
package com.wl4g.devops.iam.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import static com.google.common.base.Charsets.*;
import static org.apache.commons.codec.binary.Hex.*;
import static org.apache.commons.lang3.StringUtils.*;
import static com.wl4g.devops.common.constants.IAMDevOpsConstants.*;

/**
 * IAM security utility tools.
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年5月21日
 * @since
 */
public abstract class Securitys {

	/**
	 * Default authentication status.
	 */
	final public static String SESSION_STATUS_AUTHC = "Authenticated";

	/**
	 * Default Unauthenticated status.
	 */
	final public static String SESSION_STATUS_UNAUTHC = "Unauthenticated";

	/**
	 * Safety limiting factor(e.g. Client remote IP and loginId)
	 * 
	 * @param remoteHost
	 * @param uid
	 * @return
	 */
	public static List<String> createLimitFactors(String remoteHost, String uid) {
		return new ArrayList<String>(2) {
			private static final long serialVersionUID = -5976569540781454836L;
			{
				String uidFactor = createUIDLimitFactor(uid);
				if (isNotBlank(uidFactor)) {
					add(uidFactor);
				}

				String hostFactor = createAddrFactor(remoteHost);
				if (isNotBlank(hostFactor)) {
					add(hostFactor);
				}
			}
		};
	}

	/**
	 * Create limit remote host factor.
	 * 
	 * @param remoteHost
	 * @return
	 */
	public static String createAddrFactor(String remoteHost) {
		return isNotBlank(remoteHost) ? (KEY_FAIL_LIMIT_RIP_PREFIX + encodeHexString(remoteHost.getBytes(UTF_8))) : EMPTY;
	}

	/**
	 * Create limit login UID factor.
	 * 
	 * @param uid
	 * @return
	 */
	public static String createUIDLimitFactor(String uid) {
		return isNotBlank(uid) ? (KEY_FAIL_LIMIT_UID_PREFIX + uid) : EMPTY;
	}

	/**
	 * Current session authentication status.
	 * 
	 * @return
	 */
	public static String sessionStatus() {
		return SecurityUtils.getSubject().isAuthenticated() ? SESSION_STATUS_AUTHC : SESSION_STATUS_UNAUTHC;
	}

}