package com.github.ristoautio.stunningparakeet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @ViewById
    TextView tvHello;

    @ViewById
    TextView tvFileData;

    @ViewById
    Toolbar toolbar;

    @ViewById
    FloatingActionButton fab;

    private File fileHolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void bindActionBar() {
        Log.d(TAG, "bindActionBar: afterviews");
        setSupportActionBar(toolbar);

        if(fileHolder == null){
            fab.setVisibility(View.INVISIBLE);
            tvHello.setText(getString(R.string.no_file_selected));
            tvFileData.setText("");
        }

        Intent intent = getIntent();
        if(intent.getAction().compareTo(Intent.ACTION_VIEW) == 0){

            Log.d(TAG, "action: " + intent.getAction());
            Log.d(TAG, "dataString: " + intent.getDataString());
            Log.d(TAG, "mimetype: " + intent.getType());

            // TODO refactor
            String[] permissions = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
            };
            int requestCode = 200;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }


            if (intent.getType().contentEquals("application/pdf")) {
                try {
                    parsePdf(intent);
                } catch (Exception e) {
                    Log.d(TAG, "bindActionBar: error parsing");
                }
            } else {
                Log.d(TAG, "bindActionBar: not a pdf");
            }
        }

    }

    private void parsePdf(Intent intent) throws URISyntaxException, IOException {
        Log.d(TAG, "bindActionBar: open das file at: " + intent.getDataString());
        File file = new File(new URI(intent.getDataString()));
        fileHolder = file;
        Log.d(TAG, "bindActionBar: found file " + file.getName());
        PDFBoxResourceLoader.init(getApplicationContext());

        Log.d(TAG, "bindActionBar: load document");
        PDDocument document = PDDocument.load(file);
        PDDocumentInformation info = document.getDocumentInformation();
        Log.d(TAG, "bindActionBar: " + info.getAuthor());

        Set<String> keys = info.getMetadataKeys();
        if (!keys.isEmpty()) {
            Log.d(TAG, "has metakeys");
            tvHello.setText(file.getName());
            StringBuilder builder = new StringBuilder();
            for (String key : keys) {
                CharSequence text = tvFileData.getText();
                Log.d(TAG, "parsePdf: current text: " + text);
                Log.d(TAG, key + " -- " + (String) info.getPropertyStringValue(key));
                builder.append(key)
                        .append(": \t")
                        .append((String) info.getPropertyStringValue(key))
                        .append("\n");
                tvFileData.setText(builder.toString());
            }
        }
    }

    private void openFile() {
        Uri uri = Uri.fromFile(fileHolder);
        String mime = getContentResolver().getType(uri);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Click(R.id.fab)
    void onFabClick() {
        Log.d(TAG, "onFabClick: clicked fab");
        if (fileHolder != null) {
            openFile();
        }else{
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
