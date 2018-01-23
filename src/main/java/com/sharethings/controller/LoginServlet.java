package com.sharethings.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sharethings.model.User;
import com.sharethings.model.UserDAO;

@Controller
public class LoginServlet {
	@RequestMapping(value = "/Login")
	public ModelAndView Login(HttpServletRequest req, HttpServletResponse res) {

		String Mobile = req.getParameter("Mobile");
		String Password = req.getParameter("Password");
		String UserType = req.getParameter("UserType");
		System.out.println(Mobile);
		PrintWriter pw = null;
		boolean MobileNoTest = false;
		String DB_password = null;
		String response_message;

		// get the bean factory to get the user dao object.
		Resource resource = new ClassPathResource("ApplicationContext.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		// get user DAO objec and default user object for the current user login.
		UserDAO userdao = (UserDAO) factory.getBean("UserDao");
		User user = (User) factory.getBean("user");

		// set the Mobile and Password to the user object got from the login page.
		user.setMobile(Mobile);
		user.setPassword(Password);
		user.setUserType(UserType);

		HashMap<String, String> result = new HashMap<String, String>();
		// get the returned hashmap object after authentication done for login.
		result = userdao.AuthenticateUser(user);
		String flag = null;
		String message = null;

		for (Map.Entry map : result.entrySet()) {
			if (map.getKey().equals("flag")) {
				flag = (String) map.getValue();
			} else if (map.getKey().equals("message")) {
				message = (String) map.getValue();
			}
		}

		// perform the corresponding redirection based on the result obtained.
		// success case
		if (flag.contains("true")) {
			String viewName = null;
			if (UserType.equals("Consumer")) {
				viewName = "LoginHome";
			} else {
				viewName = "LoginHome";
			}
			System.out.println("Setting cookie:" + Mobile);
			Cookie c = new Cookie("Mobile", Mobile);
			c.setMaxAge(1800);
			res.addCookie(c);
			response_message = Mobile;
			return new ModelAndView(viewName, "response", response_message);
		}
		// failure cases
		else {

			// Wrong password case
			if (message.contains("Invalid Password!")) {
				return new ModelAndView("Login", "response", message);
			}
			// user not registered case
			else {
				return new ModelAndView("SignUp", "response", message);
			}
		}

	}
}
