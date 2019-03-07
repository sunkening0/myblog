package com.skn.MyBlog.service.impl;


import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skn.MyBlog.domain.Blog;
import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.Comment;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.domain.es.EsBlog;
import com.skn.MyBlog.repository.BlogMapper;
import com.skn.MyBlog.repository.CommentMapper;
import com.skn.MyBlog.service.BlogService;
import com.skn.MyBlog.service.EsBlogService;

/**
 * Blog 服务.
 * 
 * @since 1.0.0 2017年4月7日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
@Service
public class BlogServiceImpl implements BlogService {
	private static Logger logger = Logger.getLogger(BlogServiceImpl.class);
	@Autowired
	private BlogMapper blogMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private EsBlogService esBlogService;
 
	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.BlogService#saveBlog(com.waylau.spring.boot.blog.domain.Blog)
	 */
	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		boolean isNew = (blog.getId() == null);
		EsBlog esBlog = null;
		
		//logger.info("fghfgh=-fghfgh=-gbghfgh=-=-=-----"+JSONUtils.toJSONString(blog));
		if (isNew) {
			blogMapper.save(blog);
			//esBlog = new EsBlog(returnBlog);
			esBlog = new EsBlog(blog);
		} else {
			//更新主表  
			int i = blogMapper.update(blog);
			esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
			//esBlog.update(returnBlog);
			esBlog.update(blog);
		}
		
		esBlogService.updateEsBlog(esBlog);
		return blog;
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.BlogService#removeBlog(java.lang.Long)
	 */
	@Transactional
	@Override
	public void removeBlog(Long id) {
		blogMapper.delete(id);
		EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
		esBlogService.removeEsBlog(esblog.getId());
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.blog.service.BlogService#getBlogById(java.lang.Long)
	 */
	@Override
	public Blog getBlogById(Long id) {
		return blogMapper.findOne(id);
	}

	@Override
	public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		//Page<Blog> blogs = blogMapper.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
		String tags = title;
		Page<Blog> blogs = blogMapper.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user, tags,user, pageable);
		return blogs;
	}

	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		Page<Blog> blogs = blogMapper.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}
	
	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		Page<Blog> blogs = blogMapper.findByCatalog(catalog, pageable);
		return blogs;
	}

	@Override
	public void readingIncrease(Long id) {
		Blog blog = blogMapper.findOne(id);
		blog.setReadSize(blog.getCommentSize()+1);
		this.saveBlog(blog);
	}
	
	@Override
	public Blog createComment(Long blogId, String commentContent) {
		Blog originalBlog = blogMapper.findOne(blogId);
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Comment comment = new Comment(user, commentContent);
		comment.setCreateTime(new Date());

		//保存comment
		commentMapper.insert(comment);
		originalBlog.addComment(comment);			
		//保存blog_comment   
		commentMapper.insertBlogComment(originalBlog.getId(),comment.getId());			
		//更新es
		return this.saveBlog(originalBlog);
	}

	@Override
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog = blogMapper.findOne(blogId);
		//删除comment
		commentMapper.delete(commentId);
		//删除blog_comment
		commentMapper.deleteBlogComment(commentId);
		this.saveBlog(originalBlog);
	}
	/*

	@Override
	public Blog createVote(Long blogId) {
		Blog originalBlog = blogMapper.findOne(blogId);
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Vote vote = new Vote(user);
		boolean isExist = originalBlog.addVote(vote);
		if (isExist) {
			throw new IllegalArgumentException("该用户已经点过赞了");
		}
		return this.saveBlog(originalBlog);
	}

	@Override
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog = blogMapper.findOne(blogId);
		originalBlog.removeVote(voteId);
		this.saveBlog(originalBlog);
	}*/
}
