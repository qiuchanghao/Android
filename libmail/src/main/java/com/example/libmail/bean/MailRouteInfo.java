package com.example.libmail.bean;

public class MailRouteInfo {
    private String mailServerHost;// 发送邮件的服务器的IP
    private String mailServerPort;// 发送邮件的服务器的端口
    private String fromAddress;// 邮件发送者的地址
    private String toAddress;    // 邮件接收者的地址
    private String userName;// 登陆邮件发送服务器的用户名
    private String password;// 登陆邮件发送服务器的密码

    public String getMailServerHost() {
        return mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public MailRouteInfo(Builder builder) {
        this.mailServerHost = builder.mailServerHost;
        this.mailServerPort = builder.mailServerPort;
        this.fromAddress = builder.fromAddress;
        this.toAddress = builder.toAddress;
        this.userName = builder.userName;
        this.password = builder.password;
    }

   public static class Builder {
        private String mailServerHost;// 发送邮件的服务器的IP
        private String mailServerPort;// 发送邮件的服务器的端口
        private String fromAddress;// 邮件发送者的地址
        private String toAddress;    // 邮件接收者的地址
        private String userName;// 登陆邮件发送服务器的用户名
        private String password;// 登陆邮件发送服务器的密码

        public Builder mailServerHost(String mailServerHost) {
            this.mailServerHost = mailServerHost;
            return this;
        }

        public Builder mailServerPort(String mailServerPort) {
            this.mailServerPort = mailServerPort;
            return this;
        }

        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public MailRouteInfo build() {
            return new MailRouteInfo(this);
        }
    }
}

