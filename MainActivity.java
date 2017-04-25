package com.example.patrick.amostragemcontrole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Método que dará início ao servico de background.
    public void startService(View view) {
        //startService(new Intent(getBaseContext(), MyServiceSemThread.class));//Como aki eu invoco um metodo q nao foi implementado??? Ele pertence a Context.

        startService(new Intent(getBaseContext(), ServicoColetaDados.class));//Como aki eu invoco um metodo q nao foi implementado??? Ele pertence a Context.

    }

    // Metodo que parara o servico
    public void stopService(View view) {
        //stopService(new Intent(getBaseContext(), MyServiceSemThread.class));

        stopService(new Intent(getBaseContext(), ServicoColetaDados.class));//Como aki eu invoco um metodo q nao foi implementado??? Ele pertence a Context.
    }

}
