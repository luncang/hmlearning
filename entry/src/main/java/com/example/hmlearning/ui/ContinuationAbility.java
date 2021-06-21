package com.example.hmlearning.ui;

import com.example.hmlearning.ui.slice.ContinuationAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

public class ContinuationAbility extends Ability implements IAbilityContinuation {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ContinuationAbilitySlice.class.getName());
    }

    @Override
    public boolean onStartContinuation() {
        HiLog.error(LABEL_LOG,"onStartContinuation");
        return false;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        HiLog.error(LABEL_LOG,"onSaveData");
        return false;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        HiLog.error(LABEL_LOG,"onRestoreData");
        return false;
    }

    @Override
    public void onCompleteContinuation(int i) {
        HiLog.error(LABEL_LOG,"onCompleteContinuation");
    }
}
