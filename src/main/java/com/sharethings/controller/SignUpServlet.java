package com.sharethings.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
public class SignUpServlet {
	@RequestMapping(value = "/SignUp")
	public ModelAndView SignUp(HttpServletRequest req, HttpServletResponse res) {
		synchronized (this) {
			System.out.println("SignUp servlet is hit!");
			String UserName = req.getParameter("UserName");
			String MobileNumber = req.getParameter("MobileNumber");
			String UserType = req.getParameter("UserType");
			String Password = req.getParameter("Password");

			Resource resource = new ClassPathResource("ApplicationContext.xml");
			BeanFactory factory = new XmlBeanFactory(resource);

			UserDAO userdao = (UserDAO) factory.getBean("UserDao");
			User user = (User) factory.getBean("user");

			user.setMobile(MobileNumber);
			user.setPassword(Password);
			user.setUserName(UserName);
			user.setUserType(UserType);

			HashMap<String, String> result = userdao.AddUser(user);
			String flag = null;
			String message = null;

			for (Map.Entry map : result.entrySet()) {
				if (map.getKey().equals("flag")) {
					flag = (String) map.getValue();
				} else if (map.getKey().equals("message")) {
					message = (String) map.getValue();
				}
			}
			// success case
			if (flag.equals("true")) {
				System.out.println(message);
				return new ModelAndView("Login", "response", message);
			}
			// failure case
			else {
				System.out.println(message);
				return new ModelAndView("SignUp", "response", message);
			}

		}
	}

}
