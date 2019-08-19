package com.skn.MyBlog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.Blog;
import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.User;



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
	List<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title);
	
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	List<Blog> findByUserAndTitleLike(@Param("user") User user,@Param("title") String title);
	
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	List<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(@Param("title") String title,@Param("tags") String tags,@Param("user") User user2);
	/**
	 * 根据用户名分页查询用户列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	List<Blog> findByCatalog(@Param("user") User user,@Param("catalog") Catalog catalog);
}
