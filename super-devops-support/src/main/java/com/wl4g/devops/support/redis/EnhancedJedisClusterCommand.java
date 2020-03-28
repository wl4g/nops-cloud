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
package com.wl4g.devops.support.redis;

import static java.lang.String.format;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.exceptions.JedisException;

/**
 * {@link EnhancedJedisClusterCommand}
 * 
 * @param <T>
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version 2020年3月28日 v1.0.0
 * @see
 */
public abstract class EnhancedJedisClusterCommand<T> extends JedisClusterCommand<T> {

	public EnhancedJedisClusterCommand(JedisClusterConnectionHandler connectionHandler, int maxAttempts) {
		super(connectionHandler, maxAttempts);
	}

	@Override
	public T execute(Jedis connection) {
		try {
			return doExecute(connection);
		} catch (JedisException e) {
			String node = connection.getClient().getHost() + ":" + connection.getClient().getPort();
			throw new JedisException(format("Couldn't execution jedis command of node: %s", node), e);
		}
	}

	/**
	 * DO execute
	 * 
	 * @param connection
	 * @return
	 */
	public abstract T doExecute(Jedis connection);

}
