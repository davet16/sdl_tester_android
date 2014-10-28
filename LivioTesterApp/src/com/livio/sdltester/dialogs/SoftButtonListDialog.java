package com.livio.sdltester.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.livio.sdl.SdlImageItem;
import com.livio.sdl.SdlImageItem.SdlImageItemComparator;
import com.livio.sdl.adapters.SdlImageAdapter;
import com.livio.sdl.dialogs.BaseAlertDialog;
import com.livio.sdl.dialogs.BaseOkCancelDialog;
import com.livio.sdl.dialogs.TextViewOkCancelDialog;
import com.livio.sdltester.R;
import com.smartdevicelink.proxy.rpc.SoftButton;
import com.smartdevicelink.proxy.rpc.enums.FileType;


public class SoftButtonListDialog extends BaseOkCancelDialog{

    private static final String DIALOG_TITLE = "Create Soft Buttons";

    private static final int MAX_BUTTONS = 10;
    
    private List<SoftButton> softButtonItemList = new ArrayList<SoftButton>(MAX_BUTTONS);
    
    private Button but_addItem;
    private ListView lv_softButtonItems;
    private ArrayAdapter<SdlImageItem> adapter;
    private List<SdlImageItem> adapterList = new ArrayList<SdlImageItem>(MAX_BUTTONS);
    
    private BaseAlertDialog softButtonDialog = null;
    private List<SdlImageItem> allImages;
    
    public SoftButtonListDialog(Context context, List<SdlImageItem> images){
        super(context, DIALOG_TITLE, R.layout.create_choice_interaction_set);
        setPositiveButton(okButtonListener);
        adapter = new SdlImageAdapter(context, adapterList);
        lv_softButtonItems.setAdapter(adapter);
        this.allImages = images;
        createDialog();
    }

    @Override
    protected void findViews(View parent) {
        lv_softButtonItems = (ListView) view.findViewById(R.id.lv_choices);
        lv_softButtonItems.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                BaseAlertDialog deleteDialog = new TextViewOkCancelDialog(context, "Delete", "Do you want to delete this item?");
                deleteDialog.setListener(new BaseAlertDialog.Listener() {
                    @Override
                    public void onResult(Object resultData) {
                        boolean delete = (Boolean) resultData;
                        if(delete){
                            softButtonItemList.remove(position);
                            adapterList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                deleteDialog.show();
            }
        });
        but_addItem = (Button) view.findViewById(R.id.but_addItem);
        but_addItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftButtonDialog();
            }
        });
    }
    
    private void showSoftButtonDialog(){
        softButtonDialog = new SoftButtonItemDialog(context, allImages);
        softButtonDialog.setListener(new BaseAlertDialog.Listener() {
            @Override
            public void onResult(Object resultData) {
                SoftButton button = (SoftButton) resultData;
                addSoftButtonToList(button);
            }
        });
        softButtonDialog.show();
    }
    
    private void addSoftButtonToList(SoftButton button){
        softButtonItemList.add(button);
        
        if(button.getImage() != null){
            // if user selected an image, figure out which one they selected
            int imageIndex = Collections.binarySearch(allImages, new SdlImageItem(null, button.getImage().getValue(), null), new SdlImageItemComparator());
            if(imageIndex >= 0 && imageIndex < allImages.size()){
                SdlImageItem item = allImages.get(imageIndex);
                adapter.add(new SdlImageItem(item.getBitmap(), button.getText(), FileType.GRAPHIC_JPEG));
                adapter.notifyDataSetChanged();
            }
        }
        else{
            // if the user didn't select an image, we'll create an empty one to display in the adapter
            Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            adapter.add(new SdlImageItem(image, button.getText(), FileType.GRAPHIC_JPEG));
            adapter.notifyDataSetChanged();
        }
    }
    
    //dialog button listeners
    private final DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Vector<SoftButton> softButtonItems = new Vector<SoftButton>(softButtonItemList);
            
            if(softButtonItems.size() > 0){
                notifyListener(softButtonItems);
            }
        }
    };

}
