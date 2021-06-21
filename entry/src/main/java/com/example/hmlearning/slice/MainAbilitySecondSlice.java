package com.example.hmlearning.slice;

import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.utils.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

/**
 * @Class: MainAbilitySecondSlice
 * @Description:
 * @author: Charles-lun
 * @Date: 2021/6/16
 */
public class MainAbilitySecondSlice extends AbilitySlice {

    HiLogLabel label =  new HiLogLabel(3, 0xD001100, "MainAbilitySecondSlice");

    private Button jumpToFirstSlick;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main_second);
        jumpToFirstSlick = (Button) findComponentById(ResourceTable.Id_jumpToFirstSlick);

        //回传值调用
        jumpToFirstSlick.setClickedListener((component) -> {
            HiLog.error(label, "jumpToSecondClick");
            Intent data = new Intent();
            data.setParam("data", "我是SecondSlice");
            setResult(data);
            terminate();
        });

        //普通返回
        findComponentById(ResourceTable.Id_back).setClickedListener((component)->{
            terminate();
        });

        if (intent != null && intent.hasParameter("data")) {
            String data = intent.getStringParam("data");
            HiLog.error(label, "data:" + data);

        }

        HiLog.error(label,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        HiLog.error(label,"onStop");
    }

    @Override
    public void onActive() {
        super.onActive();
        HiLog.error(label,"onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.error(label,"onInactive");
    }

    @Override
    protected void onBackground() {
        super.onBackground();
        HiLog.error(label,"onBackground");

    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
        HiLog.error(label,"onForeground");
    }
}
