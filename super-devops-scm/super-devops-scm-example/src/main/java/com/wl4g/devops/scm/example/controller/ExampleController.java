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
package com.wl4g.devops.scm.example.controller;

import com.wl4g.devops.common.bean.iam.User;
import com.wl4g.devops.dao.iam.UserDao;
import com.wl4g.devops.scm.example.service.Example2;
import com.wl4g.devops.scm.example.service.ExampleService;
import com.wl4g.devops.tool.common.serialize.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RefreshScope
@RestController
@RequestMapping("/")
public class ExampleController implements ApplicationRunner {

	private AtomicBoolean running = new AtomicBoolean(false);

	private Thread thread;

	@Autowired
	private ExampleService exampleService;


	@Value("${spring.datasource.druid.url}")
	private String url;

	@Autowired
	private Example2 example2;


	@Autowired
	private UserDao userDao;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!running.compareAndSet(false, true)) {
			return;
		}

		System.out.println("ExampleService#afterPropertiesSet()..." + this);
		thread = new Thread(() -> {
			while (true) {
				System.out.println("ExampleService " + thread.getName() + ", firstName=" + exampleService.getFirstName()
						+ ", lastName=" + exampleService.getLastName() + " " + exampleService);
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}


	@RequestMapping(value = "test")
	public String test() {
		System.out.println("url = "+url);
		return url;
	}

	@RequestMapping(value = "test2")
	public String test2() {
		List<User> list = userDao.list(null, null, null);
		return JacksonUtils.toJSONString(list);
	}

	@RequestMapping(value = "test3")
	public String test3() {
		System.out.println("lastname = "+example2.getLastName());
		return example2.getLastName();
	}





}