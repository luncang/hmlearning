package com.example.hmlearning.ui;

import com.example.hmlearning.utils.Const;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.rpc.*;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

/**
 * @Class: LocalServiceConnection
 * @Description:
 * @author: Charles-lun
 * @Date: 2021/6/22
 */
public class LocalServiceConnection implements IAbilityConnection {


    private static EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case Const.HANDLER_EVENT_ID:
                    HiLog.error(LABEL_LOG, event.param + ":" + event.object);
                    break;
            }
        }
    };

    private LocalServiceAbilityProxy proxy;

    private Context context;

    public LocalServiceConnection(Context context) {
        this.context = context;
    }

    @Override
    public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
        HiLog.error(LABEL_LOG, "onAbilityConnectDone");
        sendHandlerMessage("onAbilityConnectDone");
        proxy = new LocalServiceAbilityProxy(iRemoteObject);
    }

    @Override
    public void onAbilityDisconnectDone(ElementName elementName, int i) {
        HiLog.error(LABEL_LOG, "onAbilityDisconnectDone");
        sendHandlerMessage("onAbilityDisconnectDone");
        proxy = null;
    }


    public void connectLocalService() {
        if (proxy != null) {
            sendHandlerMessage("service has connected");
            return;
        }
        context.connectAbility(getIntent(), this);
    }

    public void disconnectLocalService() {
        if (proxy == null) {
            sendHandlerMessage("service has disconnected");
            return;
        }
        context.disconnectAbility(this);
        context.stopAbility(getIntent());
        proxy = null;
    }

    public void startNewTask(String url) {
        if (proxy == null) {
            return;
        }
        proxy.startTask(url);
    }

    public void cancelTask() {
        if (proxy == null) {
            return;
        }
        proxy.cancelTask();
    }


    public static EventHandler getEventHandler() {
        return handler;
    }


    private Intent getIntent() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId("")
                .withBundleName("com.example.hmlearning")
                .withAbilityName(LocalServiceAbility.class.getName())
                .build();
        intent.setOperation(operation);
        return intent;
    }


    private class LocalServiceAbilityProxy implements IRemoteBroker {

        private IRemoteObject remoteObject;

        public LocalServiceAbilityProxy(IRemoteObject object) {
            this.remoteObject = object;
        }

        @Override
        public IRemoteObject asObject() {
            return remoteObject;
        }

        private void startTask(String url) {
            MessageParcel data = MessageParcel.obtain();
            MessageParcel reply = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                remoteObject.sendRequest(Const.REMOTE_REQUEST_CODE_NEW_TASK, data, reply, messageOption);
                if (reply.readInt() == Const.SEND_REQUEST_SUCCESS) {
                    sendHandlerMessage("start task");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                data.reclaim();
                reply.reclaim();
            }

        }


        private void cancelTask() {
            MessageParcel data = MessageParcel.obtain();
            MessageParcel reply = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            try {
                remoteObject.sendRequest(Const.REMOTE_REQUEST_CODE_CANCEL_TASK, data, reply, messageOption);
                if (reply.readInt() == Const.SEND_REQUEST_SUCCESS) {
                    sendHandlerMessage("cancel task");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                data.reclaim();
                reply.reclaim();
            }
        }
    }


    private void sendHandlerMessage(String message) {
        InnerEvent innerEvent = InnerEvent.get(Const.HANDLER_EVENT_ID, Const.HANDLER_EVENT_PARAM, message);
        handler.sendEvent(innerEvent);
    }

}
