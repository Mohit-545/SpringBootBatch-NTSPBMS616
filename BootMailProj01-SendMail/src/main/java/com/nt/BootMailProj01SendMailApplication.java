//BootMailProj01SendMailApplication.java
package com.nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.nt.service.IPurchaseOrder;
import com.nt.service.PurchaseOrderServiceImpl;

@SpringBootApplication
public class BootMailProj01SendMailApplication {

	public static void main(String[] args) {
	//get the IOC Container
	ApplicationContext ctx=SpringApplication.run(BootMailProj01SendMailApplication.class, args);
	//get the Service class object ref
	IPurchaseOrder order = ctx.getBean("purchaseService", IPurchaseOrder.class);
	//invoke the method
	try {
		String msg = order.purchase(new String[] {"t-shirt","jeans","sneakers"}, new double[] {1275.0,3899.0,5685.0}
																												,new String[] {"ronak.12@gmail.com","hk.nit@gmail.com"});
		System.out.println(msg);
	}//try
	catch(Exception e) {
		e.printStackTrace();
	}//catch
	
	//close the IOC Container
	((ConfigurableApplicationContext) ctx).close();
	
	}//main

}//class
