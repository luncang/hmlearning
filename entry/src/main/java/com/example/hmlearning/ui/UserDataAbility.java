package com.example.hmlearning.ui;

import com.example.hmlearning.utils.Const;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.global.resource.RawFileEntry;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.MessageParcel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

public class UserDataAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, UserDataAbility.class.getSimpleName());

    private StoreConfig config = StoreConfig.newDefaultConfig(Const.DB_NAME);

    private RdbStore rdbStore;

    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore rdbStore) {
            //建表
            rdbStore.executeSql("create table if not exists " + Const.DB_TAB_NAME + " (userId integer primary key autoincrement," +
                    Const.DB_COLUMN_NAME + " text not null, " + Const.DB_COLUMN_AGE + " integer)");
            HiLog.info(LABEL_LOG, "%{public}s", "create a new database");
        }

        @Override
        public void onUpgrade(RdbStore rdbStore, int oldVersion, int newVersion) {
            HiLog.info(LABEL_LOG, "%{public}s", "database upgrade");

        }
    };


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "MyDataAbility onStart");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //创建数据库
        rdbStore = databaseHelper.getRdbStore(config, 1, rdbOpenCallback);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, Const.DB_TAB_NAME);
        return rdbStore.query(rdbPredicates, columns);
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        HiLog.info(LABEL_LOG, "MyDataAbility insert");
        String path = uri.getLastPath();
        if (!"person".equals(path)) {
            HiLog.info(LABEL_LOG, "%{public}s", "DataAbility insert path is not matched");
            return -1;
        }
        ValuesBucket values = new ValuesBucket();
        values.putString(Const.DB_COLUMN_NAME, value.getString(Const.DB_COLUMN_NAME));
        values.putInteger(Const.DB_COLUMN_AGE, value.getInteger(Const.DB_COLUMN_AGE));
        int index = (int) rdbStore.insert(Const.DB_TAB_NAME, values);
        DataAbilityHelper.creator(this).notifyChange(uri);
        return index;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, Const.DB_TAB_NAME);
        int index = rdbStore.delete(rdbPredicates);
        DataAbilityHelper.creator(this).notifyChange(uri);
        return index;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, Const.DB_TAB_NAME);
        int index = rdbStore.update(value, rdbPredicates);
        DataAbilityHelper.creator(this).notifyChange(uri);
        return index;
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        HiLog.info(LABEL_LOG,"dir:%{public}s,query:%{public}s",getFilesDir(),uri.getDecodedQuery());
        File file = new File(getFilesDir(), uri.getDecodedQuery());
        if (!"rw".equals(mode)) {
            boolean result = file.setReadOnly();
            HiLog.info(LABEL_LOG, "%{public}s", "setReadOnly result: " + result);
        }
        FileDescriptor fileDescriptor = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileDescriptor = fileInputStream.getFD();
            return MessageParcel.dupFileDescriptor(fileDescriptor);
        }catch (Exception e){
            HiLog.error(LABEL_LOG, "%{public}s", "openFile: ioException");

        }
        return fileDescriptor;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}