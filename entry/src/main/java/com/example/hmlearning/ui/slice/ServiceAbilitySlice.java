package com.example.hmlearning.ui.slice;

import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.slice.MainAbilitySecondSlice;
import com.example.hmlearning.ui.LocalServiceConnection;
import com.example.hmlearning.ui.RemoteServiceConnection;
import com.example.hmlearning.utils.Const;
import com.example.hmlearning.utils.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.TextField;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceAbilitySlice extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, ServiceAbilitySlice.class.getSimpleName());


    private Button startLocalService;
    private Button stopLocalService;
    private Button connectLocalService, disconnectLocalService;
    private Button startTask, cancelTask;
    private Button addNumber;
    private TextField numA, numB;

    private LocalServiceConnection connection = new LocalServiceConnection(this);


    private AtomicBoolean remoteConnection = new AtomicBoolean(false);

    private EventHandler handler = new EventHandler(EventRunner.current()) {

        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case Const.REMOTE_CONNECTION_SUCCESS:
                    remoteConnection.set(true);
                    add();
                    break;
            }
        }
    };

    private RemoteServiceConnection remoteServiceConnection = new RemoteServiceConnection(this, handler);


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_service);
        startLocalService = (Button) findComponentById(ResourceTable.Id_startLocalService);
        stopLocalService = (Button) findComponentById(ResourceTable.Id_stopLocalService);
        connectLocalService = (Button) findComponentById(ResourceTable.Id_connectService);
        disconnectLocalService = (Button) findComponentById(ResourceTable.Id_disconnectService);
        startTask = (Button) findComponentById(ResourceTable.Id_startTask);
        cancelTask = (Button) findComponentById(ResourceTable.Id_cancelTask);
        addNumber = (Button) findComponentById(ResourceTable.Id_addNumber);
        numA = (TextField) findComponentById(ResourceTable.Id_numA);
        numB = (TextField) findComponentById(ResourceTable.Id_numB);

        //普通启动service
        startLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onclick");
            Utils.startLocalService(getAbility());

        });

        //普通停止service
        stopLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onStopclick");
            Utils.stopService(getAbility());
        });

        //连接service
        connectLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onConnectClick");
//            Utils.connectLocalService(getAbility(),connection);
            connection.connectLocalService();
        });

        //断开连接service
        disconnectLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onDisconnectClick");
//            Utils.disconnectLocalService(getAbility(),connection);
            connection.disconnectLocalService();
        });

        startTask.setClickedListener((component) -> {
            connection.startNewTask("baidu.com");
        });

        cancelTask.setClickedListener((component) -> {
            connection.cancelTask();
        });

        //连接远程service
        addNumber.setClickedListener((c) -> {
            startAddCalculator();
        });


    }

    private void startAddCalculator() {
        if (remoteConnection.get()) {
            add();
            return;
        }
        HiLog.error(LABEL_LOG, "连接远程service");
        remoteServiceConnection.connectRemoteService();

    }

    private void add() {
        int a = Integer.parseInt(numA.getText());
        int b = Integer.parseInt(numB.getText());
        int result = remoteServiceConnection.addNumber(a, b);
        HiLog.error(LABEL_LOG, "result:" + result);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.stopService(getAbility());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
