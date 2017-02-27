package hitam.epics.anbfhadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private EditText ItemNameEditText;
    private EditText ItemPriceEditText;
    private ImageView ItemImageView;
    private StorageReference mStorageRef;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        database = FirebaseDatabase.getInstance();
        category = getIntent().getExtras().getString("category");
        reference = database.getReference("donation_items").child(category);

        ItemNameEditText = (EditText) findViewById(R.id.add_item_name);
        ItemPriceEditText = (EditText) findViewById(R.id.add_item_price);
        ItemImageView = (ImageView) findViewById(R.id.item_image);
    }

    public void additem(View view) {
        final String ItemName = ItemNameEditText.getText().toString().trim();
        String ItemPrice = ItemPriceEditText.getText().toString().trim();
        if (ItemName.length() == 0) {
            ItemNameEditText.setError("Required");
            return;
        }
        if (ItemPrice.length() == 0) {
            ItemPriceEditText.setError("Required");
            return;
        }
        findViewById(R.id.add_item_main).setVisibility(View.GONE);
        findViewById(R.id.process).setVisibility(View.VISIBLE);
        Item item = new Item(ItemName, Float.parseFloat(ItemPrice));
        reference.child(ItemName).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                uploadPic(ItemName);
            }
        });
    }

    public void selectPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "false");
        intent.putExtra("scale", "false");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap itemPic = extras.getParcelable("data");
                ItemImageView.setImageBitmap(itemPic);
            } else {
                Log.e("onActivityResult: ", "error");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadPic(final String ItemName) {
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference ProfilePicRef = mStorageRef.child("donation_items/" + category + "/" + ItemName + ".jpg");

        ItemImageView.setDrawingCacheEnabled(true);
        ItemImageView.buildDrawingCache();
        Bitmap bitmap = ItemImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ProfilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddItemActivity.this, "Error uploading picture", Toast.LENGTH_SHORT).show();
                findViewById(R.id.add_item_main).setVisibility(View.VISIBLE);
                findViewById(R.id.process).setVisibility(View.GONE);
                Log.e("onFailure: ", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this, "Item Added to database", Toast.LENGTH_SHORT).show();
                Uri photoUrl = taskSnapshot.getDownloadUrl();
                reference.child(ItemName).child("pictureurl").setValue(photoUrl.toString());
                AddItemActivity.this.finish();
            }
        });
    }
}
