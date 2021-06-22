package com.example.hmlearning.utils;

import com.example.hmlearning.ui.LocalServiceAbility;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;

/**
 * @Class: Utils
 * @Description:
 * @author: Charles-lun
 * @Date: 2021/6/15
 */
public class Utils {

    /**
     * 启动本地
     */
    public static void startLocalService(Ability ability) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.example.hmlearning")
                .withAbilityName("com.example.hmlearning.ui.MyServiceAbility")
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    /**
     * 停止service
     * @param ability
     */
    public static void stopService(Ability ability){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.example.hmlearning")
                .withAbilityName("com.example.hmlearning.ui.MyServiceAbility")
                .build();
        intent.setOperation(operation);
        ability.stopAbility(intent);
    }

    /**
     * 启动远程service
     */
    public static void startRemoteService(Ability ability) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("deviceId")
                .withBundleName("com.domainname.hiworld.himusic")
                .withAbilityName("com.domainname.hiworld.himusic.ServiceAbility")
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE) // 设置支持分布式调度系统多设备启动的标识
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }


    /**
     * 连接service
     * @param ability
     * @param connection
     */
    public static void connectLocalService(Ability ability, IAbilityConnection connection){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.example.hmlearning")
                .withAbilityName(LocalServiceAbility.class)
                .build();
        intent.setOperation(operation);
        ability.connectAbility(intent,connection);
    }

    /**
     * 断开本地连接
     * @param ability
     * @param connection
     */
    public static void disconnectLocalService(Ability ability, IAbilityConnection connection){
        ability.disconnectAbility(connection);
    }


}
