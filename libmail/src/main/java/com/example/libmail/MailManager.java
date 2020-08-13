package com.example.libmail;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.libmail.bean.MailDetailsInfo;
import com.example.libmail.bean.MailRouteInfo;
import com.example.libmail.core.MailSender;
import com.example.libmail.util.PermissionsUtils;

import java.io.File;

/**
 * 邮件管理类
 */
public class MailManager {

    private static MailManager instance = new MailManager();
    private MailRouteInfo mailRouteInfo;

    private MailManager() {
    }

    public static MailManager getInstance() {
        return instance;
    }

    /**
     * 邮件
     *
     * @param activity
     * @param mailRouteInfo
     */
    public void setMailRoute(Activity activity, MailRouteInfo mailRouteInfo) {
        this.mailRouteInfo = mailRouteInfo;
        PermissionsUtils.requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    public void sendFile(String subjectStr, String contentStr, String toAdd, final File file) {
        final MailDetailsInfo mailDetailsInfo = createMail(subjectStr, contentStr, toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailDetailsInfo, file);
            }
        }).start();
    }

    public void sendText(String subjectStr, String contentStr, String toAdd) {
        final MailDetailsInfo mailDetailsInfo = createMail(subjectStr, contentStr, toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailDetailsInfo);
            }
        }).start();
    }

    @NonNull
    private MailDetailsInfo createMail(String subjectStr, String contentStr, String toAdd) {
        if (mailRouteInfo == null) throw new IllegalStateException("未设置发件人邮箱或者收件人邮箱");
        final MailDetailsInfo mailDetailsInfo = new MailDetailsInfo();
        mailDetailsInfo.setMailServerHost(mailRouteInfo.getMailServerPort());//发送方邮箱服务器
        mailDetailsInfo.setMailServerPort(mailRouteInfo.getMailServerPort());//发送方邮箱端口号
        mailDetailsInfo.setValidate(true);
        mailDetailsInfo.setUserName(mailRouteInfo.getUserName()); // 发送者邮箱地址
        mailDetailsInfo.setPassword(mailRouteInfo.getPassword());// 发送者邮箱授权码
        mailDetailsInfo.setFromAddress(mailRouteInfo.getFromAddress()); // 发送者邮箱
        mailDetailsInfo.setToAddress(toAdd); // 接收者邮箱
        mailDetailsInfo.setSubject(subjectStr); // 邮件主题
        mailDetailsInfo.setContent(contentStr); // 邮件文本
        return mailDetailsInfo;
    }
}
