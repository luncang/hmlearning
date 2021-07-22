package com.example.hmlearning.ui;

import com.example.hmlearning.ui.slice.DataAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.security.SystemPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DataAbilitySlice.class.getName());
        requestPermissions();
    }

    private void requestPermissions(){
        if(verifySelfPermission(SystemPermission.WRITE_USER_STORAGE)!= IBundleManager.PERMISSION_GRANTED){
            requestPermissionsFromUser(new String[]{SystemPermission.WRITE_USER_STORAGE},0);
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
      if(permissions==null || permissions.length==0||grantResults==null||grantResults.length==0){
          return;
      }
      if(requestCode==0){
          if(grantResults[0]==IBundleManager.PERMISSION_GRANTED){
              try {
                  writeToDisk();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
    }

    private void writeToDisk() throws IOException {
        String rawFilePath="entry/resources/rawfile/userdataability.txt";
        String externalFilePath = getFilesDir()+"/userdataability.txt";
        File file = new File(externalFilePath);
        if(file.exists()){
            return;
        }
        RawFileEntry rawFileEntry = getResourceManager().getRawFileEntry(rawFilePath);
        FileOutputStream outputStream = new FileOutputStream(new File(externalFilePath));
        Resource resource = rawFileEntry.openRawFile();
        byte[] cache = new byte[1024];
        int len = resource.read(cache);
        while(len!=-1){
            outputStream.write(cache,0,len);
            len = resource.read(cache);
        }

    }
}
