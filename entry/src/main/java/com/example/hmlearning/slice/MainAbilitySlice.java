package com.example.hmlearning.slice;

import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.utils.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");


    private Button startLocalService;
    private Button stopLocalService;
    private Button jumpToSecondSlick, jumpToSecondSlice;
    private Button jumpToOtherFirst, jumpToOtherSecond;

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

        startLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onclick");
            Utils.startLocalService(getAbility());

        });

        stopLocalService.setClickedListener((component) -> {
            HiLog.error(LABEL_LOG, "onStopclick");
            Utils.stopService(getAbility());
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
