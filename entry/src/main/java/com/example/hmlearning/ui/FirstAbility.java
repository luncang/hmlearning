package com.example.hmlearning.ui;

import com.example.hmlearning.ui.slice.FirstAbilitySecondSlice;
import com.example.hmlearning.ui.slice.FirstAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class FirstAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(FirstAbilitySlice.class.getName());
        addActionRoute("action.firstability.first", FirstAbilitySlice.class.getName());
        addActionRoute("action.firstability.second", FirstAbilitySecondSlice.class.getName());

    }

}
