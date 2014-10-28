package com.livio.sdltester.dialogs;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.livio.sdl.IdGenerator;
import com.livio.sdl.SdlImageItem;
import com.livio.sdl.dialogs.BaseAlertDialog;
import com.livio.sdl.dialogs.BaseOkCancelDialog;
import com.livio.sdl.dialogs.ImageListDialog;
import com.livio.sdl.enums.SdlSystemAction;
import com.livio.sdl.utils.AndroidUtils;
import com.livio.sdl.utils.SdlUtils;
import com.livio.sdltester.R;
import com.smartdevicelink.proxy.rpc.SoftButton;


public class SoftButtonItemDialog extends BaseOkCancelDialog {

    private static final String DIALOG_TITLE = "Create a Soft Button";
    private List<SdlImageItem> availableImages;
    
    private EditText et_softButton_name, et_imageName;
    private CheckBox cb_hasImage, cb_isHighlighted;
    private Spinner spin_systemAction;
    private BaseAlertDialog imagesDialog;

    public SoftButtonItemDialog(Context context, List<SdlImageItem> availableImages){
        super(context, DIALOG_TITLE, R.layout.soft_button_item);
        spin_systemAction.setAdapter(AndroidUtils.createSpinnerAdapter(context, SdlSystemAction.values()));
        setPositiveButton(okButton);
        this.availableImages = availableImages;
        if(availableImages == null || availableImages.size() <= 0){
            cb_hasImage.setVisibility(View.GONE);
            et_imageName.setVisibility(View.GONE);
        }
        createDialog();
    }

    @Override
    protected void findViews(View parent){
        et_softButton_name = (EditText) parent.findViewById(R.id.et_softbutton_text);
        et_imageName = (EditText) parent.findViewById(R.id.et_softbutton_imageName);
        cb_isHighlighted = (CheckBox) parent.findViewById(R.id.check_enable_highlight);
        spin_systemAction = (Spinner) parent.findViewById(R.id.spin_softbutton_actions);
        cb_hasImage = (CheckBox) parent.findViewById(R.id.check_enable_image);
        cb_hasImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showImagesDialog();
                }
                else{
                    et_imageName.setText("");
                }
            }
        });
    }
    
    private void showImagesDialog(){
        if(imagesDialog == null){
            imagesDialog = new ImageListDialog(context, availableImages);
            imagesDialog.setListener(new BaseAlertDialog.Listener() {
                @Override
                public void onResult(Object resultData) {
                    SdlImageItem selectedItem = (SdlImageItem) resultData;
                    if(selectedItem != null){
                        et_imageName.setText(selectedItem.getImageName());
                    }
                }
            });
        }
        
        imagesDialog.show();
    }
    
    private DialogInterface.OnClickListener okButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String softButtonName = et_softButton_name.getText().toString().trim();
            String imageName = et_imageName.getText().toString().trim();
            boolean isHighlighted = cb_isHighlighted.isChecked();
            SdlSystemAction action = (SdlSystemAction) spin_systemAction.getSelectedItem();
            
            if(softButtonName.length() > 0 || imageName.length() > 0){
                SoftButton result = SdlUtils.createSoftButton(softButtonName, imageName, isHighlighted, action);
                result.setSoftButtonID(IdGenerator.next());
                notifyListener(result);
            }
            else{
                Toast.makeText(context, "SoftButton must have either a name or an image.", Toast.LENGTH_LONG).show();
            }
        }
    };

}
