package com.example.max.wifiapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "myServerApp";
    private Button mButtonOpen = null;
    private Button mButtonGive = null;
    private Button mButtonClose = null;
    private Button mButtonSensors = null;
    private Button mButtonExpect = null;
    private LaptopServer mServer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonOpen = (Button) findViewById(R.id.button_open_connection);
        mButtonGive = (Button) findViewById(R.id.button_give_connection);
        mButtonSensors = (Button) findViewById(R.id.button_sensors_connection);
        mButtonExpect = (Button) findViewById(R.id.button_expect_connection);
        mButtonClose = (Button) findViewById(R.id.button_close_connection);
        mButtonGive.setEnabled(false);
        mButtonSensors.setEnabled(false);
        mButtonExpect.setEnabled(false);
        mButtonClose.setEnabled(false);


        mButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* создаем объект для работы с сервером*/
                mServer = new LaptopServer();
                /* Открываем соединение. Открытие должно происходить в отдельном потоке от ui */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mServer.openConnection();
 /*
 устанавливаем активные кнопки для отправки данных
 и закрытия соедиения. Все данные по обновлению интерфеса должны
 обрабатывается в Ui потоке, а так как мы сейчас находимся в
 отдельном потоке, нам необходимо вызвать метод runOnUiThread()
 */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mButtonGive.setEnabled(true);
                                    mButtonSensors.setEnabled(true);
                                    mButtonExpect.setEnabled(true);
                                    mButtonClose.setEnabled(true);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                            mServer = null;
                        }
                    }
                }).start();
            }
        });


        mButtonGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ViewAxisActivity.class));
                if (mServer == null) {
                    Log.e(LOG_TAG, "Сервер не создан");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /* отправляем на сервер данные */
                            mServer.sendData("give\r\n".getBytes());
                            String delimeter = "\n"; // Разделитель
                            String[] subStr = LaptopServer.getString().split(delimeter);
                            String[] subStrNew = new String[subStr.length];
                            for (String x:subStrNew
                                 ) {
                                Log.d(LOG_TAG, LaptopServer.getString()+""+x+"++++++++++++");
                            }


                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }
                    }
                }).start();
            }
        });
        mButtonSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServer == null) {
                    Log.e(LOG_TAG, "Сервер не создан");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /* отправляем на сервер данные */
                            Log.d(LOG_TAG, "mButtonSensors");
                            mServer.sendData("sensors:2\r\n".getBytes());
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }
                    }
                }).start();
            }
        });
        mButtonExpect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServer == null) {
                    Log.e(LOG_TAG, "Сервер не создан");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /* отправляем на сервер данные */
                            Log.d(LOG_TAG, "mButtonExpect");
                            mServer.sendData("expect:2\r\n".getBytes());
                        } catch (Exception e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }
                    }
                }).start();
            }
        });

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Закрываем соединение */
                mServer.closeConnection();
                /* устанавливаем неактивными кнопки отправки и закрытия */
                mButtonGive.setEnabled(false);
                mButtonSensors.setEnabled(false);
                mButtonExpect.setEnabled(false);
                mButtonClose.setEnabled(false);
            }
        });






    }



}
