package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Autowired //이메일 전송과 관련된 인터페이스
	//단순 이메일 발송, 첨부파일 포함한 이메일 발송 서비스
	private JavaMailSender mailSender;
	
	public void sendTextEmail(String to, String subject, String content) {
		
		//SimpleMailMessage 스프링에서 제공하는 이메일 메세지 클래스
		//일반텍스트 이메일만 지원(첨부파일, html불가)
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		mailSender.send(message);
	}

    public void sendWelcomeEmail(String userEmail, String nickname) {
        String subject = " " + nickname + "님, 환영합니다!";
        String htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #4CAF50;">환영합니다! </h2>
                <p>안녕하세요, <strong>%s</strong>님!</p>
                <p>회원가입을 축하드립니다. 저희 서비스와 함께 즐거운 시간을 보내세요!</p>
                <div style="background-color: #f5f5f5; padding: 15px; margin: 20px 0;">
                    <p><strong>다음 단계:</strong></p>
                    <ul>
                        <li>프로필을 완성해보세요</li>
                        <li>첫 번째 기능을 사용해보세요</li>
                        <li>문의사항이 있으면 언제든 연락주세요</li>
                    </ul>
                </div>
                <p>감사합니다! </p>
            </div>
            """.formatted(nickname);
        
        sendHtmlEmail(userEmail, subject, htmlContent);
    }

	private void sendHtmlEmail(String userEmail, String subject, String htmlContent) {
		
		//Mime 형식 - 인터넷에서 데이터 전송하기위해 사용되는 표준형식
		//-> 텍스트 외의 데이터를 전송하기 위한 방식으로 개발
		//-> 공학적으로 파일의 컨텐츠를 설명하기위해 사용하는 객체
		//-> 파일의 유형과 데이터의 인코딩 방식 정의
		//-> 클라이언트와 서버가 파일의 내용을 올바르게 처리할수 있음
		
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); 
            
            mailSender.send(message);
            
		} catch (MessagingException e) {
			throw new RuntimeException("이메일 전송 실패", e);
		}
	
	}
}
