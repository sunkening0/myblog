package com.skn.MyBlog.vo;

import java.io.Serializable;

public class PageVo implements Serializable{
	 
		private static final long serialVersionUID = 1L;

		private Integer totalPages;
		private Long totalElements;
		private Integer number;
		private Integer size;
		private Integer first;
		private Integer last;
		
		
		
		public Integer getFirst() {
			return first;
		}
		public void setFirst(Integer first) {
			this.first = first;
		}
		public Integer getLast() {
			return last;
		}
		public void setLast(Integer last) {
			this.last = last;
		}
		public Integer getTotalPages() {
			return totalPages;
		}
		public void setTotalPages(Integer totalPages) {
			this.totalPages = totalPages;
		}
		public Long getTotalElements() {
			return totalElements;
		}
		public void setTotalElements(Long totalElements) {
			this.totalElements = totalElements;
		}
		public Integer getNumber() {
			return number;
		}
		public void setNumber(Integer number) {
			this.number = number;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		
		
	}