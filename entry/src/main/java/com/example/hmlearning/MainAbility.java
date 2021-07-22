package com.example.hmlearning;

import com.example.hmlearning.slice.MainAbilitySlice2;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability {

    public static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice2.class.getName());
        HiLog.error(LABEL_LOG,"onStart");
    }


    @Override
    protected void onActive() {
        super.onActive();
        HiLog.error(LABEL_LOG,"onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.error(LABEL_LOG,"onInactive");
    }

    @Override
    protected void onBackground() {
        super.onBackground();
        HiLog.error(LABEL_LOG,"onBackground");

    }

    @Override
    protected void onStop() {
        super.onStop();
        HiLog.error(LABEL_LOG,"onStop");

    }

}
