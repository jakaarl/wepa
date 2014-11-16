package wepa.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BaseInterceptor extends HandlerInterceptorAdapter
{
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		modelAndView.addObject("controllerName", handlerMethod.getBean()
				.getClass().getSimpleName());
		modelAndView.addObject("controllerAction", handlerMethod.getMethod()
				.getName());
	}
}