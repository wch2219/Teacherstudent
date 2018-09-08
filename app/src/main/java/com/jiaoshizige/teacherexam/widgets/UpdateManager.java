package com.jiaoshizige.teacherexam.widgets;

/**
 * Created by Administrator on 2018/2/22 0022.
 */

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.utils.HttpClientUtil;


/**
 * 检查自动更新
 */

public class UpdateManager {
    private Context mContext;
    private static final int down_step_custom = 3;

    // 提示语
    private String updateMsg1;
    private String updateMsg2;
    private String versionname, newVersionName, appname;
    private HashMap<String, Object> map;
    // 返回的安装包url
    private String apkUrl;
    private Notification notification;
    private RemoteViews contentView;
    private NotificationManager notificationManager;

    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/jiaoshizige.teacherexam/";

    private static final String saveFileName = savePath + "jiaoshizige.teacherexam.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
    // private ProgressBar mProgress;

    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 1;

    private int versioncode, newVersionCode;

    private Thread downLoadThread;

    private boolean isFromMainActivity;

    public UpdateManager(Context mContext, boolean isFromMainActivity) {
        this.mContext = mContext;
        this.isFromMainActivity = isFromMainActivity;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_ERROR:
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    //notification.setLatestEventInfo(UpdateService.this,app_name, getString(R.string.down_fail), pendingIntent);
//                    notification.setLatestEventInfo(mContext,"教师资格证", "下载失败", null);
                    notification = new Notification.Builder(mContext)
                            .setAutoCancel(true)
                            .setContentTitle("教师资格证")
                            .setContentText("下载失败")
                            .setContentIntent(null)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    break;
                case DOWN_OVER:
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    notification.setLatestEventInfo(mContext,"教师资格证", "下载成功", null);
//                    //notification.setLatestEventInfo(UpdateService.this,app_name, app_name + getString(R.string.down_sucess), null);
//                    notificationManager.notify(R.layout.notification_item, notification);
//                    installApk();
                    notification = new Notification.Builder(mContext)
                            .setAutoCancel(true)
                            .setContentTitle("教师资格证")
                            .setContentText("下载成功")
                            .setContentIntent(null)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    //notification.setLatestEventInfo(UpdateService.this,app_name, app_name + getString(R.string.down_sucess), null);
                    notificationManager.notify(R.layout.notification_item, notification);
                    installApk();


                    break;
                default:
                    break;
            }
        }

        ;
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            newVersionCode = (Integer) map.get("newVersionCode");
            newVersionName = map.get("newVersionName").toString();
            apkUrl = map.get("apkUrl").toString();
            appname = map.get("appname").toString();
            updateMsg1 = map.get("updateMsg").toString();
//			System.out.println("updateMsg:"+updateMsg1);
//			updateMsg1 = "更新版本 V2.4\n更新内容：\n1.修改了应用中存在的一些bug.\n2.优化了软件的性能，运行更加流畅.\n3.调整了软件的界面布局，使软件看起来更加美观.\n";
            updateMsg2 = updateMsg1.replace("\\n", "\n");
            Log.e("newVersionCode:", newVersionCode + "*********oldVersionCode" + oldVersionCode());
            if (newVersionCode > oldVersionCode()) {
                showNoticeDialog();
            }
        }
    };

    /**
     * 获取应用版本号
     *
     * @return
     */
    private int oldVersionCode() {
        try {
            versioncode = mContext.getPackageManager().getPackageInfo(
                    "com.jiaoshizige.teacherexam", 0).versionCode;
//			System.out.println("versioncode:" + versioncode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
     * 获取应用版本名称
     *
     * @return
     */
    private String oldVersionName() {
        try {
            versionname = mContext.getPackageManager().getPackageInfo(
                    "com.jiaoshizige.teacherexam", 0).versionName;
            Log.e("versionname===========", versionname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionname;
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo() {
        postdata();
    }

    private void showNoticeDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.DialogCustom);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_custom);
        TextView updateContent = (TextView) window
                .findViewById(R.id.update_content);
        TextView newversionNumber = (TextView) window
                .findViewById(R.id.new_version_code);
        TextView oldversionNumber = (TextView) window
                .findViewById(R.id.old_version_code);
        Button btnOk = (Button) window.findViewById(R.id.btnOk);
        Button btnCancel = (Button) window.findViewById(R.id.btnCancel);
        updateContent.setText(updateMsg2);
        oldversionNumber.setText("当前版本:V" + oldVersionName());
        newversionNumber.setText("最新版本:V" + newVersionName);
        btnOk.setText("立即更新");
        btnCancel.setText("以后再说");
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
                createNotification();
                downloadApk();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
//				System.exit(0);
            }
        });
    }

    // 下载apk文件
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                Log.e("*******url", apkUrl + "/////");
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int down_step = down_step_custom;// 提示step
                int downloadCount = 0;// 已经下载好的大小
                int updateCount = 0;// 已经上传的文件大小
                int totalSize = conn.getContentLength();//获取下载文件的大小
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                Log.e("******apkFile",apkFile+"//////");
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    downloadCount += numread;// 时时获取下载到的大小
                    fos.write(buf, 0, numread);
                    if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                        updateCount += down_step;
                        // 改变通知栏
                        contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                        contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                        notification.contentView = contentView;
                        notificationManager.notify(R.layout.notification_item, notification);
                    }
                } while (true);

                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWN_ERROR);
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 请求服务器版本号
     */
    private void postdata() {

        try {
            AsyncHttpClient client = HttpClientUtil.getClient();
            String url = "http://www.yeluedu.cn/api.php?op=appup&appname=jszgz&version=" + oldVersionName();
            Log.e("url==========", url);
            Log.e("url==========", oldVersionName());
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    try {
                        String jsonStr = new String(arg2);
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        Log.e("code=========", code + "++message=====" + message);
                        if (110000 == code) {
                            JSONObject user = jsonObject.getJSONObject("result");
//							JSONObject user = result.getJSONObject("user");
                            map = new HashMap<String, Object>();
                            map.put("newVersionCode", user.getInt("versionnumber"));
                            map.put("newVersionName", user.getString("versionname"));
                            map.put("updateMsg", user.getString("updatemsg"));
                            map.put("appname", user.getString("appname"));
                            map.put("apkUrl", user.getString("apkurl"));
                            Message msg = new Message();
                            msg.obj = map;
                            handler.sendMessage(msg);

                            Log.e("map=====", map.toString());
                        } else if (110020 == code) {
                            if (!isFromMainActivity) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                      Throwable arg3) {
                    Log.e("******arg3", arg0+"/////arg1"+arg1+"******arg2"+arg2+"*******"+arg3.getMessage());
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }


    private void install1(String apkPath) {
        //7.0以上通过FileProvider
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uri = FileProvider.getUriForFile(mContext, Environment.MEDIA_MOUNTED, new File(apkPath));
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }





    /**
     * 方法描述：createNotification方法
     *
     * @param
     * @return
     * @see
     */
    public void createNotification() {

        //notification = new Notification(R.drawable.dot_enable,app_name + getString(R.string.is_downing) ,System.currentTimeMillis());
        notification = new Notification(
                //R.drawable.video_player,//应用的图标
                R.mipmap.logo,//应用的图标
                "教师资格证正在下载",
                System.currentTimeMillis());//获取系统当前时间
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews("com.jiaoshizige.teacherexam", R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "教师资格证正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

//		updateIntent = new Intent(this, AboutActivity.class);
//		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		//updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
//		notification.contentIntent = pendingIntent;

        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.notification_item, notification);
    }
}