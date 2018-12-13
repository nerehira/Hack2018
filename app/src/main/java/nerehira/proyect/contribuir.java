
package nerehira.proyect;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nerehira.proyect.Modelos.detalle;

public class contribuir extends AppCompatActivity {


    private ImageView imageView;
    private EditText ubicacion;
    private Spinner tipo;
    private EditText inventario;
    private EditText observacion;
    private Button enviar;

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Bitmap imageBitmap;
    private String encodeString="";
    private LocationManager mlocManager;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 1;
    private LocationManager locationManager;

    Context mContext;
    private LoginButton loginFacebook;
    private CallbackManager callbackManager;
    String tokenFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contribuir);
        mContext = this;
        imageView = (ImageView) findViewById(R.id.imageView);
        obtenerUbicacion();
        ubicacion = (EditText) findViewById(R.id.ubicacion);
        tipo=(Spinner)findViewById(R.id.tipo_sea);
        inventario=(EditText)findViewById(R.id.inventario);
        observacion=(EditText)findViewById(R.id.observacion);
        enviar=(Button)findViewById(R.id.enviar);
        tokenFacebook="";

         loginFacebook=(LoginButton)findViewById(R.id.login_button);
         loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              FacebookSdk.sdkInitialize(getApplicationContext());
                callbackManager=CallbackManager.Factory.create();
                loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                       // Toast.makeText(getApplicationContext()," "+loginResult.getAccessToken().getUserId()+"\n"+loginResult.getAccessToken().getToken(),Toast.LENGTH_LONG).show();
                       // Toast.makeText(getApplicationContext()," "+loginResult.getAccessToken().getToken(),Toast.LENGTH_LONG).show();
                        tokenFacebook=loginResult.getAccessToken().getUserId();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),"cancelado",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void enviarForm(View view) {
        if(validar()){

            detalle detalles=new detalle();
            detalles.setLocation(ubicacion.getText().toString());
            detalles.setType(tipo.getSelectedItem().toString());
            detalles.setId(inventario.getText().toString());
            detalles.setStatus(observacion.getText().toString());
            detalles.setPhoto(encodeString);


            String s=  JSON.toJSONString(detalles);
            //String parkeo=detalles.getParking().toString();
            //JSON.parseObject(s8, ModeloEncuestas.class);
            JSONObject  myJsonjObject = null;

            try {
                myJsonjObject = new JSONObject(s);
                final String s5= getDataString(jsonToMap(myJsonjObject));

                new Thread(new Runnable() {
                    public void run() {

                        doInBackground(s5);
                    }
                }).start();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    protected String doInBackground(String... params) {

        String urlString = "https://innovahack.azurewebsites.net/contribute";

        URL url = null;
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            //  String data = URLEncoder.encode(String.valueOf(params), "UTF-8");



            urlConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(params[0]);
            wr.flush();

            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
            String result = reader.readLine();
           //Toast.makeText(getApplicationContext(),""+result.toString(),Toast.LENGTH_LONG).show();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Intent a = new Intent(this,MainActivity.class);
                startActivity(a);
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Result", "SLEEP ERROR");
        }
        return null;
    }

    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public static HashMap<String, String> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, String> retMap = new HashMap<String, String>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, (String) value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public boolean validar(){
        if(ubicacion.getText().length()!=0 &&
                !tipo.getSelectedItem().equals("Ninguno") &&
                inventario.getText().length()!=0 &&
                observacion.getText().length()!=0 && !tokenFacebook.equals("")){
            return true;
        }
        return false;
    }

    public void obtenerUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

    }

    //////////////Actualizando ubicacion

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            // actualizarUbicacion(location);
            if (location != null) {
                Double lat = location.getLatitude();
                Double longi = location.getLongitude();
                // Toast.makeText(getApplicationContext(),"ubicacion: "+String.valueOf(lat)+" : "+String.valueOf(longi),Toast.LENGTH_LONG).show();
                ubicacion.setText(String.valueOf(lat) + "," + String.valueOf(longi));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }
    };

//////////////////////////////////////////////////////////////////////////



    public void capturarFoto(View view) {
        // verifico si el usuario dio los permisos para la camara
        if(!tokenFacebook.equals("")){
            if (ActivityCompat.checkSelfPermission(contribuir.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.CAMERA)) ;
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return;
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else{Toast.makeText(getApplicationContext(),"please log in facebook",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if(tokenFacebook.equals("")){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else {

           if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
               Bundle extras = data.getExtras();
               imageBitmap = (Bitmap) extras.get("data");
               imageView.setImageBitmap(imageBitmap);
               ByteArrayOutputStream baos = null;
               byte[] baat = null;
               baos = new ByteArrayOutputStream();
               imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
               baat = baos.toByteArray();
               encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
               //String[] s=encodeString.split("\n");
               encodeString = encodeString.substring(0,encodeString.length()-2);
               //encodeString=s[0];
               //Toast.makeText(getApplicationContext(), "code:" + encodeString, Toast.LENGTH_LONG).show();
           }
       }



    }



}
