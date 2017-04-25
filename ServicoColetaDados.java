package com.example.patrick.amostragemcontrole;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Double.parseDouble;

/**
 * Created by patrick on 24/03/17.
 */

public class ServicoColetaDados extends Service {

    final Handler handler = new Handler();
    final AquisicaoSensores info = new AquisicaoSensores(this);

    FileWriter escritor;

    Runnable runnableCode;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Coletando Started", LENGTH_LONG).show();

        int id = 1;

        NotificationManager mNotifyManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
        mBuilder.setContentTitle("Rodando Amostragem Controle").setSmallIcon(R.mipmap.ic_launcher);

        //mNotifyManager.notify(id, mBuilder.build());

        startForeground(id, mBuilder.build());

        info.getInfo();

        runnableCode = new Runnable() {

            private int contador = 0;
            private int contadorDeLongoPrazo = 0;

            @Override
            public void run() {

                Log.v("SERVICO", "O ServicoColetaDados foi chamado. Contador: " + contador + "  Contador De Longo Prazo: " + contadorDeLongoPrazo);

                File arquivoEixoX = new File(Environment.getExternalStorageDirectory().toString() + "/" + "Controle_Eixo_X_Tempo.txt");
                File arquivoEixoY = new File(Environment.getExternalStorageDirectory().toString() + "/" + "Controle_Eixo_Y_Bateria.txt");

                if(++contador<1) {
                    handler.postDelayed(this, 1000);//O serviço se repete múltiplas vezes seguidas para garantir que estamos recebendo uma leitura correta dos sensores.

                } else if(++contadorDeLongoPrazo<1000000000){//Após sucessivas repetições, aguardamos um longo período de tempo para realizar uma nova amostragem.

//=========================DADOS PARA A REGRESSÃO LINEAR========================================================================================================================================================"

                    try {

                        Log.v("MMQ", "Verificando se está descarregando.");
                        if(info.getStatusString().equals("Discharging")) {//Só coleta pontos para a regressão de consumo se a bateria estiver sendo usada como alimentação.
                            Log.v("MMQ", "Escrevendo mais um ponto de amostragem nos arquivos vetores.");

                            arquivoEixoY.createNewFile();//Garantindo que o arquivo existe.
                            escritor = new FileWriter(arquivoEixoY, true);
                            escritor.write("" + info.getLevel() + "\n");//Estamos normalizando o level de bateria no momento de retirada do carregador no valor máximo para facilitar a regressão.
                            escritor.close();

                            arquivoEixoX.createNewFile();//Garantindo que o arquivo existe.
                            escritor = new FileWriter(arquivoEixoX, true);
                            escritor.write("" + System.currentTimeMillis() + "\n");//Esses -10800000 são para converter o fuso horário para o horário de brasília.
                            escritor.close();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//==================================FIM DA OBTENÇÃO===============================================================================================================================================

                    contador = 0;//Reiniciamos o contador de amostragem.
                    handler.postDelayed(this, 150000);// 2,5 minutos.
                }else{
                    onDestroy();
                }
            }
        };

        handler.post(runnableCode);

        return START_STICKY;
    }

    private void desligaSensores(){//Este métoddo permite ao celular desligar os sensores e GPS para poupar energia.
        if(info != null) info.onDestroy();//Deixa de requisitar atualizações ao sistema e remove os listener. Economiza energia e evita relatório de erros.
    }

    @Override
    public void onDestroy() {

        desligaSensores();
        Toast.makeText(this, "Service Destroyed", LENGTH_LONG).show();
        handler.removeCallbacks(runnableCode);//Retira todas as chamadas agendadas deste serviço.
        super.onDestroy();

    }

}
