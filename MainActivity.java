package YOUR_PACKAGE_IDENTIFIER;

import android.content.Context;
import com.getcapacitor.BridgeActivity;

import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends BridgeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstallTime();
    }
    /**
     * start install-time delivery mode
     */
    private void initInstallTime() {
        try {
            Context context = createPackageContext(YOUR_PACKAGE_IDENTIFIER, 0);
            AssetManager assetManager  = context.getAssets();
            copyAsset(assetManager, YOUR_ASSET_NAME);
            //File directories[] = new File(String.valueOf(getFilesDir())).listFiles(File::isDirectory);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void copyAsset(AssetManager asset, String path){
        try {
            String[] contents = asset.list(path);
            if (contents == null || contents.length == 0) {
                throw new IOException();
            }
            File dir = new File(getFilesDir(), path);
            if(dir.exists()){
                deleteDirectory(dir);
            }
            dir.mkdirs();
            for (String entry : contents) {
                copyAsset(asset, path + "/" + entry);
            }
        }catch (IOException e){
            copyFileAsset(asset, path);
        }
    }
    private boolean deleteDirectory(File directoryToBeDeleted){
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null){
            for( File file: allContents){
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    private void copyFileAsset(AssetManager assets, String path){
        File file = new File(getFilesDir(), path);
        if(!file.exists()){
        try {
            InputStream inputStream = assets.open(path);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        }
    }

}
