package com.example.hmlearning.ui.slice;

import com.example.hmlearning.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

public class ContinuationAbilitySlice extends AbilitySlice implements IAbilityContinuation {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_continuation);
        //开始迁移
        findComponentById(ResourceTable.Id_continuation).setClickedListener((component) -> {
                    try {
                        continueAbility();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );

        //允许回迁
        findComponentById(ResourceTable.Id_continuation2).setClickedListener((component) -> {
            try {
                continueAbilityReversibly();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public boolean onStartContinuation() {
        HiLog.error(LABEL_LOG, "onStartContinuation");
        return false;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        HiLog.error(LABEL_LOG, "onSaveData");
        return false;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        HiLog.error(LABEL_LOG, "onRestoreData");
        return false;
    }

    @Override
    public void onCompleteContinuation(int i) {
        HiLog.error(LABEL_LOG, "onCompleteContinuation");
    }
}
