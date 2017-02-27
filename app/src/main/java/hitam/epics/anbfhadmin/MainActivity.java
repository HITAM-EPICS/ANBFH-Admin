package hitam.epics.anbfhadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewGroceries(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("category", "grocery");
        startActivity(intent);
    }

    public void viewCleaningProducts(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("category", "cleaningProducts");
        startActivity(intent);
    }

    public void viewToiletries(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("category", "toiletries");
        startActivity(intent);
    }
}
