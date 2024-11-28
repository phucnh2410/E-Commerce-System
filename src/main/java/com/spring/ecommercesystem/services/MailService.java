package com.spring.ecommercesystem.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendRequestByMail(String toEmail, String sellerName, String categoryName) throws MessagingException, UnsupportedEncodingException {
        String emailTemplate = loadRequestSendingMailTemplate();

        // Replace placeholders with actual values
        String emailContent = emailTemplate
                .replace("{{SELLER_NAME}}", sellerName)
                .replace("{{CATEGORY_NAME}}", categoryName);

        // Prepare the MimeMessage
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("ngohuynhphuc24.10@gmail.com", "Sherlock Store");
        helper.setTo(toEmail);
        helper.setSubject("New Category Proposal");
        helper.setText(emailContent, true);  // true means the content is HTML

        // Send the email
        mailSender.send(mimeMessage);
    }

    @Async
    public void sendOtpByMail(String toEmail, String otpCode) throws MessagingException, UnsupportedEncodingException {
        String emailTemplate = loadHtmlMailTemplate();

        // Replace the {{OTP_CODE}} placeholder with the actual OTP code
        String emailContent = emailTemplate.replace("{{OTP_CODE}}", otpCode);

        // Prepare the MimeMessage
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("ngohuynhphuc24.10@gmail.com", "Sherlock Store");
        helper.setTo(toEmail);
        helper.setSubject("Verify Your Identity");
        helper.setText(emailContent, true);  // true means the content is HTML

        // Send the email
        mailSender.send(mimeMessage);
    }

    private String loadHtmlMailTemplate() {
        return """ 
                <!DOCTYPE html>
                 <html lang="en">
                 <head>
                     <meta charset="UTF-8">
                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
                     <title>Verify Your Identity</title>
                     <style>
                         body {
                             font-family: Arial, sans-serif;
                             margin: 0;
                             padding: 0;
                             background-color: #f4f4f4;
                             display: flex;
                             justify-content: center;
                             align-items: center;
                             height: 100vh;
                         }
                         .email-container {
                             max-width: 600px;
                             margin: 0 auto;
                             background-color: #ffffff;
                             padding: 20px;
                             border-radius: 8px;
                             box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                         }
                         .header {
                             text-align: center;
                             background-color: #415a80;
                             color: #ffffff;
                             padding: 20px 0;
                             font-size: 24px;
                             font-weight: bold;
                             border-radius: 8px 8px 0 0;
                         }
                         .content {
                             padding: 20px;
                             line-height: 1.6;
                             color: #333333;
                         }
                         .otp-code {
                             font-size: 32px;
                             font-weight: bold;
                             text-align: center;
                             margin: 20px 0;
                         }
                         .footer {
                             text-align: center;
                             color: #888888;
                             font-size: 12px;
                             padding-top: 10px;
                             border-top: 1px solid #dddddd;
                             margin-top: 20px;
                         }
                         a {
                             color: #415a80;
                             text-decoration: none;
                         }
                     </style>
                 </head>
                 <body>
                     <div class="email-container">
                         <div class="header">
                             Verify Your Identity
                         </div>
                         <div class="content">
                             <p>Hello,</p>
                             <p>We identified unusual activity in a recent sign in attempt from your account. If you initiated the request to sign in, please enter the following code to verify your identity and complete your sign in.</p>
                             <div class="otp-code">{{OTP_CODE}}</div>
                             <p>(This code will expire 5 minutes after it was sent.)</p>
                             <!-- <p>If you did not initiate the request, please <a href="https://your-website.com/reset-password">reset your password</a> immediately.</p> -->
                             <p>If you have any questions or need assistance, please contact <a href="https://your-website.com/support">Support</a>.</p>
                         </div>
                         <div class="footer">
                             <p>Thank you for using our services.</p>
                             <p >&copy; 2024 - Ngo Huynh Phuc. All rights reserved. The certification names are the trademarks of their respective owners.</p>
                         </div>
                     </div>
                 </body>
                 </html>
               """;
    }


    private String loadRequestSendingMailTemplate() {
        return """ 
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>New Category Proposal</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f4f4f4;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #ffffff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        text-align: center;
                        background-color: #415a80;
                        color: #ffffff;
                        padding: 20px 0;
                        font-size: 24px;
                        font-weight: bold;
                        border-radius: 8px 8px 0 0;
                    }
                    .content {
                        padding: 20px;
                        line-height: 1.6;
                        color: #333333;
                    }
                    .proposal-details {
                        font-size: 20px;
                        font-weight: bold;
                        text-align: center;
                        margin: 20px 0;
                    }
                    .footer {
                        text-align: center;
                        color: #888888;
                        font-size: 12px;
                        padding-top: 10px;
                        border-top: 1px solid #dddddd;
                        margin-top: 20px;
                    }
                    a {
                        color: #415a80;
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        New Category Proposal
                    </div>
                    <div class="content">
                        <p>Hello Admin,</p>
                        <p>A seller has proposed a new category to be added to the system. Below are the details of the proposal:</p>
                        <div class="proposal-details">
                            Seller Name: <strong>{{SELLER_NAME}}</strong><br>
                            Proposed Category: <strong>{{CATEGORY_NAME}}</strong>
                        </div>
                        <p>Please review this proposal and take appropriate action.</p>
                        <p>If you have any questions or need additional information, please contact <a href="https://your-website.com/support">Support</a>.</p>
                    </div>
                    <div class="footer">
                        <p>Thank you for using our services.</p>
                        <p>&copy; 2024 - Ngo Huynh Phuc. All rights reserved. The certification names are the trademarks of their respective owners.</p>
                    </div>
                </div>
            </body>
            </html>
           """;
    }



}
