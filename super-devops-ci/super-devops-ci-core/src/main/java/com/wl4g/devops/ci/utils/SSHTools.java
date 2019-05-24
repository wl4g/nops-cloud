/*
 * Copyright 2015 the original author or authors.
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
package com.wl4g.devops.ci.utils;

import ch.ethz.ssh2.*;

import com.google.common.base.Charsets;
import com.wl4g.devops.shell.utils.ShellConsoleHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * SSH connection utility tools.
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年5月24日
 * @since
 */
public class SSHTools {
	final private static Logger log = Logger.getLogger(SSHTools.class);

	/**
	 * run in local
	 */
	public static String runLocal(String cmd) throws Exception {
		log.info("command:" + cmd);
		StringBuffer sb = new StringBuffer();
		StringBuffer se = new StringBuffer();

		Process ps = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		BufferedReader be = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
			log.info(line);
			ShellConsoleHolder.printfQuietly(line);
		}
		while ((line = be.readLine()) != null) {
			se.append(line).append("\n");
			log.info(line);
			ShellConsoleHolder.printfQuietly(line);
		}
		String result = sb.toString();
		String resulterror = se.toString();
		if (StringUtils.isNotBlank(resulterror)) {
			result += resulterror;
			throw new RuntimeException("exce command fail,command=" + cmd + "\n cause:" + result.toString());
		}

		return result;
	}

	/**
	 * execute with local rsa file
	 *
	 * @param ip
	 * @param command
	 * @return
	 */
	public static String execute(String ip, String userName, String command) throws Exception {
		Connection conn = null;
		try {
			boolean flag = false;
			conn = new Connection(ip);
			conn.connect();// connect
			String userHome = System.getProperties().getProperty("user.home");
			flag = conn.authenticateWithPublicKey(userName, new File(userHome + "/.ssh/id_rsa"), null);
			if (flag) {
				log.debug("login success！");
				return execute(conn, command);
			} else {
				log.error("login fail!");
				throw new RuntimeException("login fail");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != conn) {
				conn.close();
			}
		}
	}

	/**
	 * @param ip
	 * @param command
	 * @return
	 */
	public static String execute(String ip, String userName, String command, char[] rsa) throws Exception {
		log.info("exce command:" + command);
		ShellConsoleHolder.printfQuietly("exce command:" + command);
		Connection conn = null;
		try {
			boolean flag = false;
			conn = new Connection(ip);
			conn.connect();// connect
			flag = conn.authenticateWithPublicKey(userName, rsa, null);
			if (flag) {
				log.debug("login success！");
				String result = execute(conn, command);
				// logger.info(result);
				// ShellConsoleHolder.printfQuietly(result);
				return result;
			} else {
				log.error("login fail!");
				throw new RuntimeException("login fail");
			}
		} catch (Exception e) {
			ShellConsoleHolder.printfQuietly("exce fail:" + command + "\n" + e.getMessage());
			throw e;
		} finally {
			if (null != conn) {
				conn.close();
			}
		}
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param f
	 *            文件对象
	 * @param length
	 *            文件大小
	 * @param remoteTargetDirectory
	 *            上传路径
	 * @param mode
	 *            默认为null
	 */
	public static void uploadFile(String ip, String userName, char[] rsa, File f, String remoteTargetDirectory) {
		log.info("upload file begin: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
		ShellConsoleHolder.printfQuietly("upload file: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
		Connection conn = null;
		SCPOutputStream os = null;
		FileInputStream fis = null;
		try {
			boolean flag = false;
			conn = new Connection(ip);
			conn.connect();
			flag = conn.authenticateWithPublicKey(userName, rsa, null);
			if (flag) {
				log.debug("login success！");
				SCPClient scpClient = new SCPClient(conn);
				os = scpClient.put(f.getName(), f.length(), remoteTargetDirectory, "0744");
				byte[] b = new byte[4096];
				fis = new FileInputStream(f);
				int i;
				while ((i = fis.read(b)) != -1) {
					os.write(b, 0, i);
				}
				os.flush();
				log.info("upload file success: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
				ShellConsoleHolder.printfQuietly("upload file success: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
			} else {
				log.error("login fail!");
				throw new RuntimeException("login fail");
			}
		} catch (IOException e) {
			log.error("upload file fail: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
			ShellConsoleHolder.printfQuietly("upload file fail: " + f.getAbsolutePath() + " to " + remoteTargetDirectory);
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
				if (null != os) {
					os.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {

			}
		}
	}

	private static String execute(Connection conn, String cmd) {
		// logger.info("command=" + cmd);
		String result = "";
		Session session = null;
		try {
			session = conn.openSession();// open session
			session.execCommand(cmd);// execute
			result = processStdout(session.getStdout());
			// if result blank, get error msg
			if (StringUtils.isBlank(result)) {
				result = processStdout(session.getStderr());
				// throw new RuntimeException("execute fail result="+result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		// logger.info(result);
		return result;
	}

	private static String processStdout(InputStream in) {
		StringBuffer buffer = new StringBuffer();
		InputStream stdout = new StreamGobbler(in);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(stdout, Charsets.UTF_8))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (log.isInfoEnabled()) {
					log.info(line);
				}
				buffer.append(line);
				buffer.append("\n");

				ShellConsoleHolder.printfQuietly(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static void main(String[] args) {

		/*
		 * try { AES aes = new AES("03DE18C2FC4E605F"); String s =
		 * "-----BEGIN RSA PRIVATE KEY-----\n" +
		 * "MIIEpQIBAAKCAQEAwawifYZlHNdmkdMmXdi6wslkfvvAVjGo4cBPtrOFonD0Paex\n"
		 * +
		 * "tVRckfkj6rCu4IkKOq6HFBBf1peYVHojLFUm4FGC+YatxoLcdExBj8A/oMVsWN8a\n"
		 * +
		 * "ZWv5RH0lqUPZyuefqIrD+pos0R1hJtEDh5cKKT+Ae7kOP2+pX0QeGu0F/z9jozPo\n"
		 * +
		 * "PiM5DaoM0xaDqhmn1dnY03X3TAY8/V9Oy1zSRslXoiF2EmfTiaHBlvCeK5WhCiMd\n"
		 * +
		 * "5Xfn26Eiw3RBePh/eGiXjgv2ILZA7pnoINCa+PXI6VW6mthHQ8GJ7w+2afCGZBun\n"
		 * +
		 * "hxpGPEKih8YGNHBlGKUPale0pPMqI703iENZrQIDAQABAoIBAQCD2/qvk+0Lsev3\n"
		 * +
		 * "pNceVgzxycROYIEXLkBZU2Hydk+pxVXFFIN9fa55BDNb+mdWIHeCdIkrM+rMY/Im\n"
		 * +
		 * "sfF4oZEScOzHjtaJrVcDJ1gL00x+3WtjJqMGInlYFAysLbH+36xoR/IekRGqXmJi\n"
		 * +
		 * "1zOcAU29v6pukhQNRK0AW5RTqMTIfuQeR7Utpq4QNk8BSfDKJcyVoiTpaJ3Pv4AG\n"
		 * +
		 * "rPkIgtIqtmDJmp6MbMVtJA8AyxYrAiJbHntVeRddj2NkrScK/J4RG+5U/9Jj5cpB\n"
		 * +
		 * "VA21C7pt4lvUVuAPQ+esFLiwxBlIfW+sS5jaKCbJaeBL78xOiaLBJNWfrGR4USdu\n"
		 * +
		 * "k0P8o/sBAoGBAO6YeX2TDyVH/8XsSq9nPs+hu85wMqWohswG9R6oxcurVJLLKjdd\n"
		 * +
		 * "vqwpFaOUaQHcZr7iN2jGWqxxjFRW5qnwevk8uyXs9joSKV2C7KUdxuP9dtuqJNyl\n"
		 * +
		 * "+p1qN57+J69WXQNJoMKmiICK43fW89CoNucqJqrmsLzBkQiFmtR83ORxAoGBAM/M\n"
		 * +
		 * "xBTaVPrJYXxx10TZo+NcN5dv4FfDmK/Uz6v2F4y8xoRxpde8yeo0xj9CM8J2SX8w\n"
		 * +
		 * "712GLWI03m8RsYg/g2qRueK34lrA0eJ8GGUV2SJOqOQqlv1FJJVDp3658Cm0IyDT\n"
		 * +
		 * "CvyHJyrGmJljUhTgnwMuuzcp1wAfenWFp6OaAfb9AoGAXZMxOr29V+rH9nD4zZgZ\n"
		 * +
		 * "e0c8J/e69VuGGmi0I+UfRgSY88V4diRvDohCc1hWYqN1LHH+Nzpr/2u9FKrMZmPp\n"
		 * +
		 * "ZuyZnYM1Aoty67jYZN2rzmju/7HYKS1zf99TlyiomcyuSAbNZOn5aSiPk8Wa8/+1\n"
		 * +
		 * "IK5YYfh94lmsLwJvOd0KqRECgYEAxUa22L02dCh/Pm+tWRXt+0lvFXwG1gtBh5xX\n"
		 * +
		 * "0/97+Aa3yMFEGv6GCq0zkJa/INy/hdrlRDrAFz3t9jAsBReXIbNbcBv27wWjvIrn\n"
		 * +
		 * "dgA59dILkSHF2oir5HEoMK1BjbYQq3bwNTHyQy/ra6PZJyzgiVryLbqw/NLlpXDP\n"
		 * +
		 * "6Aer2dkCgYEAwKka1EYm5/N4krwsVvNBWD4Xgt4dtkGkQYkhyXZIqGTLFntIdVig\n"
		 * +
		 * "jKoQ6kaFTPaSST4kWNoXxNWvBDjarOriPa//St+l5fsEjfhjF1CfCS5aKvKfIwmP\n"
		 * + "jZss7kAharoCjXmxdyqPBEjJPHMts7d93olfGDGCvFrZnEfuD+zUcmU=\n" +
		 * "-----END RSA PRIVATE KEY-----"; System.out.println("cipherText: " +
		 * (s = aes.encrypt(s))); System.out.println("plainText: " +
		 * aes.decrypt(s)); } catch (Exception e) { e.printStackTrace(); }
		 */

		String ase = "-----BEGIN RSA PRIVATE KEY-----\n" + "MIIEpQIBAAKCAQEAwawifYZlHNdmkdMmXdi6wslkfvvAVjGo4cBPtrOFonD0Paex\n"
				+ "tVRckfkj6rCu4IkKOq6HFBBf1peYVHojLFUm4FGC+YatxoLcdExBj8A/oMVsWN8a\n"
				+ "ZWv5RH0lqUPZyuefqIrD+pos0R1hJtEDh5cKKT+Ae7kOP2+pX0QeGu0F/z9jozPo\n"
				+ "PiM5DaoM0xaDqhmn1dnY03X3TAY8/V9Oy1zSRslXoiF2EmfTiaHBlvCeK5WhCiMd\n"
				+ "5Xfn26Eiw3RBePh/eGiXjgv2ILZA7pnoINCa+PXI6VW6mthHQ8GJ7w+2afCGZBun\n"
				+ "hxpGPEKih8YGNHBlGKUPale0pPMqI703iENZrQIDAQABAoIBAQCD2/qvk+0Lsev3\n"
				+ "pNceVgzxycROYIEXLkBZU2Hydk+pxVXFFIN9fa55BDNb+mdWIHeCdIkrM+rMY/Im\n"
				+ "sfF4oZEScOzHjtaJrVcDJ1gL00x+3WtjJqMGInlYFAysLbH+36xoR/IekRGqXmJi\n"
				+ "1zOcAU29v6pukhQNRK0AW5RTqMTIfuQeR7Utpq4QNk8BSfDKJcyVoiTpaJ3Pv4AG\n"
				+ "rPkIgtIqtmDJmp6MbMVtJA8AyxYrAiJbHntVeRddj2NkrScK/J4RG+5U/9Jj5cpB\n"
				+ "VA21C7pt4lvUVuAPQ+esFLiwxBlIfW+sS5jaKCbJaeBL78xOiaLBJNWfrGR4USdu\n"
				+ "k0P8o/sBAoGBAO6YeX2TDyVH/8XsSq9nPs+hu85wMqWohswG9R6oxcurVJLLKjdd\n"
				+ "vqwpFaOUaQHcZr7iN2jGWqxxjFRW5qnwevk8uyXs9joSKV2C7KUdxuP9dtuqJNyl\n"
				+ "+p1qN57+J69WXQNJoMKmiICK43fW89CoNucqJqrmsLzBkQiFmtR83ORxAoGBAM/M\n"
				+ "xBTaVPrJYXxx10TZo+NcN5dv4FfDmK/Uz6v2F4y8xoRxpde8yeo0xj9CM8J2SX8w\n"
				+ "712GLWI03m8RsYg/g2qRueK34lrA0eJ8GGUV2SJOqOQqlv1FJJVDp3658Cm0IyDT\n"
				+ "CvyHJyrGmJljUhTgnwMuuzcp1wAfenWFp6OaAfb9AoGAXZMxOr29V+rH9nD4zZgZ\n"
				+ "e0c8J/e69VuGGmi0I+UfRgSY88V4diRvDohCc1hWYqN1LHH+Nzpr/2u9FKrMZmPp\n"
				+ "ZuyZnYM1Aoty67jYZN2rzmju/7HYKS1zf99TlyiomcyuSAbNZOn5aSiPk8Wa8/+1\n"
				+ "IK5YYfh94lmsLwJvOd0KqRECgYEAxUa22L02dCh/Pm+tWRXt+0lvFXwG1gtBh5xX\n"
				+ "0/97+Aa3yMFEGv6GCq0zkJa/INy/hdrlRDrAFz3t9jAsBReXIbNbcBv27wWjvIrn\n"
				+ "dgA59dILkSHF2oir5HEoMK1BjbYQq3bwNTHyQy/ra6PZJyzgiVryLbqw/NLlpXDP\n"
				+ "6Aer2dkCgYEAwKka1EYm5/N4krwsVvNBWD4Xgt4dtkGkQYkhyXZIqGTLFntIdVig\n"
				+ "jKoQ6kaFTPaSST4kWNoXxNWvBDjarOriPa//St+l5fsEjfhjF1CfCS5aKvKfIwmP\n"
				+ "jZss7kAharoCjXmxdyqPBEjJPHMts7d93olfGDGCvFrZnEfuD+zUcmU=\n" + "-----END RSA PRIVATE KEY-----";
		/*
		 * Connection conn = new Connection("10.0.0.161"); try {
		 * conn.connect();// connect boolean flag =
		 * conn.authenticateWithPublicKey("datachecker", ase.toCharArray(),
		 * null); //execute(conn,
		 * ". /etc/profile && . /etc/bashrc && . ~/.bash_profile && . ~/.bashrc && sc datachecker restart"
		 * ); if (flag) { logger.debug("login success！");
		 * uploadFile("10.0.0.161","datachecker",ase.toCharArray(),new File(
		 * "/Users/vjay/gittest/safecloud-devops-datachecker/boot/target/datachecker-master-bin.tar"
		 * ),"/home/ci/tmp"); } else { logger.error("login fail!"); } } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

		uploadFile("10.0.0.10", "root", ase.toCharArray(),
				new File("/Users/vjay/gittest/safecloud-devops-datachecker/boot/target/datachecker-master-bin.tar"), "/root/tmp");
	}

}