package com.skn.MyBlog.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.skn.MyBlog.domain.Blog;
import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.Comment;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.domain.Vote;
import com.skn.MyBlog.domain.es.EsBlog;
import com.skn.MyBlog.repository.BlogMapper;
import com.skn.MyBlog.repository.CommentMapper;
import com.skn.MyBlog.repository.VoteMapper;
import com.skn.MyBlog.service.BlogService;
import com.skn.MyBlog.service.EsBlogService;

@Service
public class BlogServiceImpl implements BlogService {
	private static Logger logger = Logger.getLogger(BlogServiceImpl.class);
	@Autowired
	private BlogMapper blogMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private VoteMapper voteMapper;
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
	public List<Blog> listBlogsByTitleVote(User user, String title,int pageIndex,int pageSize) {
		// 模糊查询
		title = "%" + title + "%";
		String tags = title;
		PageHelper.startPage(pageIndex+1, pageSize);
		//PageHelper.startPage(1, 2);
		List<Blog> blogs = blogMapper.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, tags,user);
		return blogs;
	}

	@Override
	public List<Blog> listBlogsByTitleVoteAndSort(User user, String title,int pageIndex,int pageSize) {
		// 模糊查询
		
		title = "%" + title + "%";
		PageHelper.startPage(pageIndex+1, pageSize);
		List<Blog> blogs = blogMapper.findByUserAndTitleLike(user, title);
		return blogs;
	}
	
	@Override
	public List<Blog> listBlogsByCatalog(User user, Catalog catalog,int pageIndex,int pageSize) {
		PageHelper.startPage(pageIndex+1, pageSize);
		List<Blog> blogs = blogMapper.findByCatalog(user, catalog);
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
		originalBlog.removeComment(commentId);
		this.saveBlog(originalBlog);
	}
	

	@Override
	public Blog createVote(Long blogId) {
		Blog originalBlog = blogMapper.findOne(blogId);
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Vote vote = new Vote(user);
		vote.setCreateTime(new Date());
		boolean isExist = originalBlog.addVote(vote);
		if (isExist) {
			throw new IllegalArgumentException("该用户已经点过赞了");
		}
		voteMapper.insert(vote);
		originalBlog.addVote(vote);
		//保存blog_vote   
		voteMapper.insertBlogVote(originalBlog.getId(),vote.getId());	
		
		return this.saveBlog(originalBlog);
	}

	@Override
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog = blogMapper.findOne(blogId);
		originalBlog.removeVote(voteId);
		this.saveBlog(originalBlog);
	}
}
