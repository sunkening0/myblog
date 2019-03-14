package com.skn.MyBlog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.User;

public interface UserMapper {


    int deleteByPrimaryKey(@Param("id") Long id);
    int deleteUserAuthority(@Param("id") Long id);
    

    int insert(User user);
    
    int insertUserAuthority(@Param("userId") Long userId,@Param("authorityId") Long authorityId);

   // int insertSelective(User record);

    User selectById(@Param("user") User user);
    User selectByName(@Param("user") User user);
    
    List<User> findByUsernames(@Param("list") List<String> list);
    List<User> findAll();
    
    List<User> findByNameLike(String username);
    //int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User user);
}