//PurchaseOrderServiceImpl.java
package com.nt.service;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service("purchaseService")
public class PurchaseOrderServiceImpl implements IPurchaseOrder {
	@Autowired
	private JavaMailSender sender;
	@Value("${spring.mail.username}")
	private String fromEmail;

	@Override
	public String purchase(String[] items, double[] prices, String[] emails) throws Exception {
		// calculate billAmount
		double billAmt = 0.0;
		for(double p:prices) {
			billAmt+=p;
		}//for
		String msg = "Total Bill for items :: "+Arrays.toString(items)+" with respective prices :: "+Arrays.toString(prices)+" are :: "+billAmt;
		//send mail
		String status = sendMail(msg, emails);
		return msg+"------------>"+status;
	}//method

	
	public String sendMail(String msg, String[] toEmails) throws Exception{
		MimeMessage message = sender.createMimeMessage();	// it create empty mail message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);	//set data to empty mail message
		helper.setFrom(fromEmail);
		helper.setCc(toEmails);
		helper.setSubject("Order Confirmation with attached invoice");
		helper.setSentDate(new Date());
		helper.setText(msg);
		helper.addAttachment("nit.png", new ClassPathResource("nit.png"));
		sender.send(message);
		return "mail sent";
	}//method
	
}//class
