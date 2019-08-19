package com.skn.MyBlog.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.tools.classfile.Opcode.Set;

@RestController
@RequestMapping("/redis")
public class RedisController {
	@Autowired
	private RedisTemplate redisTemplate;
	
	/*public Map<String, Object> testZset(){
		Set<typed>
	}*/
}
