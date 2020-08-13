package net.fkm.mailtest.config;

import android.os.Environment;

import java.io.File;

public class AppConstants {
    //邮箱授权码
    public final static String FROM_AUTH_CODE = "taxkaazasopldceb";

    /**
     * 发送人邮件host
     */
    public final static String FROM_MAIL_HOST = "smtp.qq.com";

    /**
     * 发送人邮件address
     */
    public final static String FROM_MAIL_ADDRESS = "2806289681@qq.com";

    /**
     * 发送人邮件host
     */
    public final static String FROM_MAIL_PORT = "587";

    /**
     * pdf 路径
     */
    public final static String PDF_ABS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shumei.txt";

}
