package my.navareno.max.wifiapp;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ViewAxisActivity extends AppCompatActivity {
    private ViewAxisActivity mServer = null;
    private Socket mSocket = null;
    private static String string = "";
    private static String[] subStr;
    private static int[] sumAxis = new int[10];
    private static boolean flagStar = true;
    private static boolean flagStarWIFI = true;

    private final int weightTruck=12560;
    private final int absoluteZeroTruck=3950;
    private final int weightTrailer=5620;
    private final int absoluteZeroTrailer=2400;
    private final double coeffTruck=2.055;
    private final double coeffTrailer=2.125;

    private Thread thread;
    private Thread thread1;
    private TextView viewConnect;
    private TextView viewAxisLeft1;
    private TextView viewAxisLeft2;
    private TextView viewAxisLeft3;
    private TextView viewAxisLeft4;
    private TextView viewAxisLeft5;
    private TextView viewAxisRight1;
    private TextView viewAxisRight2;
    private TextView viewAxisRight3;
    private TextView viewAxisRight4;
    private TextView viewAxisRight5;
    private TextView viewAxisSum1;
    private TextView viewAxisSum2;
    private TextView viewAxisSum3;
    private TextView viewAxisSum4;
    private TextView viewAxisSum5;
    private TextView weightAllTextN;
    private TextView weightTruckTextN;
    private TextView weightTrailerTextN;
    private TextView weightAllTextB;
    private TextView weightTruckTextB;
    private TextView weightTrailerTextB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_axis);
        viewConnect = findViewById(R.id.viewConnect);
        viewAxisLeft1 = findViewById(R.id.viewAxisLeft1);
        viewAxisLeft2 = findViewById(R.id.viewAxisLeft2);
        viewAxisLeft3 = findViewById(R.id.viewAxisLeft3);
        viewAxisLeft4 = findViewById(R.id.viewAxisLeft4);
        viewAxisLeft5 = findViewById(R.id.viewAxisLeft5);
        viewAxisRight1 = findViewById(R.id.viewAxisRight1);
        viewAxisRight2 = findViewById(R.id.viewAxisRight2);
        viewAxisRight3 = findViewById(R.id.viewAxisRight3);
        viewAxisRight4 = findViewById(R.id.viewAxisRight4);
        viewAxisRight5 = findViewById(R.id.viewAxisRight5);
        viewAxisSum1 = findViewById(R.id.viewAxisSum1);
        viewAxisSum2 = findViewById(R.id.viewAxisSum2);
        viewAxisSum3 = findViewById(R.id.viewAxisSum3);
        viewAxisSum4 = findViewById(R.id.viewAxisSum4);
        viewAxisSum5 = findViewById(R.id.viewAxisSum5);
        weightAllTextN = findViewById(R.id.weightAllTextN);
        weightTruckTextN = findViewById(R.id.weightTruckTextN);
        weightTrailerTextN = findViewById(R.id.weightTrailerTextN);
        weightAllTextB = findViewById(R.id.weightAllTextB);
        weightTruckTextB = findViewById(R.id.weightTruckTextB);
        weightTrailerTextB = findViewById(R.id.weightTrailerTextB);
        mServer = new ViewAxisActivity();



        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ViewAxisActivity.hasConnection(ViewAxisActivity.this)) {
                        if (flagStarWIFI) {
                            flagStarWIFI = false;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
//                    mServer.openConnection();
//                    Log.d("TT$", ViewAxisActivity.hasConnection(ViewAxisActivity.this)+"55655511");
                                        try {
                                            try {
                                                mServer.openConnection();
                                                while (true) {
//                                Log.d("TT$", ViewAxisActivity.hasConnection(ViewAxisActivity.this)+"556555");
//                                Thread.sleep(1000);
                                                    if (ViewAxisActivity.hasConnection(ViewAxisActivity.this)) {
                                                        runOnUiThread(runn1);
                                                        String delimeter = "\n"; // Разделитель
                                                        subStr = string.split(delimeter);
//                                    String[] numberOnly = new String[subStr.length];
                                                        if (subStr.length > 2) {
                                                            for (int i = 0; i < subStr.length; i++) {
                                                                subStr[i] = subStr[i].replaceAll("[^0-9]", "");
                                                            }
                                                        }

                                                        if (flagStar) {
                                                            mServer.sendData("start".getBytes());
                                                            flagStar = false;

                                                        }
                                                        if (string.contains("sgkjhcxk")) {


                                                            mServer.sendData("sensors:10\r\n".getBytes());
                                                        }
                                                        if (string.contains("expect")) {


                                                            mServer.sendData("give\r\n".getBytes());

                                                        }
                                                        if (string.contains("quantitySens")) {


                                                            mServer.sendData("expect:5\r\n".getBytes());

                                                        }
                                                        if (subStr[0].matches("\\d+")) {
                                                            Thread.sleep(1000);

                                                            mServer.sendData("give\r\n".getBytes());
                                                        }


                                                    } else {
                                                        runOnUiThread(runn1);
                                                        mSocket = null;
                                                        break;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e) {

                                        }
                                    } catch (Exception e) {
//                    mServer = null;
                                    }
                                }
                            }).start();
                        }
                    }
                    runOnUiThread(runn1);
                }
            }
        }).start();

    }


    Runnable runn1 = new Runnable() {
        public void run() {

            if (ViewAxisActivity.hasConnection(ViewAxisActivity.this)) {

                viewConnect.setTextColor(ContextCompat.getColor(ViewAxisActivity.this, R.color.blue));

                viewConnect.setText("Подключено");

                if (subStr != null && subStr.length > 2) {
                    for (int j = 0; j < subStr.length; j++) {
                                String s = subStr[j].replaceAll("[^0-9]", "");
                                int weight = Integer.parseInt(s);
                                int weightNumberSensor = (0xffff0000 & weight) >> 16;
                                int weightData = 0x0000ffff & weight;

//                                weightData = (int) (-862.5 + 1.725 * weightData);
                                if (weightNumberSensor == 1) {
                                    viewAxisLeft1.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 2) {
                                    viewAxisRight1.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 3) {
                                    viewAxisLeft2.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 4) {
                                    viewAxisRight2.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 5) {
                                    viewAxisLeft3.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 6) {
                                    viewAxisRight3.setText(weightData+"");
                                    sumAxis[weightNumberSensor-1]=weightData;
                                }
                                if (weightNumberSensor == 7) {
                                    sumAxis[weightNumberSensor-1]=weightData;
                                    weightData=(int)(weightData*coeffTrailer);
                                    viewAxisLeft4.setText(weightData+"");
                                }
                                if (weightNumberSensor == 8) {
                                    sumAxis[weightNumberSensor-1]=weightData;
                                    weightData=(int)(weightData*coeffTrailer);
                                    viewAxisRight4.setText(weightData+"");
                                }
                                if (weightNumberSensor == 9) {
                                    sumAxis[weightNumberSensor-1]=weightData;
                                    weightData=(int)(weightData*coeffTrailer);
                                    viewAxisLeft5.setText(weightData+"");
                                }
                                if (weightNumberSensor == 10) {
                                    sumAxis[weightNumberSensor-1]=weightData;
                                    weightData=(int)(weightData*coeffTrailer);
                                    viewAxisRight5.setText(weightData+"");
                                }
                        if (sumAxis[0] != 0 && sumAxis[1] != 0) {
                            viewAxisSum1.setText(sumAxis[0]+sumAxis[1]+"");
                        }
                        if (sumAxis[2] != 0 && sumAxis[3] != 0) {
                            viewAxisSum2.setText(sumAxis[2]+sumAxis[3]+"");
                        }
                        if (sumAxis[4] != 0 && sumAxis[5] != 0) {
                            viewAxisSum3.setText(sumAxis[4]+sumAxis[5]+"");
                        }
                        if (sumAxis[6] != 0 && sumAxis[7] != 0) {
                            viewAxisSum4.setText((int)((sumAxis[6]+sumAxis[7]+1200)*coeffTrailer)+"");
                        }
                        if (sumAxis[8] != 0 && sumAxis[9] != 0) {
                            viewAxisSum5.setText((int)((sumAxis[6]+sumAxis[7]+1200)*coeffTrailer)+"");
                        }

                        if (sumAxis[2] != 0 && sumAxis[3] != 0 && sumAxis[4] != 0 && sumAxis[5] != 0) {
                            if ((sumAxis[2]+sumAxis[3]+sumAxis[4]+sumAxis[5]) < absoluteZeroTruck) {
                                weightTruckTextN.setText(0+"");
                                weightTruckTextB.setText(weightTruck+"");
                            }
                            else {
                                weightTruckTextN.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck) + "");
                                weightTruckTextB.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck)+weightTruck + "");
                            }
                        }
                        if (sumAxis[6] != 0 && sumAxis[7] != 0 && sumAxis[8] != 0 && sumAxis[9] != 0) {
                            if ((sumAxis[6]+sumAxis[7]+sumAxis[8]+sumAxis[9]) < absoluteZeroTrailer) {
                                weightTrailerTextN.setText(0+"");
                                weightTrailerTextB.setText(weightTrailer+"");
                            }
                            else {
                                weightTrailerTextN.setText((int)(((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer) + "");
                                weightTrailerTextB.setText((int)(((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer)+weightTrailer + "");
                            }
                        }
                        if (sumAxis[0] != 0 && sumAxis[1] != 0 && sumAxis[2] != 0 && sumAxis[3] != 0
                                && sumAxis[4] != 0 && sumAxis[5] != 0 && sumAxis[6] != 0
                                && sumAxis[7] != 0 && sumAxis[8] != 0 && sumAxis[9] != 0) {
                            if ((sumAxis[2]+sumAxis[3]+sumAxis[4]+sumAxis[5]) < absoluteZeroTruck && (sumAxis[6]+sumAxis[7]+sumAxis[8]+sumAxis[9]) < absoluteZeroTrailer){
                                weightAllTextN.setText(0+"");
                                weightAllTextB.setText(weightTruck+weightTrailer+"");
                            }
                            else if ((sumAxis[2]+sumAxis[3]+sumAxis[4]+sumAxis[5]) < absoluteZeroTruck) {
                                weightAllTextN.setText((int)(((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer)+"");
                                weightAllTextB.setText((int)(((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer)+weightTrailer+"");
                            }
                            else if ((sumAxis[6]+sumAxis[7]+sumAxis[8]+sumAxis[9]) < absoluteZeroTrailer) {
                                weightAllTextN.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck)+"");
                                weightAllTextB.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck)+weightTruck+"");
                            }
                            else {
                                weightAllTextN.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck)+
                                        (((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer)+"");
                                weightAllTextB.setText((int)(((sumAxis[2] + sumAxis[3] + sumAxis[4] + sumAxis[5])-absoluteZeroTruck) * coeffTruck)+
                                        (((sumAxis[6] + sumAxis[7] + sumAxis[8] + sumAxis[9])-absoluteZeroTrailer) * coeffTrailer)+weightTruck+weightTrailer+"");
                            }

                        }

                    }
                }
            } else {
                viewConnect.setTextColor(Color.RED);
                viewConnect.setText("Отключено");
            }
        }
    };

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        else flagStarWIFI = true;
//        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (wifiInfo != null && wifiInfo.isConnected())
//        {
//            return true;
//        }
//        wifiInfo = cm.getActiveNetworkInfo();
//        if (wifiInfo != null && wifiInfo.isConnected())
//        {
//            return true;
//        }
        return false;
    }

    public void openConnection() throws Exception {

        /* Освобождаем ресурсы */
        closeConnection();

        try {
        /*
            Создаем новый сокет. Указываем на каком компютере и порту запущен наш процесс,
            который будет принамать наше соединение.
        */
            mSocket = new Socket(InetAddress.getByName("192.168.4.1"), 8080);
//            52.59.227.6 192.168.0.106
        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: " + e.getMessage());
        }
    }


    public void closeConnection() {

        /* Проверяем сокет. Если он не зарыт, то закрываем его и освобдождаем соединение.*/
        if (mSocket != null && !mSocket.isClosed()) {

            try {
                mSocket.close();
            } catch (IOException e) {

            } finally {
                mSocket = null;
            }

        }
        mSocket = null;
    }


    public void sendData(byte[] data) throws Exception {

        /* Проверяем сокет. Если он не создан или закрыт, то выдаем исключение */
        if (mSocket == null || mSocket.isClosed()) {
            throw new Exception("Невозможно отправить данные. Сокет не создан или закрыт");
        }

        /* Отправка данных */
        byte[] buffer = new byte[1024 * 4];
        try {
            mSocket.getOutputStream().write(data);
            mSocket.getOutputStream().flush();
            Thread.sleep(1000);
            int count = mSocket.getInputStream().read(buffer, 0, buffer.length);
            string = new String(buffer, 0, count);


        } catch (IOException e) {
            throw new Exception("Невозможно отправить данные: " + e.getMessage());
        }
    }
}
