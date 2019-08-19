package com.skn.MyBlog.service;

import java.io.File;

public interface IFtpFileService {

	void ftpUpload(File srcFile, String fileName) throws Exception ;

}
