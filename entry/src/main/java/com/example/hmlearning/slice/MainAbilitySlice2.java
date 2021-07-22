package com.example.hmlearning.slice;

import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.ui.DataAbility;
import com.example.hmlearning.ui.PageAbility;
import com.example.hmlearning.ui.ServiceAbility;
import com.example.hmlearning.utils.Const;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice2 extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbility");

    private Button page,service,data;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main_2);
        page = (Button) findComponentById(ResourceTable.Id_page);
        service = (Button) findComponentById(ResourceTable.Id_service);
        data = (Button) findComponentById(ResourceTable.Id_data);

        page.setClickedListener((component)->{
            Intent pageIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withBundleName(Const.BUNDLE_NAME)
                    .withAbilityName(PageAbility.class)
                    .build();
            pageIntent.setOperation(operation);
            startAbility(pageIntent);
        });

        service.setClickedListener((component)->{
            Intent pageIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withBundleName(Const.BUNDLE_NAME)
                    .withAbilityName(ServiceAbility.class)
                    .build();
            pageIntent.setOperation(operation);
            startAbility(pageIntent);
        });

        data.setClickedListener((component)->{
            Intent pageIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withBundleName(Const.BUNDLE_NAME)
                    .withAbilityName(DataAbility.class)
                    .build();
            pageIntent.setOperation(operation);
            startAbility(pageIntent);
        });

        HiLog.error(LABEL_LOG, "onStart");

    }



    @Override
    protected void onStop() {
        super.onStop();
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
