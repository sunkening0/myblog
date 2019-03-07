package com.skn.MyBlog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.skn.MyBlog.domain.Blog;
import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.Comment;
import com.skn.MyBlog.domain.User;


/**
 * Blog 仓库.
 *
 * @since 1.0.0 2017年4月7日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface BlogMapper {
	int save(Blog blog);
	int delete(Long id);
	Blog findOne(Long id);
	int update(Blog blog);
	//int addComment(Comment comment);
	
	/**
	 * 根据用户名分页查询用户列表（最新）
	 * 由 findByUserAndTitleLikeOrTagsLikeOrderByCreateTimeDesc 代替，可以根据标签进行查询
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 * @see findByTitleLikeOrTagsLikeAndUserOrderByCreateTimeDesc
	 */
	Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);
	
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);
	
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,User user,String tags,User user2,Pageable pageable);
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
