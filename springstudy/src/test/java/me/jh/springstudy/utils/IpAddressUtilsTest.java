package me.jh.springstudy.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpAddressUtilsTest {

		@Mock
		private HttpServletRequest request;

		@Test
		public void getClientIpReturnsXForwardedFor() {
			String ip = "192.168.1.1";
			when(request.getHeader("X-Forwarded-For")).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsProxyClientIp() {
			String ip = "192.168.1.2";
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsWLProxyClientIp() {
			String ip = "192.168.1.3";
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsHttpClientIp() {
			String ip = "192.168.1.4";
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsHttpXForwardedFor() {
			String ip = "192.168.1.5";
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
			when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsRemoteAddr() {
			String ip = "192.168.1.6";
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
			when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
			when(request.getRemoteAddr()).thenReturn(ip);

			String result = IpAddressUtils.getClientIp(request);
			assertEquals(ip, result);
		}

		@Test
		public void getClientIpReturnsNullWhenNoHeadersPresent() {
			when(request.getHeader("X-Forwarded-For")).thenReturn(null);
			when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
			when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
			when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
			when(request.getRemoteAddr()).thenReturn(null);

			String result = IpAddressUtils.getClientIp(request);
			assertNull(result);
		}
	}

