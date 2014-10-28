package com.livio.sdltester.dialogs;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.livio.sdl.SdlConstants;
import com.livio.sdl.SdlImageItem;
import com.livio.sdl.SdlRequestFactory;
import com.livio.sdl.dialogs.BaseAlertDialog;
import com.livio.sdl.dialogs.BaseOkCancelDialog;
import com.livio.sdl.enums.SdlCommand;
import com.livio.sdl.utils.MathUtils;
import com.livio.sdl.viewhelpers.SeekBarCalculator;
import com.livio.sdltester.R;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.rpc.SoftButton;

public class ScrollableMessageDialog extends BaseOkCancelDialog {

	private static final SdlCommand SYNC_COMMAND = SdlCommand.SCROLLABLE_MESSAGE;
	private static final String DIALOG_TITLE = SYNC_COMMAND.toString();
	
	private static final int TIMEOUT_DEFAULT = 30;
	private static final int TIMEOUT_MIN = SdlConstants.ScrollableMessageConstants.TIMEOUT_MINIMUM;
	private static final int TIMEOUT_MAX = SdlConstants.ScrollableMessageConstants.TIMEOUT_MAXIMUM;
	private static final int LENGTH_MAX = SdlConstants.ScrollableMessageConstants.MESSAGE_LENGTH_MAX;
	
	private EditText et_scrollableMessage_text;
	private TextView tv_timeout;
	private SeekBar seek_timeout;
	private String timeoutBaseStr;
	private CheckBox softButtonsIncluded;
	private SeekBarCalculator progressCalculator;
	
	private List<SoftButton> softButtons;
	
	public ScrollableMessageDialog(Context context) {
		this(context, null);
	}
	
	public ScrollableMessageDialog(final Context context, final List<SdlImageItem> images){
	    super(context, DIALOG_TITLE, R.layout.scrollable_message);
	    softButtonsIncluded.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    BaseAlertDialog softButtonDialog = new SoftButtonListDialog(context, images);
                    softButtonDialog.setListener(new BaseAlertDialog.Listener(){
                        @SuppressWarnings("unchecked")
                        @Override
                        public void onResult(Object resultData){
                            softButtons = (List<SoftButton>) resultData;
                        }
                    });
                    softButtonDialog.show();
                }
                else{
                    softButtons = null;
                }
            }
        });
        setPositiveButton(okButtonListener);
        createDialog();
	}

	@Override
	protected void findViews(View parent) {
		timeoutBaseStr = context.getResources().getString(R.string.timeout);
		progressCalculator = new SeekBarCalculator(TIMEOUT_MIN, TIMEOUT_MAX);
		
		et_scrollableMessage_text = (EditText) parent.findViewById(R.id.et_scrollableMessage_text);
		
		softButtonsIncluded = (CheckBox) parent.findViewById(R.id.cb_include_softbuttons);
		
		Button clearButton = (Button) parent.findViewById(R.id.but_scrollableMessage_clear);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				et_scrollableMessage_text.setText("");
			}
		});
		
		tv_timeout = (TextView) parent.findViewById(R.id.tv_scrollableMessage_timeout);
		updateTimeoutText(TIMEOUT_DEFAULT);
		
		seek_timeout = (SeekBar) parent.findViewById(R.id.seek_scrollableMessage_timeout);
		seek_timeout.setMax(progressCalculator.getMaxProgress());
		seek_timeout.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updateTimeoutText((int) progressCalculator.calculateValue(progress));
			}
		});
		seek_timeout.setProgress(progressCalculator.calculateProgress(TIMEOUT_DEFAULT));
	}
	
	private void updateTimeoutText(int timeout){
		tv_timeout.setText(new StringBuilder().append(timeoutBaseStr).append(timeout).append(" s").toString());
	}
	
	//dialog button listeners
	private final DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String message = et_scrollableMessage_text.getText().toString();
			int timeout = (int) progressCalculator.calculateValue(seek_timeout.getProgress());
			timeout = MathUtils.convertSecsToMillisecs(timeout);
			
			if(message.length() <= 0){
				message = " ";
			}
			// if the message is too long, we'll just chop off the end
			else if(message.length() > LENGTH_MAX){
				Toast.makeText(context, "Text was too long, extra characters are being dropped.", Toast.LENGTH_LONG).show();
				message = message.substring(0, LENGTH_MAX);
			}
			
			RPCRequest result = SdlRequestFactory.scrollableMessage(message, timeout, softButtons);
			notifyListener(result);
		}
	};

}
