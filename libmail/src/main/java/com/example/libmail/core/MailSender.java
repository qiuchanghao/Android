package com.example.libmail.core;

import com.example.libmail.bean.MailDetailsInfo;
import com.example.libmail.util.LogUtils;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件发送器
 */
public class MailSender {
    /**
     * 以文本格式发送邮件
     *
     * @param mailDetailsInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(final MailDetailsInfo mailDetailsInfo) {

        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;
        Properties pro = mailDetailsInfo.getProperties();
        pro.put("mail.smtp.host", "smtp.qq.com");
        pro.put("mail.smtp.auth", "true");
        // pro.put("mail.smtp.socketFactory.class", SSL_FACTORY); // 使用JSSE的SSL
        // socketfactory来取代默认的socketfactory
        pro.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
        pro.put("mail.smtp.port", 587);
        pro.put("mail.smtp.socketFactory.port", 587);
        if (mailDetailsInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MailAuthenticator(mailDetailsInfo.getUserName(), mailDetailsInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

//		Session sendMailSession = Session.getInstance(pro, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(mailDetailsInfo.getUserName(),mailDetailsInfo.getPassword());
//			}
//		});

        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailDetailsInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailDetailsInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailDetailsInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            // 设置邮件消息的主要内容
            String mailContent = mailDetailsInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailDetailsInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailDetailsInfo mailDetailsInfo) {
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;
        Properties pro = mailDetailsInfo.getProperties();
        pro.put("mail.smtp.host", "smtp.qq.com");
        pro.put("mail.smtp.auth", "true");
       // pro.put("mail.smtp.socketFactory.class", SSL_FACTORY); // 使用JSSE的SSL
        // socketfactory来取代默认的socketfactory
        pro.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
        pro.put("mail.smtp.port", 587);
        pro.put("mail.smtp.socketFactory.port", 587);
        // 如果需要身份认证，则创建一个密码验证器
        if (mailDetailsInfo.isValidate()) {
            authenticator = new MailAuthenticator(mailDetailsInfo.getUserName(), mailDetailsInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailDetailsInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailDetailsInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailDetailsInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailDetailsInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 发送带附件的邮件
     *
     * @param info
     * @return
     */
    public boolean sendFileMail(MailDetailsInfo info, File file) {
        Message attachmentMail = createAttachmentMail(info, file);
        try {
            Transport.send(attachmentMail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 创建带有附件的邮件
     *
     * @return
     */
    private Message createAttachmentMail(final MailDetailsInfo info, File file) {
        //创建邮件
        MimeMessage message = null;
        Properties pro = info.getProperties();
        pro.put("mail.smtp.host", "smtp.qq.com");
        pro.put("mail.smtp.auth", "true");
        // pro.put("mail.smtp.socketFactory.class", SSL_FACTORY); // 使用JSSE的SSL
        // socketfactory来取代默认的socketfactory
        pro.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
        pro.put("mail.smtp.port", 587);
        pro.put("mail.smtp.socketFactory.port", 587);
        try {

            Session sendMailSession = Session.getInstance(pro, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(info.getUserName(), info.getPassword());
                }
            });

            message = new MimeMessage(sendMailSession);
            // 设置邮件的基本信息
            //创建邮件发送者地址
            Address from = new InternetAddress(info.getFromAddress());
            //设置邮件消息的发送者
            message.setFrom(from);
            //创建邮件的接受者地址，并设置到邮件消息中
            Address to = new InternetAddress(info.getToAddress());
            //设置邮件消息的接受者, Message.RecipientType.TO属性表示接收者的类型为TO
            message.setRecipient(Message.RecipientType.TO, to);
            //邮件标题
            message.setSubject(info.getSubject());

            // 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用CharSet=UTF-8指明字符编码
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(info.getContent(), "text/html;charset=UTF-8");

            // 创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(text);
            // 创建邮件附件
            MimeBodyPart attach = new MimeBodyPart();

            FileDataSource ds = new FileDataSource(file);
            DataHandler dh = new DataHandler(ds);
            attach.setDataHandler(dh);
            attach.setFileName(MimeUtility.encodeText(dh.getName()));
            mp.addBodyPart(attach);
            mp.setSubType("mixed");
            message.setContent(mp);
            message.saveChanges();

        } catch (Exception e) {
            LogUtils.i("创建带附件的邮件失败");
            e.printStackTrace();
        }
        // 返回生成的邮件
        return message;
    }

}
