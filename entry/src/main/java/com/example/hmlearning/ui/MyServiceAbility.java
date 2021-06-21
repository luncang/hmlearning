package com.example.hmlearning.ui;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 *
 */

public class MyServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onStart");
        super.onStart(intent);
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
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onCommand");
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onConnect");
        return null;
    }

    @Override
    public void onDisconnect(Intent intent) {
        HiLog.error(LABEL_LOG, "MyServiceAbility::onDisconnect");
    }
}