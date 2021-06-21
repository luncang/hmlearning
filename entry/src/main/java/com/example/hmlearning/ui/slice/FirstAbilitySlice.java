package com.example.hmlearning.ui.slice;

import com.example.hmlearning.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

public class FirstAbilitySlice extends AbilitySlice {

    private Text text;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_first);
        text = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        text.setText(intent.getStringParam("data"));
        HiLog.error(LABEL_LOG,"我是第一个slice");
        findComponentById(ResourceTable.Id_backTo).setClickedListener((component)->{
            terminateAbility();
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
}
