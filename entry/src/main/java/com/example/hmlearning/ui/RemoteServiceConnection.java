package com.example.hmlearning.ui;

import com.example.hmlearning.utils.Const;
import com.example.remorerpcserver.CalculatorInterfaceProxy;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.hiviewdfx.HiLog;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

import static com.example.hmlearning.MainAbility.LABEL_LOG;

/**
 * @Class: RemoteServiceConnection
 * @Description:
 * @author: Charles-lun
 * @Date: 2021/6/24
 */
public class RemoteServiceConnection implements IAbilityConnection {

    private Context ability;

    private EventHandler handler;

    private CalculatorInterfaceProxy proxy;


    public RemoteServiceConnection(Context ability, EventHandler handler) {
        this.ability = ability;
        this.handler = handler;
    }

    @Override
    public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
        proxy = new CalculatorInterfaceProxy(iRemoteObject);
        handler.sendEvent(Const.REMOTE_CONNECTION_SUCCESS);
        HiLog.error(LABEL_LOG, "onAbilityConnectDone");
    }

    @Override
    public void onAbilityDisconnectDone(ElementName elementName, int i) {
        HiLog.error(LABEL_LOG, "onAbilityDisconnectDone");
    }

    public int addNumber(int a, int b) {
        int sum = 0;
        try {
            sum = proxy.addNumber(a, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public void connectRemoteService() {
        Intent intent = new Intent();
        ElementName elementName = new ElementName("", "com.example.remorerpcserver", "com.example.remorerpcserver.RemoteService");
        intent.setElement(elementName);
        ability.connectAbility(intent, this);

    }

    public void disconnectRemoteService() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.example.remorerpcserver")
                .withAbilityName("com.example.remorerpcserver.RemoteService")
                .build();
        intent.setOperation(operation);
        ability.disconnectAbility(this);
        ability.stopAbility(intent);
    }
}
