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
package com.wl4g.devops.iam.common.mgt;

import static com.google.common.base.Charsets.UTF_8;
import static com.wl4g.devops.common.constants.IAMDevOpsConstants.KEY_ACCESSTOKEN_SIGN_KEY;
import static com.wl4g.devops.common.constants.IAMDevOpsConstants.KEY_AUTHC_TOKEN;
import static com.wl4g.devops.tool.common.codec.Base58.encode;
import static com.wl4g.devops.tool.common.codec.Encodes.toBytes;
import static com.wl4g.devops.tool.common.lang.Assert2.hasText;
import static com.wl4g.devops.tool.common.lang.Assert2.hasTextOf;
import static com.wl4g.devops.tool.common.lang.Assert2.notNullOf;
import static com.wl4g.devops.tool.common.log.SmartLoggerFactory.getLogger;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import static org.apache.commons.codec.digest.HmacUtils.hmacSha256Hex;
import static org.apache.shiro.web.util.WebUtils.getCleanParam;
import static org.apache.shiro.web.util.WebUtils.toHttp;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;

import com.wl4g.devops.common.exception.iam.UnauthenticatedException;
import com.wl4g.devops.iam.common.authc.IamAuthenticationToken;
import com.wl4g.devops.iam.common.config.AbstractIamProperties;
import com.wl4g.devops.iam.common.config.AbstractIamProperties.ParamProperties;
import com.wl4g.devops.tool.common.log.SmartLogger;

/**
 * {@link org.apache.shiro.mgt.SubjectFactory Subject} implementation to be used
 * in CAS-enabled applications.
 *
 * @since 1.2
 */
public class IamSubjectFactory extends DefaultWebSubjectFactory {

	final protected SmartLogger log = getLogger(getClass());

	final protected AbstractIamProperties<? extends ParamProperties> config;

	public IamSubjectFactory(AbstractIamProperties<? extends ParamProperties> config) {
		notNullOf(config, "config");
		this.config = config;
	}

	@Override
	public Subject createSubject(SubjectContext context) {
		// the authenticated flag is only set by the SecurityManager after a
		// successful authentication attempt.
		//
		// although the SecurityManager 'sees' the submission as a successful
		// authentication, in reality, the
		// login might have been just a CAS rememberMe login. If so, set the
		// authenticated flag appropriately:
		if (context.isAuthenticated()) {
			AuthenticationToken token = context.getAuthenticationToken();
			if (!isNull(token) && token instanceof RememberMeAuthenticationToken) {
				RememberMeAuthenticationToken tk = (RememberMeAuthenticationToken) token;
				// set the authenticated flag of the context to true only if the
				// CAS subject is not in a remember me mode
				if (tk.isRememberMe()) {
					context.setAuthenticated(false);
				}
			}
		}

		// Validation of enhanced session additional signature.
		if (config.getSession().isEnableAccessTokenValidity() && context.isAuthenticated()) {
			try {
				assertRequestAccessTokenValidity(context);
			} catch (UnauthenticatedException e) {
				// #Forced sets notauthenticated
				context.setAuthenticated(false);
				if (log.isDebugEnabled())
					log.debug(e.getMessage(), e);
				else
					log.warn(e.getMessage());
			}
		}

		return super.createSubject(context);
	}

	/**
	 * Assertion request accessToken(signature) validity.
	 * 
	 * @param context
	 * @throws UnauthenticatedException
	 * @see {@link AbstractIamAuthenticationFilter#makeLoggedResponse}
	 */
	final private void assertRequestAccessTokenValidity(SubjectContext context) throws UnauthenticatedException {
		// Additional signature verification will only be performed on those
		// who have logged in successful.
		// e.g: Authentication requests or internal API requests does not
		// require signature verification.
		if (context.isAuthenticated() || isNull(context.getSession()))
			return;

		WebSubjectContext wsc = (WebSubjectContext) context;
		Session session = wsc.getSession();
		HttpServletRequest request = toHttp(wsc.resolveServletRequest());

		String sessionId = valueOf(session.getId());
		String accessTokenSignKey = (String) session.getAttribute(KEY_ACCESSTOKEN_SIGN_KEY);
		final String accessToken = getCleanParam(request, config.getParam().getAccessTokenName());
		IamAuthenticationToken authcToken = (IamAuthenticationToken) session.getAttribute(KEY_AUTHC_TOKEN);
		log.debug("Asserting accessToken, sessionId:{}, accessTokenSignKey: {}, authcToken: {}, accessToken: {}", sessionId,
				accessTokenSignKey, authcToken, accessToken);

		// Only the account-password authentication is verified.
		// if (authcToken instanceof ClientSecretIamAuthenticationToken) {
		hasText(accessToken, UnauthenticatedException.class, "accessToken is required");
		hasText(sessionId, UnauthenticatedException.class, "sessionId is required");
		hasTextOf(accessTokenSignKey, "accessTokenSignKey"); // Shouldn't-here

		// Calculating accessToken(signature).
		final String validAccessToken = generateAccessToken(session, accessTokenSignKey);
		log.debug(
				"Asserted accessToken of sessionId: {}, accessTokenSignKey: {}, validAccessToken: {}, accessToken: {}, authcToken: {}",
				sessionId, accessTokenSignKey, validAccessToken, accessToken, authcToken);

		// Compare accessToken(signature)
		if (!accessToken.equals(validAccessToken)) {
			throw new UnauthenticatedException(
					format("Illegal authentication credentials signature. accessToken: {}, accessTokenSignKey: {}", accessToken,
							accessTokenSignKey));
		}
		// }

	}

	/**
	 * Generate new generate accessToken string.
	 * 
	 * @param session
	 * @param accessTokenSignKey
	 * @return
	 */
	final public static String generateAccessToken(@NotBlank final Session session, @NotBlank final String accessTokenSignKey) {
		notNullOf(session, "session");
		hasTextOf(accessTokenSignKey, "accessTokenSignKey");
		return encode(hmacSha256Hex(toBytes(accessTokenSignKey), valueOf(session.getId()).getBytes(UTF_8)).getBytes(UTF_8));
	}

}