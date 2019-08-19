package com.skn.MyBlog.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.github.pagehelper.PageInfo;
import com.skn.MyBlog.domain.Blog;
import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.domain.Vote;
import com.skn.MyBlog.service.BlogService;
import com.skn.MyBlog.service.CatalogService;
import com.skn.MyBlog.service.CommentService;
import com.skn.MyBlog.service.IFtpFileService;
import com.skn.MyBlog.service.UserService;
import com.skn.MyBlog.service.VoteService;
import com.skn.MyBlog.util.ConstraintViolationExceptionHandler;
import com.skn.MyBlog.vo.PageVo;
import com.skn.MyBlog.vo.Response;


/**
 * 用户主页空间控制器.
 * @author skn
 *
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
	private static Logger logger = Logger.getLogger(UserspaceController.class);
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private IFtpFileService ftpFileService;
	
	
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return "redirect:/u/" + username + "/blogs";
	}
 
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/profile", "userModel", model);
	}
 
	/**
	 * 保存个人设置
	 * @param user
	 * @param result
	 * @param redirect
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public String saveProfile(@PathVariable("username") String username,User user) {
		User originalUser = userService.getUserById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setName(user.getName());
		
		// 判断密码是否做了变更
		String rawPassword = originalUser.getPassword();
		PasswordEncoder  encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(user.getPassword());
		boolean isMatch = encoder.matches(rawPassword, encodePasswd);
		if (!isMatch) {
			originalUser.setEncodePassword(user.getPassword());
		}
		
		userService.saveUser(originalUser);
		return "redirect:/u/" + username + "/profile";
	}
	
	/**
	 * 获取编辑头像的界面
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);
	}
	
	
	/**
	 * 保存头像
	 * @param username
	 * @param model
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
		String avatarUrl = user.getAvatar();
		
		User originalUser = userService.getUserById(user.getId());
		originalUser.setAvatar(avatarUrl);
		userService.saveUser(originalUser);
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
	}
	
	/**
	 * 保存头像到FTP
	 * @param username
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/{username}/saveavatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public String saveAvatar1(@PathVariable("username") String username,@RequestParam("file") MultipartFile file) throws Exception {
		/*String avatarUrl = ((User)user).getAvatar();
		
		User originalUser = userService.getUserById(((User)user).getId());
		originalUser.setAvatar(avatarUrl);
		userService.saveUser(originalUser);
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));*/
		System.out.println(file.getName()+"3242342");
		// 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        //String prefix=fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile(UUID.randomUUID().toString(), "ico");
        // MultipartFile to File
        file.transferTo(excelFile);
		ftpFileService.ftpUpload(excelFile,fileName);
		return "";
	}
	
	
	
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="catalog",required=false ) Long catalogId,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
		
		User  user = (User)userDetailsService.loadUserByUsername(username);
 		
		/*Page<Blog> page = null;
		
		if (catalogId != null && catalogId > 0) { // 分类查询
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		} else if (order.equals("hot")) { // 最热查询
			Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize"); 
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
		} else if (order.equals("new")) { // 最新查询
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleVote(user, keyword, pageable);
		}*/
		
		PageInfo<Blog> pageInfo = null;
		List<Blog> list = new ArrayList<>();
		if (catalogId != null && catalogId > 0) { // 分类查询
			Catalog catalog = catalogService.getCatalogById(catalogId);
			//PageHelper.startPage(pageIndex, pageSize);
			list = blogService.listBlogsByCatalog(user, catalog,pageIndex, pageSize);
			order = "";
		} else if (order.equals("hot")) { // 最热查询
			//Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize"); 
			list = blogService.listBlogsByTitleVoteAndSort(user, keyword,pageIndex, pageSize);
		} else if (order.equals("new")) { // 最新查询
			//Page page = PageHelper.startPage(1, 2);
			list = blogService.listBlogsByTitleVote(user, keyword,pageIndex, pageSize);
			//pageInfo = new PageInfo<>(page.getResult());
		}
		
		//totalPages  totalElements   number    size
		pageInfo = new PageInfo<Blog>(list);
		/*pageInfo.getPageSize()
		pageInfo.getTotal()
		pageInfo.get*/
		PageVo page = new PageVo();
		page.setTotalElements(pageInfo.getTotal());
		page.setTotalPages(pageInfo.getPages());
		page.setNumber(pageInfo.getPageNum());
		page.setSize(pageInfo.getPageSize());
		page.setFirst(pageInfo.getLastPage());
		page.setLast(pageInfo.getNextPage());
		//List<Blog> list = page.getContent();	// 当前所在页面数据列表
		
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return (async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
	}
	
	/**
	 * 获取博客展示界面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
		User principal = null;
		Blog blog = blogService.getBlogById(id);
		
		// 每次读取，简单的可以认为阅读量增加1次
		blogService.readingIncrease(id);

		// 判断操作用户是否是博客的所有者
		boolean isBlogOwner = false;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			} 
		}
		
		// 判断操作用户的点赞情况
		List<Vote> votes = blog.getVotes();
		Vote currentVote = null; // 当前用户的点赞情况
		
		if (principal !=null) {
			for (Vote vote : votes) {
				if(vote.getUser().getUsername().equals(principal.getUsername())){
					currentVote = vote;
					break;					
				}
			}
		}

		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel",blog);
		model.addAttribute("currentVote",currentVote);
		
		return "/userspace/blog";
	}
	
	
	/**
	 * 删除博客
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id) {
		
		try {
			//删除所有评论(关联表和主表)
			commentService.removeAllComment(id);
			
			//删除所有点赞(关联表和主表)
			voteService.removeAllVote(id);
			
			//删除博客
			blogService.removeBlog(id);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
	
	/**
	 * 获取新增博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", new Blog());
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 获取编辑博客的界面
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		// 获取用户分类列表
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * 保存博客
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		// 对 Catalog 进行空处理
		if (blog.getCatalog().getId() == null) {
			return ResponseEntity.ok().body(new Response(false,"未选择分类"));
		}
		try {

			// 判断是修改还是新增
			
			if (blog.getId()!=null) {
				Blog orignalBlog = blogService.getBlogById(blog.getId());
				orignalBlog.setTitle(blog.getTitle());
				orignalBlog.setContent(blog.getContent());
				orignalBlog.setSummary(blog.getSummary());
				orignalBlog.setCatalog(blog.getCatalog());
				orignalBlog.setTags(blog.getTags());
				orignalBlog.setCreateTime(new Date());
				//logger.info("-=-=-=-=-=-=-=-==-1231231234435646-=-=-=-=-----"+JSONUtils.toJSONString(orignalBlog));
				blogService.saveBlog(orignalBlog);
	        } else {
	    		User user = (User)userDetailsService.loadUserByUsername(username);
	    		//logger.info("332423=-==-1231231234435646-=-=-=-=-----"+JSONUtils.toJSONString(user));
	    		blog.setUser(user);
	    		blog.setCatalogId(blog.getCatalog().getId());
	    		blog.setUserId(user.getId());
	    		blog.setCreateTime(new Date());
				blogService.saveBlog(blog);
	        }
			
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
}
