package com.skn.MyBlog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skn.MyBlog.domain.Catalog;
import com.skn.MyBlog.domain.User;
import com.skn.MyBlog.repository.CatalogMapper;
import com.skn.MyBlog.service.CatalogService;


@Service
public class CatalogServiceImpl implements CatalogService{

	@Autowired
	private CatalogMapper catalogMapper;
	
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		// 判断重复
		List<Catalog> list = catalogMapper.findByUserAndName(catalog.getUser(), catalog.getName());
		if(list !=null && list.size() > 0) {
			throw new IllegalArgumentException("该分类已经存在了");
		}
		int x = catalogMapper.save(catalog);
		if(x > 0){
			return catalog;			
		}else{
			return null;
		}
	}

	@Override
	public void removeCatalog(Long id) {
		catalogMapper.delete(id);
	}

	@Override
	public Catalog getCatalogById(Long id) {
		return catalogMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Catalog> listCatalogs(User user) {
		return catalogMapper.findByUser(user);
	}

}
