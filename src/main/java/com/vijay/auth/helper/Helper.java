package com.vijay.auth.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.vijay.auth.response.PageableResponse;

import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Helper {

	@Autowired
	private JavaMailSender mailSender;

	public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
		List<U> entity = page.getContent();
		List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type))
				.collect(Collectors.toList());

		PageableResponse<V> response = new PageableResponse<>();
		response.setContent(dtoList);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());
		return response;
	}

	

	public boolean sendEmail(String to, String subject, String body) {
		boolean isSent = false;
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(mimeMessage);
			isSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSent;

	}
	
	public static String generatePwd() {
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";

		// combine all strings
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

		// create random string builder
		StringBuilder sb = new StringBuilder();

		// create an object of Random class
		Random random = new Random();

		// specify length of random string
		int length = 6;

		for (int i = 0; i < length; i++) {

			// generate random index number
			int index = random.nextInt(alphaNumeric.length());

			// get character specified by index
			// from the string
			char randomChar = alphaNumeric.charAt(index);

			// append the character to string builder
			sb.append(randomChar);
		}

		return sb.toString();
	}
	
	
	
	
//	public static String generateRandomPwd() {
//		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//		// String pwd = RandomStringUtils.random( 6, characters );
//		// System.out.println( pwd );
//		return null;
//	}
}
