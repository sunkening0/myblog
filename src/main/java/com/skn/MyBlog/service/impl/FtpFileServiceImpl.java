package com.skn.MyBlog.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skn.MyBlog.service.IFtpFileService;

/**
 * ftp 文件操作服务实现类
 * 
 */
@Service
public class FtpFileServiceImpl implements IFtpFileService {

	/** ftp 地址 */
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	
	/** ftp 端口 */
	@Value("${FTP_PORT}")
	private int FTP_PORT;

	/** Ftp 用户名 */
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;

	/** ftp 密码 */
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	
	/** ftp 目录*/
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;

	/** 本地字符集编码 */
	private static final String LOCAL_CHARSET = "GBK";

	/** ftp 服务器字符集编码 */
	private static final String SERVER_CHARSET = "UTF-8";

	/**
	 * ftp 文件上传
	 * @throws Exception 
	 */
	@Override
	public void ftpUpload(File srcFile, String fileName) throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("GBK");
		FileInputStream fis = null;
		String charset = LOCAL_CHARSET;
		try {

			/*ftpClient.connect(FTP_ADDRESS,FTP_PORT);
			ftpClient.login(FTP_USERNAME, FTP_PASSWORD);

			fis = new FileInputStream(srcFile);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(FTP_BASE_PATH);
			ftpClient.setBufferSize(1024);
			ftpClient.enterLocalPassiveMode();
			if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
				// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
				charset = "UTF-8";
			}
			ftpClient.setControlEncoding(charset);
			fileName = new String(fileName.getBytes(charset), SERVER_CHARSET);
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(fileName, fis);*/
			boolean success = false;
			FTPClient ftp = new FTPClient();

	        ftp.setControlEncoding("GBK");
			int reply;            
			ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器           
			ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录            
			reply = ftp.getReplyCode();            
			if (!FTPReply.isPositiveCompletion(reply)) {                
				ftp.disconnect();                
				return;            
			}            
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);            
			ftp.makeDirectory(FTP_BASE_PATH);            
			ftp.changeWorkingDirectory(FTP_BASE_PATH);     
			fis = new FileInputStream(srcFile);
			ftp.storeFile(fileName, fis);            
			fis.close();            
			ftp.logout();            
			success = true;
		} catch (IOException e) {
			throw new Exception("FTP客户端出错！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new Exception("关闭FTP连接发生异常！", e);
			}
		}
	}
}