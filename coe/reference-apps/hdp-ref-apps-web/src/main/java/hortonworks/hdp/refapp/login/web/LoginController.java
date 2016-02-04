package hortonworks.hdp.refapp.login.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	private static final Logger LOG = Logger.getLogger(LoginController.class);
	
	@RequestMapping("/login")
	public String renderLoginPage(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("inside renderLoginPage...");
		return "login/login";
	}
	
	
}
