package com.example.patrick.amostragemcontrole;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by patrick on 12/01/17.
 */

public class Listener_de_Boot_Completo extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {//No Manifeste declaramos qu esta classe é um receiver (listener) para o sinal de boot concluído e para carregador desconectando.
        Log.d("BROADSCAST", "Foi chamado por broadcast");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("BROADSCAST", "Foi foi reconhecido broadcast de boot");
            Intent serviceIntent = new Intent(context, ServicoColetaDados.class);//invocando o serviço qque desejamos rodar ao boot
            context.startService(serviceIntent);//iremos invocá-lo a partir do contexto que nos foi passado pelo broadcast.

        }

    }
}
