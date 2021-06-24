package com.example.hmlearning.slice;

import com.example.hmlearning.MainAbility;
import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.ui.LocalServiceConnection;
import com.example.hmlearning.ui.RemoteServiceConnection;
import com.example.hmlearning.utils.Const;
import com.example.hmlearning.utils.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.TextField;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainAbilitySlice extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");


    private Button startLocalService;
    private Button stopLocalService;
    private Button jumpToSecondSlick, jumpToSecondSlice;
    private Button jumpToOtherFirst, jumpToOtherSecond;
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
        super.setUIContent(ResourceTable.Layout_ability_main);
        startLocalService = (Button) findComponentById(ResourceTable.Id_startLocalService);
        stopLocalService = (Button) findComponentById(ResourceTable.Id_stopLocalService);
        jumpToSecondSlick = (Button) findComponentById(ResourceTable.Id_jumpToSecondSlick);
        jumpToSecondSlice = (Button) findComponentById(ResourceTable.Id_jumpToSecondSlice);
        jumpToOtherFirst = (Button) findComponentById(ResourceTable.Id_jumpToOtherFirst);
        jumpToOtherSecond = (Button) findComponentById(ResourceTable.Id_jumpToOtherSecond);
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


        //相同page,回传值
        jumpToSecondSlick.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "jumpToSecondClick");
            Intent data = new Intent();
            data.setParam("data", "我是MainSlice");
            presentForResult(new MainAbilitySecondSlice(), data, 1);
        });

        //相同page,普通跳转
        jumpToSecondSlice.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "jumpToSecondClick2");
            Intent data = new Intent();
            data.setParam("data", "我是MainSlice");
            present(new MainAbilitySecondSlice(), data);
        });

        if (intent != null && intent.hasParameter("data")) {
            String data = intent.getStringParam("data");
            HiLog.error(LABEL_LOG, "data:" + data);

        }

        //不同page，普通跳转
        jumpToOtherFirst.setClickedListener((component) -> {
            Intent data = new Intent();
            data.setParam("data", "第一个slice");
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withAction("action.firstability.first")
                    .build();
            data.setOperation(operation);
            startAbility(data);

        });

        //不同page，回传值跳转
        jumpToOtherSecond.setClickedListener((component) -> {
            Intent data = new Intent();
            data.setParam("data", "第二个slice");

            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withAction("action.firstability.second")
                    .build();
            data.setOperation(operation);
            startAbilityForResult(data, 2);

        });


        HiLog.error(LABEL_LOG, "onStart");

    }


    private void startAddCalculator() {
        if (remoteConnection.get()) {
            add();
            return;
        }
        HiLog.error(LABEL_LOG,"连接远程service");
        remoteServiceConnection.connectRemoteService();

    }

    private void add() {
        int a = Integer.parseInt(numA.getText());
        int b = Integer.parseInt(numB.getText());
        int result = remoteServiceConnection.addNumber(a, b);
        HiLog.error(LABEL_LOG,"result:"+result);
    }

    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        if (requestCode == 1) {
            HiLog.error(LABEL_LOG, "结果：" + resultIntent.getStringParam("data"));
        }
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        if (requestCode == 2 && resultCode == 100) {
            HiLog.error(LABEL_LOG, "结果：" + resultData.getStringParam("data"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.stopService(getAbility());
        HiLog.error(LABEL_LOG, "onStop");
    }

    @Override
    public void onActive() {
        super.onActive();
        HiLog.error(LABEL_LOG, "onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.error(LABEL_LOG, "onInactive");
    }

    @Override
    protected void onBackground() {
        super.onBackground();
        HiLog.error(LABEL_LOG, "onBackground");

    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
        HiLog.error(LABEL_LOG, "onForeground");
    }
}
