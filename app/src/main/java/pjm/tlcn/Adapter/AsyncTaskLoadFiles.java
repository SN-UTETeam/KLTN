package pjm.tlcn.Adapter;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

/**
 * Created by Pjm on 10/25/2017.
 */

public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

    File targetDirector;
    TabNews_ImageAdapter myTaskAdapter;

    public AsyncTaskLoadFiles(TabNews_ImageAdapter adapter) {
        myTaskAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera/";
        targetDirector = new File(targetPath);
        myTaskAdapter.clear();

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            publishProgress(file.getAbsolutePath());
            if (isCancelled()) break;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        myTaskAdapter.add(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
        myTaskAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }

}
