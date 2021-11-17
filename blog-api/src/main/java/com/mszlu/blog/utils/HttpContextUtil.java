package com.mszlu.blog.utils;
 
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
 
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
 
/**
 * @author MrBird
 */
public class HttpContextUtil {
 
	private HttpContextUtil(){
 
	}
        //获取HttpServletRequest请求
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}
}