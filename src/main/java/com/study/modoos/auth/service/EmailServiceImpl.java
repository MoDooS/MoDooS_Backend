package com.study.modoos.auth.service;

import java.util.Random;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    JavaMailSender emailSender;

    public static final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception {
        System.out.println("보내는 대상: " + to);
        System.out.println("인증 번호: " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // 보내는 대상
        message.setSubject("이메일 인증 테스트"); // 제목

        String verificationCode = ePw;

        String msgg = "<div style='margin: 20px; font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>"
                + "<h1 style='color: #3f51b5; margin-bottom: 20px;'>MoS 모두의 스터디</h1>"
                + "<div style='margin-top: 20px;'>"
                + "<p style='font-size: 16px; margin-bottom: 10px;'>아래 코드를 복사하여 입력해주세요:</p>"
                + "<p style='font-size: 24px; font-weight: bold; color: #f44336; margin-bottom: 20px;'>"
                + verificationCode + "</p>"
                + "<p style='font-size: 16px;'>감사합니다.</p>"
                + "</div>"
                + "<div style='border: 1px solid #e0e0e0; padding: 10px; margin-top: 20px;'>"
                + "<h3 style='color: #3f51b5; font-size: 18px; margin-bottom: 10px;'>회원가입 인증 코드입니다.</h3>"
                + "<p style='font-size: 16px; margin-bottom: 10px;'>위의 코드를 사용하여 회원가입을 완료해주세요.</p>"
                + "</div>"
                + "</div>";


        message.setText(msgg, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress("modustudy00@gmail.com", "MoS 모두의 스터디")); // 보내는 사람

        return message;
    }


    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
    @Override
    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
}