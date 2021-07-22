package com.example.hmlearning.ui;

import com.example.hmlearning.ui.slice.PageAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class PageAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(PageAbilitySlice.class.getName());
    }
}
