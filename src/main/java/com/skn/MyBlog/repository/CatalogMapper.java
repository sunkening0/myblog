package com.skn.MyBlog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.User;



public interface CatalogMapper{
	
	/**
	 * 根据用户查询
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(@Param("user") User user);
	
	/**
	 * 根据用户查询
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(@Param("user") User user,@Param("name") String name);
	
	
	int delete(Long id);

    int save(Catalog record);

    int insertSelective(Catalog record);

    Catalog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Catalog record);

    int updateByPrimaryKey(Catalog record);
}
