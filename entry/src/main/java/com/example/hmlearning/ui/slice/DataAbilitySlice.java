package com.example.hmlearning.ui.slice;

import com.example.hmlearning.ResourceTable;
import com.example.hmlearning.utils.Const;
import ohos.aafwk.ability.*;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;

public class DataAbilitySlice extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, DataAbilitySlice.class.getSimpleName());

    private Text logText;

    private DataAbilityHelper databaseHelper;

    private IDataAbilityObserver dataAbilityObserver = () -> {

    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_data);
        initComponents();
        initDatabaseHelper();
    }

    private void initComponents() {
        logText = (Text) findComponentById(ResourceTable.Id_log_text);
        findComponentById(ResourceTable.Id_insert_button).setClickedListener(this::insert);
        findComponentById(ResourceTable.Id_delete_button).setClickedListener(this::delete);
        findComponentById(ResourceTable.Id_update_button).setClickedListener(this::update);
        findComponentById(ResourceTable.Id_query_button).setClickedListener(c -> query(false));
        findComponentById(ResourceTable.Id_batch_insert_button).setClickedListener(this::batchInsert);
        findComponentById(ResourceTable.Id_batch_execute_button).setClickedListener(this::batchExecute);
        findComponentById(ResourceTable.Id_read_file_button).setClickedListener(this::readTextFile);

    }

    private void initDatabaseHelper() {
        databaseHelper = DataAbilityHelper.creator(this);
        databaseHelper.registerObserver(Uri.parse(Const.BASE_URI), dataAbilityObserver);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void insert(Component component) {
        String name = getRandomName();
        int age = getRandomAge();

        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(Const.DB_COLUMN_NAME, name);
        valuesBucket.putInteger(Const.DB_COLUMN_AGE, age);
        try {
            int result = databaseHelper.insert(Uri.parse(Const.BASE_URI + Const.DATA_PATH), valuesBucket);
            HiLog.info(LABEL_LOG, "%{public}s", "insert result:" + result);
        } catch (DataAbilityRemoteException e) {
            HiLog.error(LABEL_LOG, "%{public}s", "insert exception:" + e.getMessage());
        }

    }

    private void delete(Component component) {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.between(Const.DB_COLUMN_USER_ID, 1, 2);
        try {
            int rowId = databaseHelper.delete(Uri.parse(Const.BASE_URI + Const.DATA_PATH), predicates);
            HiLog.info(LABEL_LOG, "delete result %{public}s", rowId + "");
        } catch (DataAbilityRemoteException e) {
            HiLog.error(LABEL_LOG, "%{public}s", "delete error:" + e.getMessage());
        }

    }

    private void update(Component component) {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo(Const.DB_COLUMN_USER_ID, 1);
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(Const.DB_COLUMN_NAME, "Tom_update");
        valuesBucket.putInteger(Const.DB_COLUMN_AGE, 0);
        try {
            int rowId = databaseHelper.update(Uri.parse(Const.BASE_URI + Const.DATA_PATH), valuesBucket, predicates);
            HiLog.info(LABEL_LOG, "update rowId:%{public}s", rowId + "");
        } catch (DataAbilityRemoteException e) {
            HiLog.error(LABEL_LOG, "update exception:%{public}s", e.getCause());
        }
    }

    private void query(boolean queryAll) {
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            String[] columns = new String[]{Const.DB_COLUMN_NAME, Const.DB_COLUMN_AGE, Const.DB_COLUMN_USER_ID};
            DataAbilityPredicates predicates = new DataAbilityPredicates();
            if (!queryAll) {
                predicates.between(Const.DB_COLUMN_USER_ID, 0, 5);
            }
            try {
                ResultSet resultSet = databaseHelper.query(Uri.parse(Const.BASE_URI + Const.DATA_PATH), columns, predicates);
                appendText(resultSet);
            } catch (DataAbilityRemoteException e) {
                HiLog.error(LABEL_LOG, "exception:%{public}s", e.getMessage());
            }

        });
    }

    private void batchInsert(Component component) {
        ValuesBucket[] valuesBuckets = new ValuesBucket[2];
        valuesBuckets[0] = new ValuesBucket();
        valuesBuckets[0].putString(Const.DB_COLUMN_NAME, getRandomName());
        valuesBuckets[0].putInteger(Const.DB_COLUMN_AGE, getRandomAge());
        valuesBuckets[1] = new ValuesBucket();
        valuesBuckets[1].putString(Const.DB_COLUMN_NAME, getRandomName());
        valuesBuckets[1].putInteger(Const.DB_COLUMN_AGE, getRandomAge());
        try {
            databaseHelper.batchInsert(Uri.parse(Const.BASE_URI + Const.DATA_PATH), valuesBuckets);
        } catch (DataAbilityRemoteException e) {
            HiLog.error(LABEL_LOG, "query data exception:%{public}s", e.getMessage());
        }
    }

    private void batchExecute(Component component) {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.between(Const.DB_COLUMN_USER_ID, 1, 3);
        DataAbilityOperation deleteOperation = DataAbilityOperation.newDeleteBuilder(Uri.parse(Const.BASE_URI + Const.DATA_PATH))
                .withPredicates(predicates)
                .build();

        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(Const.DB_COLUMN_NAME, getRandomName());
        valuesBucket.putInteger(Const.DB_COLUMN_AGE, getRandomAge());
        DataAbilityOperation insertOperation = DataAbilityOperation.newInsertBuilder(
                Uri.parse(Const.BASE_URI + Const.DATA_PATH)).withValuesBucket(valuesBucket).build();

        ArrayList<DataAbilityOperation> operations = new ArrayList<>();
        operations.add(deleteOperation);
        operations.add(insertOperation);

        try {
            databaseHelper.executeBatch(Uri.parse(Const.BASE_URI + Const.DATA_PATH), operations);
            query(true);
        } catch (DataAbilityRemoteException | OperationExecuteException e) {
            HiLog.error(LABEL_LOG, "%{public}s", "batchExecute: dataAbilityRemoteException|operationExecuteException");

        }
    }

    private void readTextFile(Component component) {
        try {
            FileDescriptor fileDescriptor = databaseHelper.openFile(Uri.parse(Const.BASE_URI + File.separator + "document?userdataability.txt"), "r");
            if (fileDescriptor == null) {
                new ToastDialog(this).setText("no such file").show();
                return;
            }
            showText(fileDescriptor);
        } catch (DataAbilityRemoteException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showText(FileDescriptor fileDescriptor) {
        FileInputStream inputStream = new FileInputStream(fileDescriptor);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logText.setText(stringBuilder.toString());
    }

    private void appendText(ResultSet resultSet) {
        if (!resultSet.goToFirstRow()) {
            HiLog.info(LABEL_LOG, "query has no result");
            return;
        }
        int querycount = 0;
        int allowQueryMaxCount = 100;
        StringBuilder appendStr = new StringBuilder();
        int nameIndex = resultSet.getColumnIndexForName(Const.DB_COLUMN_NAME);
        int ageIndex = resultSet.getColumnIndexForName(Const.DB_COLUMN_AGE);
        int useIndex = resultSet.getColumnIndexForName(Const.DB_COLUMN_USER_ID);
        do {
            querycount++;
            String name = resultSet.getString(nameIndex);
            int age = resultSet.getInt(ageIndex);
            int userId = resultSet.getInt(useIndex);
            appendStr.append(userId + " ")
                    .append(name + " ")
                    .append(age + System.lineSeparator());
        } while (resultSet.goToNextRow() && querycount < allowQueryMaxCount);
        HiLog.info(LABEL_LOG, "queryCount:%{public}s,appendStr:%{public}s", querycount, appendStr.toString());
        getUITaskDispatcher().asyncDispatch(() -> {
            logText.setText(appendStr.toString());
        });
    }

    private int getRandomAge() {
        return new SecureRandom().nextInt(20);
    }

    private String getRandomName() {
        String[] names = {"Tom", "Jerry", "Bob", "Coco", "Sum", "Marry"};
        int index = new SecureRandom().nextInt(names.length);
        return names[index];
    }
}
