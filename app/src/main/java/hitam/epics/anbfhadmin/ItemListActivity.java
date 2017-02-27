package hitam.epics.anbfhadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private ListView listView;
    private ArrayList<Item> items;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        String category = getIntent().getExtras().getString("category");

        if (category != null) {
            reference = FirebaseDatabase.getInstance().getReference("donation_items").child(category);
            listView = (ListView) findViewById(R.id.item_list);
            listView.setEmptyView(findViewById(R.id.empty_view));

            items = new ArrayList<>();
            adapter = new ItemAdapter(this, items);
            listView.setAdapter(adapter);
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Item item = dataSnapshot.getValue(Item.class);
                    items.add(item);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Item item = dataSnapshot.getValue(Item.class);
                    for (Item i : items) {
                        if (i.getName().equals(item.getName())) {
                            items.remove(i);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Item item = items.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemListActivity.this);
                    builder.setItems(new CharSequence[]{"Delete Item", "Cancel"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                reference.child(item.getName()).removeValue();
                            }
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    public void addItem(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }
}
