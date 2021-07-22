package com.example.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Controller
public class AppController {

	@Autowired
	UserRepository repo;
	@Autowired
	RoleRepository roleRepo;

	@RequestMapping(value = "/userRole")
	public ModelAndView userRolePage(@RequestParam("role") Integer role, HttpServletRequest request) {
		HttpSession session = request.getSession();
		DefaultOidcUser user = (DefaultOidcUser) session.getAttribute("user");
		User user1 = repo.getUserByUsername(user.getEmail());
		Role role1 = roleRepo.getRoleById(role);
		// System.out.println(user1.getUserName());

		System.out.println(role1.getName());
		if (user1 != null) {
			user1.setRole(role1);
			repo.save(user1);
		}
		ModelAndView view = new ModelAndView("index");
		view.addObject("role", role1.getName());
		view.addObject("userName", user.getFullName());
		return view;
	}

	@RequestMapping("/logOut")
	public ModelAndView logoutDo(HttpServletRequest request, HttpServletResponse response) {

		// User user1 = repo.getUserByUsername();
		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		for (Cookie cookie : request.getCookies()) {
			System.out.println(cookie.getName());
			cookie.setMaxAge(0);
			cookie.setValue(null);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return new ModelAndView("redirect:/loginNew");
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView userDetails(@AuthenticationPrincipal OAuth2User user, HttpServletRequest request, Model m) {
		User user1 = repo.getUserByUsername(user.getAttribute("email"));
		ModelAndView view = new ModelAndView("index");
		view.addObject("role", user1.getRole().getName());
		view.addObject("userName", user.getAttribute("name"));

		return view;
	}

	@RequestMapping(value = "/loginNew", method = RequestMethod.GET)
	public ModelAndView seeMess(@AuthenticationPrincipal OAuth2User user, HttpServletRequest request, Model m) {
		System.out.println("Hi User" + user.getName());
		ModelAndView view = new ModelAndView("login");
		view.addObject("role", user.getAttribute("name"));
		view.addObject("userName", user.getAttribute("name"));
		return view;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "success";
	}

	@RequestMapping(value = "/uRolePage", method = RequestMethod.GET)
	public String getRolePage() {

		return "userRole";
	}

	@RequestMapping(value = "/loginForm", method = RequestMethod.POST)
	public String loginForm() {

		return "login";
	}

	@RequestMapping(value = "/403", method = RequestMethod.POST)
	public String restricted() {
		return "403";
	}
}
