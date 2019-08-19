package com.skn.MyBlog.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mail")
public class MailController {
	@Autowired
	private JavaMailSender javaMailSender;

	@RequestMapping("/sim")	
	@ResponseBody
	public String sendMail() {		
		SimpleMailMessage message = new SimpleMailMessage();		
		//发件人		
		message.setFrom("2911698579@qq.com");		
		//收件人		
		message.setTo("812610493@qq.com");		
		message.setSubject("邮件标题");		
		message.setText("邮件内容");		
		javaMailSender.send(message);		
		return "发送成功";	
	}
	
	
	/**
	 * 附件邮件
	 * @return
	 * @throws MessagingException
	 */
	@RequestMapping("/fj")	
	@ResponseBody
	public String sendFjMail() throws MessagingException {		
		//需要创建一个MimeMessageHelper对象，相关参数和简单邮件类似		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);		
		helper.setFrom("2911698579@qq.com");		
		helper.setTo("812610493@qq.com");		
		helper.setSubject("附件-----");		
		//将邮件内容设置为html格式		
		helper.setText("<html><body><h1>hello world</h1></body></html>",true);		
		//定义文件，这是java.main.resources也就是classpach路径下的文件abc.png		
		ClassPathResource file = new ClassPathResource("/log_info.log");		
		//添加附件文件， 设置文件名为abc.png		
		helper.addAttachment("log_info.log", file);		
		javaMailSender.send(mimeMessage);		
		return "发送成功";	
	}
	
	

	public static void main(String[] args) {

		int[] arr = {1,3,2,2};

		int k = 2;

		int sum = 4;

		System.out.println(solution(arr, 0, k, sum));

		

	}

	static int solution(int[] arr,int begin, int k , int sum){

		if(begin > arr.length)

			return 0;		

		if(k==0 && sum != 0)

			return 0;		

		if(k == 0 && sum == 0)

			return 1;		

		if(begin >= arr.length)

			return 0;	

		return solution(arr, begin+1, k-1,sum-arr[begin])+solution(arr, begin+1, k, sum);//第i个选，或者第i个不选，两者相加		

	}


		
}
