package my.navareno.max.wifiapp;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class LaptopServer {


    private static String string = "";
    public static String getString() {
        return string;
    }
    private static final String LOG_TAG = "myServerApp";
    // ip адрес сервера, который принимает соединения
    private String mServerName = "192.168.4.1";
    // номер порта, на который сервер принимает соединения
    private int mServerPort = 8080;
    // сокет, через которий приложения общается с сервером
    private Socket mSocket = null;
    public LaptopServer() {}

    /**
     * Открытие нового соединения. Если сокет уже открыт, то он закрывается.
     *
     * Если не удалось открыть сокет
     */
    public void openConnection() throws Exception {
        /* Освобождаем ресурсы */
        closeConnection();
        try {
 /*
 Создаем новый сокет. Указываем на каком компютере и порту запущен наш процес
 который будет принамать наше соединение.
 */
            mSocket = new Socket(mServerName,mServerPort);
        } catch (IOException e) {
            throw new Exception("Невозможно создать сокет: "+e.getMessage());
        }
    }

    /**
     * Метод для закрытия сокета, по которому мы общались.
     */
    public void closeConnection() {
        /* Проверяем сокет. Если он не зарыт, то закрываем его и освобдождаем соединение.*/
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Невозможно закрыть сокет: " + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
    }

    /**
     * Метод для отправки данных по сокету.
     *
     * @param data
     * Данные, которые будут отправлены
     * Если невозможно отправить данные
     */
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
            int count = mSocket.getInputStream().read(buffer, 0, buffer.length);
            string = new String(buffer, 0, count);

            Log.d("%%%^",string+"++++++  FG ");


        } catch (IOException e) {
            throw new Exception("Невозможно отправить данные: " + e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }





}
