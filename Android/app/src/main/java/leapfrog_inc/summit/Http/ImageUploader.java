package leapfrog_inc.summit.Http;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import leapfrog_inc.summit.Function.Constants;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class ImageUploader extends AsyncTask<ImageUploader.ImageUploaderParameter, Integer, Object> {

    class ImageUploaderParameter {
        public String userId;
        public Bitmap bitmap;
    }

    private ImageUploaderCallback mCallback = null;

    public ImageUploader(ImageUploaderCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(ImageUploaderParameter... params) {

        ImageUploaderParameter parameter = params[0];

        HttpURLConnection conn = null;
        String result = "";

        try {
            URL url = new URL(Constants.ServerApiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Constants.HttpReadTimeout);
            conn.setConnectTimeout(Constants.HttpConnectTimeout);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=boundary");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            setOutputStream(conn, parameter);

            conn.connect();

            int resp = conn.getResponseCode();
            if ((int)(resp / 100) == 2) {
                InputStream stream = conn.getInputStream();
                result = streamToString(stream);
            }
        } catch (Exception e) {
            result = null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private void setOutputStream(HttpURLConnection connection, ImageUploaderParameter param) {

        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            byte[] byteArray;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            param.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();

            String command = "uploadUserImage";
            outputStream.writeBytes("--boundary\r\nContent-Disposition: form-data; name=\"command\"\r\n\r\n" + command + "\r\n");
            outputStream.writeBytes("--boundary\r\nContent-Disposition: form-data; name=\"userId\"\r\n\r\n" + param.userId + "\r\n");
            outputStream.writeBytes("--boundary\r\nContent-Disposition: form-data; name=\"image\"; filename=\"image\"\r\nContent-Type: image/png\r\n\r\n");
            for (int i = 0; i < byteArray.length; i++) {
                outputStream.writeByte(byteArray[i]);
            }

            outputStream.writeBytes("\r\n\r\n--boundary--\r\n\r\n");

        } catch (Exception e) {}
    }

    private String streamToString(InputStream stream) {

        try {
            StringBuffer buffer = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufReder = new BufferedReader(reader);
            String line = "";
            while ((line = bufReder.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(line);
            }
            stream.close();

            return buffer.toString();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);

        if (data instanceof String) {
            if (data == "0") {
                mCallback.didReceive(true);
                return;
            }
        }
        mCallback.didReceive(false);
    }

    public interface ImageUploaderCallback {
        void didReceive(boolean result);
    }
}
