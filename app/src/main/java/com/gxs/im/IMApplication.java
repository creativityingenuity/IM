package com.gxs.im;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.gxs.im.adapter.MessageListenerAdapter;
import com.gxs.im.base.BaseActivity;
import com.gxs.im.chattask.ChatActivity;
import com.gxs.im.db.DBUtils;
import com.gxs.im.logintask.LoginActivity;
import com.gxs.im.model.ContactUpdateEvent;
import com.gxs.im.utils.ThreadPoolUtils;
import com.gxs.im.utils.ToastUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class IMApplication extends Application {
    private HashMap<String, Object> mHashMap = null;
    private static IMApplication mApplication = null;

    public IMApplication() {
    }

    public static IMApplication getInstance() {
        return mApplication;
    }

    /**
     * 从application 中的map中拿取数据
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        if (!TextUtils.isEmpty(key)) {
            return mHashMap.get(key);
        }
        return "";
    }

    /**
     * 保存数据到applica 中的Map
     *
     * @param key
     * @param o
     */
    public void setData(String key, Object o) {
        mHashMap.put(key, o);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mHashMap = new HashMap<>();
        //初始化环信
        initEM();
        //初始化bmob
        initBmob();
        //初始化Xlog
        XLog.init(LogLevel.ALL, new LogConfiguration.Builder().b().build());
        initDB();
    }

    private void initDB() {
        DBUtils.initDB(this);
    }

    private void initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "2007412b1f53f7b665a34bf1b184cc5a");
    }

    private void initEM() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //添加通讯录监听
        initContactListener();
        //添加消息的监听
        initMessageListener();
        //监听连接状态的改变
        initConnectionListener();
    }
    private List<BaseActivity> mBaseActivityList = new ArrayList<>();
    public void addActivity(BaseActivity baseActivity){
        if(!mBaseActivityList.contains(baseActivity)) {
            mBaseActivityList.add(baseActivity);
        }
    }
    public void removeActivity(BaseActivity baseActivity){
        mBaseActivityList.remove(baseActivity);
    }
    private void initConnectionListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int i) {
                if (i== EMError.USER_LOGIN_ANOTHER_DEVICE){
                    // 显示帐号在其他设备登录
                    /**
                     *  将当前任务栈中所有的Activity给清空掉
                     *  重新打开登录界面
                     */
                    for (BaseActivity baseActivity : mBaseActivityList) {
                        baseActivity.finish();
                    }

                    Intent intent = new Intent(IMApplication.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ThreadPoolUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(IMApplication.this,"您已在其他设备上登录了，请重新登录。");
                        }
                    });

                }
            }
        });
    }

    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new MessageListenerAdapter() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                super.onMessageReceived(list);
                if (list != null && list.size() > 0) {
                    /**
                     * 判断当前应用是否在后台运行
                     如果在后台运行，则发出通知，
                     */
                    if (isRunningBackground()) {
                        sendNotification(list.get(0));
                    }
                    EventBus.getDefault().post(list.get(0));
                }
            }
        });
    }

    /**
     * 想通知栏发送消息
     * @param emMessage
     */
    private void sendNotification(EMMessage emMessage) {
        EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("username",emMessage.getFrom());
        Intent[] intents = {mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT) ;
        Notification notification = new Notification.Builder(this)
                .setContentTitle("您有一条新消息")//设置通知的标题
                .setContentText(messageBody.getMessage())//设置通知的内容
                .setSmallIcon(R.mipmap.default_acatar)//设置一个小图标  如果不设置 通知显示会有问题
                .setContentInfo(emMessage.getFrom())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.nv))
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)//设置setAutoCancel为true 当用户点击当前的通知的时候 通知会自动消失
                .setContentIntent(pendingIntent)//点击通知之后 通过pendingintent可以操作一个组件(activity service 广播)也就是相当于给通知设置了一个点击事件
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //通过NotificationManager 发送通知 第一个参数 id 可以通过id找到notification 并且把这个通知消除
        manager.notify(1, notification);
    }

    /**
     * 判断当前应用是否在后台运行
     *
     * @return
     */
    private boolean isRunningBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        if (runningTasks.get(0).topActivity.getPackageName().equals(getPackageName())) {
            return false;
        }
        return true;
    }

    private void initContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //添加好友 更新ui
                EventBus.getDefault().post(new ContactUpdateEvent(s, true));
            }

            @Override
            public void onContactDeleted(String s) {
                EventBus.getDefault().post(new ContactUpdateEvent(s, false));
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {
                //好友请求被拒绝
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
