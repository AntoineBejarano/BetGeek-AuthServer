package io.betgeek.authserver.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RequestCORSFilter implements Filter {

	private List<String> whiteList = Arrays.asList(
			"http://ec2-3-23-9-143.us-east-2.compute.amazonaws.com",
			"https://ec2-3-23-9-143.us-east-2.compute.amazonaws.com",
			"http://localhost:4200",
			"https://localhost:4200",
			"http://app.betgeek.io",
			"https://app.betgeek.io");
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        String domainOrigin = request.getHeader("Origin");
        if (isPermitedDomain(domainOrigin)) {
            response.setHeader("Access-Control-Allow-Origin", domainOrigin);
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, content-type, withCredentials");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
	}
	
	private Boolean isPermitedDomain(String domain) {
		return whiteList.contains(domain);
	}
	
}