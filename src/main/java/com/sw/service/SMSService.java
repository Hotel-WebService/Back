package com.sw.service;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
public class SMSService {

    private final DefaultMessageService messageService;

    public SMSService() {
        this.messageService = NurigoApp.INSTANCE.initialize(
                "NCSX8QGIJRP85HQC", 
                "HNN2LTG4GUXJTSHRV6QQX7SY9PAVECEL",
                "https://api.coolsms.co.kr"
        );
    }

    public String sendVerificationCode(String to) {
        String code = String.format("%06d", (int)(Math.random() * 1000000));

        Message message = new Message();
        message.setFrom("01031874593");
        message.setTo(to);
        message.setText("[Stay Manager] 인증번호는 [" + code + "]입니다.");

        try {
            SingleMessageSentResponse response = this.messageService.sendOne(new net.nurigo.sdk.message.request.SingleMessageSendingRequest(message));
            System.out.println("SMS 전송 결과: " + response);
        } catch (Exception e) {
            System.err.println("SMS 전송 실패: " + e.getMessage());
        }

        return code;
    }
}