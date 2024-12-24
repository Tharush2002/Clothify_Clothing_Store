package auth;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class Email {
    public static void SendEmail(String toEmail, String OTP) {
        String from = "tharusha1205@gmail.com";
        String password = "zbpk phiy jgxv vvhi";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Session creation
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Verify Your Identity - Clothify");

            String htmlContent = String.format("""
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 20px;
                        }
                        .email-container {
                            background-color: #ffffff;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            max-width: 600px;
                            margin: 0 auto;
                        }
                        .header {
                            font-size: 20px;
                            color: #ac9b5f;
                            margin-bottom: 20px;
                        }
                        .code {
                            font-size: 36px;
                            font-weight: semibold;
                            color: #333333;
                            text-align: center;
                            margin: 20px 0;
                        }
                        .footer {
                            font-size: 14px;
                            color: #777777;
                            margin-top: 20px;
                        }
                        .heading{
                            border-radius: 8px;
                            font-size: 25px;
                            background-color: #5c522f;
                            padding: 10px;
                            colour: #f4da7d;
                            margin-bottom: 30px;
                            margin-right: auto;
                            margin-left: auto;
                            box-sizing: border-box;
                            width: 100%%;
                            text-align: center;
                            font-weight: bold;
                            overflow: hidden;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="heading">Clothify Store</div>
                        <div class="header">Enter the 4-digit code below to verify your identity and reset your password.</div>
                        <div class="code">%s</div>
                        <div class="footer">Please do not share this code with anyone.</div>
                    </div>
                </body>
                </html>
                """, OTP);

            message.setContent(htmlContent, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
