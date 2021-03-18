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
package com.wl4g.devops.uos.common.model;

import static java.util.Objects.isNull;

import com.wl4g.devops.uos.common.model.ObjectMetadata;

public class ObjectSymlink {

	/**
	 * Symlink file key
	 */
	private String symlink;

	/**
	 * The original file's key
	 */
	private String target;

	/**
	 * The symlink file's metadata.
	 */
	private ObjectMetadata metadata = new ObjectMetadata();

	public String getSymlink() {
		return symlink;
	}

	public void setSymlink(String symlink) {
		this.symlink = symlink;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public ObjectMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(ObjectMetadata metadata) {
		if (!isNull(metadata)) {
			this.metadata = metadata;
		}
	}

}