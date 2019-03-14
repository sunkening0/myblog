package com.skn.MyBlog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skn.MyBlog.domain.Authority;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.service.AuthorityService;
import com.skn.MyBlog.service.UserService;

/**
 * 主页控制器.
 * @author skn
 *
 */
@Controller
public class MainController {
	
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService  authorityService;

	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String index() {
		return "redirect:/blogs";
	}

	/**
	 * 获取登录界面
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	/**
	 * 注册用户
	 * @param user
	 * @param result
	 * @param redirect
	 * @return
	 */
	@PostMapping("/register")
	public String registerUser(User user) {
		List<Authority> authorities = new ArrayList<>();
		Authority authority = authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID);
		authorities.add(authority);
		user.setAuthority(authority);
		user.setAuthorities(authorities);
		user.setEncodePassword(user.getPassword());
		userService.saveUser(user);
		return "redirect:/login";
	}
	
	@GetMapping("/search")
	public String search() {
		return "search";
	}
}
