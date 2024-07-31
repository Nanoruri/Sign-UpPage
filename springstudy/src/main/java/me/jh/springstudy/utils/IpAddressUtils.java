package me.jh.springstudy.utils;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class IpAddressUtils {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(IpAddressUtils.class);

	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		logger.info("> X-FORWARDED-FOR : {}", ip);

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.info("> Proxy-Client-IP : {}", ip);
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.info(">  WL-Proxy-Client-IP : {}", ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			logger.info("> HTTP_CLIENT_IP : {}", ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.info("> HTTP_X_FORWARDED_FOR : {}", ip);
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
			logger.info("> getRemoteAddr : {}", ip);
		}


		logger.info("> Result : IP Address : {}", ip);

		return ip;
	}
}
