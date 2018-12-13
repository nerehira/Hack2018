package nerehira.proyect;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ver_detalles extends AppCompatActivity {

    SharedPreferences preferencias;
    TextView ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles);
        Log.i(null, "onCreate: Bienvenidos!!!");

        preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String ubicacion =preferencias.getString("ubicacion","sin informacion");
        String  tipo =preferencias.getString("tipo","sin informacion");
        String inventario =preferencias.getString("inventario","sin informacion");
        String  foto =preferencias.getString("foto","sin informacion");
        String lineas =preferencias.getString("lineas","sin informacion");
        String  horaparqueo =preferencias.getString("horaparqueo","sin informacion");
        //Toast.makeText(getApplicationContext(),ubicacion+" : "+ tipo+" : "+inventario+" : "+ foto+ " : "+lineas+" : "+ horaparqueo, Toast.LENGTH_LONG).show();
        ver=(TextView) findViewById(R.id.detalle);
        ver.setText("Ubicación: "+ubicacion+"\n"+
            "Tipo Señalización: "+tipo+"\n"+
            "Inventario: "+inventario+"\n"+
            "Foto: "+foto+"\n"+
            "Lineas: "+lineas+"\n"+
            "Horaparqueo: "+ horaparqueo);
    }
}
