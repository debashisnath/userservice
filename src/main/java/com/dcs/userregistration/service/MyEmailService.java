/**
 * 
 */
package com.dcs.userregistration.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.dcs.userregistration.model.Mail;
import com.dcs.userregistration.util.CommonContants;

/**
 * @author Debashis
 *
 */
@Service
public class MyEmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	/**
	 * @param mail
	 * @throws MessagingException
	 */
	public void sendOtpMessage(Mail mail) throws MessagingException {
		String html = null;
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		helper.addAttachment("logo.png", new ClassPathResource("consultancy.png"));
		Context context = new Context();
		context.setVariables(mail.getModel());
		if(mail.getSubject().equalsIgnoreCase(CommonContants.OTP_SUBJECT)) {
			html = springTemplateEngine.process("otp-email-template", context);
		}else if(mail.getSubject().equalsIgnoreCase(CommonContants.FORGOT_PWD)){
			html = springTemplateEngine.process("forgot-pwd-template", context);
		}
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		javaMailSender.send(mimeMessage);
	}
}
