package com.example.hmlearning.ui;

import com.example.hmlearning.ui.slice.ServiceAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ServiceAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ServiceAbilitySlice.class.getName());
    }
}
