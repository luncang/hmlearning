package com.example.remorerpcserver;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class RemoteService extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "RemoteService::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "RemoteService::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "RemoteService::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return new ICalcultorInterfaceImpl("this is remote service");
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    private class ICalcultorInterfaceImpl extends CalculatorInterfaceStub{

        public ICalcultorInterfaceImpl(String descriptor) {
            super(descriptor);
        }

        @Override
        public int addNumber(int a, int b) throws RemoteException {
            return a+b;
        }
    }
}