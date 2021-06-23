package com.example.hmlearning.ui;

import com.example.hmlearning.utils.Const;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.LocalRemoteObject;
import ohos.aafwk.content.Intent;
import ohos.event.notification.NotificationRequest;
import ohos.eventhandler.InnerEvent;
import ohos.rpc.IRemoteObject;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

/**
 *
 */

public class LocalServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");

    private static final int NOTIFICATION_ID = 0XD0000002;

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onStart");
//        startForeground();
        super.onStart(intent);
    }


    private void startForeground(){
        // 创建通知，其中1005为notificationId
        NotificationRequest request = new NotificationRequest(NOTIFICATION_ID);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle("Foreground Service").setText("I'm a ForeGround Service");
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);

        // 绑定通知，1005为创建通知时传入的notificationId
        keepBackgroundRunning(NOTIFICATION_ID, request);
    }

    private void cancelForeground(){
        cancelBackgroundRunning();
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.error(LABEL_LOG, "MyServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.error(LABEL_LOG, "MyServiceAbility::onStop");
//        cancelForeground();
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onCommand");
    }

    /**
     * 返回给客户端
     *
     * @param intent
     * @return
     */
    @Override
    public IRemoteObject onConnect(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onConnect");
        return new MyRemoteObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onDisconnect");
    }


    //创建自定义LocalRemoteObject
    private class MyRemoteObject extends LocalRemoteObject {
        public MyRemoteObject() {

        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException {
            HiLog.error(LABEL_LOG, "MyServiceAbility::onRemoteRequest:" + data.readString() + ":" + reply.readString() + ":" + code);
            switch (code) {
                case Const.REMOTE_REQUEST_CODE_NEW_TASK:
                    startNewTask(data.readString());
                    reply.writeInt(Const.SEND_REQUEST_SUCCESS);
                    break;
                case Const.REMOTE_REQUEST_CODE_CANCEL_TASK:
                    cancelTask();
                    reply.writeInt(Const.SEND_REQUEST_SUCCESS);
                    break;
            }
            return true;
        }
    }


    private void sendMessage(String msg){
        InnerEvent event = InnerEvent.get(Const.HANDLER_EVENT_ID, Const.HANDLER_EVENT_PARAM, msg);
        LocalServiceConnection.getEventHandler().sendEvent(event);
    }


    private void startNewTask(String url){
        HiLog.error(LABEL_LOG,"startNewTask");
        sendMessage("service start new task");
    }

    private void cancelTask(){
        HiLog.error(LABEL_LOG,"cancelTask");
        sendMessage("service cancel task");
    }
}