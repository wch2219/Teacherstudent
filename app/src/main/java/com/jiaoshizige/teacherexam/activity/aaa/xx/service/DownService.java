package com.jiaoshizige.teacherexam.activity.aaa.xx.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.VideoDownAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.interfa.DownSuccessbackListener;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.MessageEvent;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/9/4.
 */

public class DownService extends Service{
    private Context ctx;
    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        VideoDownAdapter.setOnDownVideoBackInterface(new VideoDownAdapter.DownVideoBackInterface() {
            @Override
            public void backDown(String vid, PolyvDownloadInfo downloadInfo,int bitrate) {
                Toast.makeText(ctx, "开始下载", Toast.LENGTH_SHORT).show();
                starDown(false,vid,bitrate);
            }
        });
        EventBus.getDefault().register(this);
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");
        return super.onStartCommand(intent, flags, startId);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        final String vid = messageEvent.getVid();
        int bitRate = messageEvent.getBitRate();
        downListener(vid, bitRate);
    }

    private void downListener(final String vid, int bitRate) {
        PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitRate);
        downloader.start(ctx);
        downloader.setPolyvDownloadProressListener(new IPolyvDownloaderProgressListener() {
            @Override
            public void onDownload(long l, long l1) {
                WcHLogUtils.I(l+"----"+l1+"------"+(((l%l1))));
                PolyvDownloadSQLiteHelper.getInstance(ctx).updateProgress(l,l1, (int) ((l%l1)),vid);
            }

            @Override
            public void onDownloadSuccess() {
                if (successbackListener != null) {
                    successbackListener.success();
                }
                PolyvDownloadSQLiteHelper.getInstance(ctx).updateProgress(100,100, 100,vid);
//                    showNotifi();
                Toast.makeText(ctx, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFail(@NonNull PolyvDownloaderErrorReason polyvDownloaderErrorReason) {
                Toast.makeText(ctx, polyvDownloaderErrorReason.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
            @Override
            public void onSpeed(int i) {
                WcHLogUtils.I(i+"下载速度");
            }
        });
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void starDown(boolean isAll, final String vid,int bitrate) {
        if (isAll) {
            PolyvDownloaderManager.startAll(ctx);
        }else{
            downListener(vid, bitrate);
        }
    }
    public void showNotifi(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.ic_launcher); builder.setContentTitle("下载内容");
        builder.setContentText("下载成功");
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        // Creates an explicit intent for an Activity in your app //自定义打开的界面
//        Intent resultIntent = new Intent(ctx, FlashPageActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent() ,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) ctx .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());



    }

    private static DownSuccessbackListener successbackListener;
    public static void setSuccessbackListener(DownSuccessbackListener backListener){
        successbackListener = backListener;

    }
}

