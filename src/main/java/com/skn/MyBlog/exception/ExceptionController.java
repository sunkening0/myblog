package com.skn.MyBlog.exception;


import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {  

    /*//用来手动记录日志  
    private static final Logger log = LoggerFactory.getLogger(SampleController.class);   

    //触发除0异常  
    @GetMapping("/test1")  
    public void test1() {  
        int i=1/0;    
    }  */

    /*//触发空指针异常  
    @GetMapping("/test2")  
    public void test2()  {  
        String a=null;  
        a.length();   
    }  

    //捕获除0异常  
    @ExceptionHandler(ArithmeticException.class)  
    public void exception1(ArithmeticException e){  
        System.out.println("处理除0异常");  
        //继续抛出异常，才能被logback的error级别日志捕获  
        throw e;  
    }  

    //捕获空指针异常  
    @ExceptionHandler(NullPointerException.class)  
    public String exception2(NullPointerException e){  
        System.out.println("处理空指针异常");  
        //手动将异常写入logback的error级别日志  
        log.error("空指针异常",e);  
        return "null";
    }  */


/*
    *  拦截Exception类的异常
    * @param e
    * @return
    */
   @ExceptionHandler(Exception.class)
   @ResponseBody
   public Map<String,Object> exceptionHandler(Exception e){
       Map<String,Object> result = new HashMap<String,Object>();
       result.put("respCode", "9999");
       result.put("respMsg", e.getMessage());
       //正常开发中，可创建一个统一响应实体，如CommonResp
       return result; 
   }
    
    
}

