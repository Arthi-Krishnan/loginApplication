package com.example.service;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.ServiceProvider;
import com.example.entity.User;
import com.example.repository.UserRepository;
@Service
public class UserService { 
	 
	  @Autowired
	  private UserRepository repo;
	     
	    public String processOAuthPostLogin(String username,HttpServletRequest request) { 
	    	String userType = "1";
	        User existUser = repo.getUserByUsername(username);
	  
	        if (existUser == null) {
	            User newUser = new User();
	            newUser.setUserName(username);
	            newUser.setProvider(ServiceProvider.GOOGLE);
	            newUser.setEnabled(true);     
	            //newUser.s
	            repo.save(newUser);
	            
	            userType = "0";
	        }
	       
	        
	     return userType;    
	    }
	     
	}
