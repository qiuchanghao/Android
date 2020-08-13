package net.fkm.mailtest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libmail.MailManager;
import com.example.libmail.bean.MailRouteInfo;
import com.example.libmail.util.FileUtils;
import com.example.libmail.util.PermissionsUtils;

import net.fkm.mailtest.config.AppConstants;

import java.io.File;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MailnActivity extends AppCompatActivity {

    private EditText toAddEt, etSubject, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailn);
        initView();
        initMailManager();
    }

    private void initView() {
        toAddEt = (EditText) findViewById(R.id.toAddEt);
        etSubject = findViewById(R.id.etubject);
        etContent = findViewById(R.id.etContent);
    }

    /**
     * 初始化邮件配置项
     */
    private void initMailManager() {
        MailRouteInfo.Builder builder = new MailRouteInfo.Builder()
                .mailServerHost(AppConstants.FROM_MAIL_HOST)
                .mailServerPort(AppConstants.FROM_MAIL_PORT)
                .fromAddress(AppConstants.FROM_MAIL_ADDRESS)
                .password(AppConstants.FROM_AUTH_CODE)
                .userName(AppConstants.FROM_MAIL_ADDRESS);
        MailManager.getInstance().setMailRoute(this, builder.build());
    }

    public void sendFileMail(View view) {
        File file = FileUtils.getFile(AppConstants.PDF_ABS_PATH);
        FileUtils.writeDataToFile(file);
        String subject = etSubject.getText().toString();
        String content = etContent.getText().toString();
        String toAdd = toAddEt.getText().toString();
        MailManager.getInstance().sendFile(subject, content, toAdd, file);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsUtils.CODE) {
            int index = 0;
            for (; index < permissions.length; index++) {
                int grantResult = grantResults[index];
                if (grantResult != PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限被拒绝", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (index == permissions.length) {
                //权限允许
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
