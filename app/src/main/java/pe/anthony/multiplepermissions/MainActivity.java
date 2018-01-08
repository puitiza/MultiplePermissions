package pe.anthony.multiplepermissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karan.churi.PermissionManager.PermissionManager;

public class MainActivity extends AppCompatActivity {

    FirstRunDetected firstRun;

    private Button buttonReal;
    private static final int  Permission_All = 1 ;
    private static final int PERMISO_LOCATION = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonReal = findViewById(R.id.buttn);
        firstRun = new FirstRunDetected(MainActivity.this);
        if(firstRun.loadFirstRun()){
            Log.i("onCreate: ","first time" );
            firstRun.saveFirstRun();
            String[] Permissions ={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_CONTACTS,Manifest.permission.CAMERA};
            if(!hasPermissions(MainActivity.this,Permissions)){
                ActivityCompat.requestPermissions(MainActivity.this,Permissions,Permission_All);
            }
        }
//        else{
            Log.i("onCreate: ","second time");
            buttonReal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                      if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                          ActivityCompat.requestPermissions(MainActivity.this,
                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISO_LOCATION);
                      }else{
                          startApplicationDetailsActivity(getPackageName());
//                          startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                      }
                  }else{
                      permissionAllow();
                  }
                }
            });
//        }
    }
    public void permissionAllow(){
        Toast.makeText(getApplicationContext(),"Ya tienes el permiso",Toast.LENGTH_SHORT).show();
    }
    public static boolean hasPermissions(Context context,String... permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions!=null){
            for (String permission :permissions) {
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    private void startApplicationDetailsActivity(String packageName) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setComponent(intent.resolveActivity(this.getPackageManager()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Permission_All:
                boolean allPermissionsGranted = true;
                if(grantResults.length>0){
                    for(int grantResult: grantResults){
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                }
                if (allPermissionsGranted) {
                    // Permission Granted
                    Toast.makeText(getApplicationContext(),"Los permisos fueron permitido",Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Multiple Permissions");
                    builder.setMessage("Se requieren todos los permisos para que la aplicación funcione correctamente" +
                            ", por favor activarlos desde la configuración de su disositivo");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                break;
            case PERMISO_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getApplicationContext(),"Ya tienes el permiso prro",Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
