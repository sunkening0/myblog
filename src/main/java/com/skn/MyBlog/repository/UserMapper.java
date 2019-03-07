package com.skn.MyBlog.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.skn.MyBlog.domain.User;

public interface UserMapper {


    int deleteByPrimaryKey(@Param("id") Long id);

    int insert(@Param("user") User user);

   // int insertSelective(User record);

    User selectById(@Param("user") User user);
    User selectByName(@Param("user") User user);
    
    List<User> findByUsernames(@Param("list") List<String> list);
    
    //int updateByPrimaryKeySelective(User record);

//    int updateByPrimaryKey(User record);
}