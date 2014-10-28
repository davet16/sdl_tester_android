package com.livio.sdltester.dialogs;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.livio.sdl.SdlConstants;
import com.livio.sdl.SdlImageItem;
import com.livio.sdl.SdlRequestFactory;
import com.livio.sdl.dialogs.BaseAlertDialog;
import com.livio.sdl.dialogs.BaseOkCancelDialog;
import com.livio.sdl.enums.SdlCommand;
import com.livio.sdl.utils.MathUtils;
import com.livio.sdl.viewhelpers.SeekBarCalculator;
import com.livio.sdltester.R;
import com.smartdevicelink.proxy.rpc.Alert;
import com.smartdevicelink.proxy.rpc.SoftButton;

public class SdlAlertDialog extends BaseOkCancelDialog implements OnSeekBarChangeListener{

	private static final SdlCommand SYNC_COMMAND = SdlCommand.ALERT;
	private static final String DIALOG_TITLE = SYNC_COMMAND.toString();
	
	// a seekbar cannot do decimal points, so it currently ranges 0-50, which is then
	// divided by 10.0f to give us a number of seconds, rounded to 1/10 of a second.
	private static final float TENS_PLACE_DENOMINATOR = 10.0f;
	
	//set up your min & max time allowed here. we multiply by 10 since we're allowing 1/10th of a second selections
	private static final int MINIMUM_ALERT_TONE_TIME = (int) (SdlConstants.AlertConstants.ALERT_TIME_MINIMUM * TENS_PLACE_DENOMINATOR);
	private static final int MAXIMUM_ALERT_TONE_TIME = (int) (SdlConstants.AlertConstants.ALERT_TIME_MAXIMUM * TENS_PLACE_DENOMINATOR);

	//this is your default selection for tone duration.  again, divide by 10 for the actual time in seconds.
	private static final float DEFAULT_TONE_DURATION = 5.0f;  // 5.0 seconds
	
	private EditText et_alert_textToSpeak;
	private EditText et_alert_line1;
	private EditText et_alert_line2;
	private EditText et_alert_line3;
	
	private TextView tv_alert_toneDuration;
	
	private CheckBox check_alert_playTone, check_softButtons;
	
	private SeekBar seek_alert_toneDuration;
	private SeekBarCalculator progressCalculator;
	
	private String secondsStr;
	
	private List<SoftButton> softButtons;
	
	public SdlAlertDialog(Context context) {
		this(context, null);
	}
	
	public SdlAlertDialog(final Context context, final List<SdlImageItem> images){
	    super(context, DIALOG_TITLE, R.layout.alert);

        check_softButtons.setOnCheckedChangeListener(new OnCheckedChangeListener(){
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
		secondsStr = context.getResources().getString(R.string.units_seconds);
		progressCalculator = new SeekBarCalculator(MINIMUM_ALERT_TONE_TIME, MAXIMUM_ALERT_TONE_TIME, TENS_PLACE_DENOMINATOR);
		
		et_alert_textToSpeak = (EditText) view.findViewById(R.id.et_alert_textToSpeak);
		et_alert_line1 = (EditText) view.findViewById(R.id.et_alert_line1);
		et_alert_line2 = (EditText) view.findViewById(R.id.et_alert_line2);
		et_alert_line3 = (EditText) view.findViewById(R.id.et_alert_line3);
		
		tv_alert_toneDuration = (TextView) view.findViewById(R.id.tv_alert_toneDuration);
		
		seek_alert_toneDuration = (SeekBar) view.findViewById(R.id.seek_alert_toneDuration);
		seek_alert_toneDuration.setMax(progressCalculator.getMaxProgress());
		seek_alert_toneDuration.setProgress(progressCalculator.calculateProgress(DEFAULT_TONE_DURATION));
		seek_alert_toneDuration.setOnSeekBarChangeListener(this);
		
		check_alert_playTone = (CheckBox) view.findViewById(R.id.check_alert_playTone);
		check_softButtons = (CheckBox) view.findViewById(R.id.cb_include_softbuttons);
		
		//make initial updates to the UI using default values
		updateProgressText(DEFAULT_TONE_DURATION);
		
		//TODO - leaving these out for now - MRB
		//check_alert_includeSoftButtons = (CheckBox) view.findViewById(R.id.check_alert_includeSoftButtons);
		//
		//but_alert_includeSoftButtons = (Button) view.findViewById(R.id.but_alert_includeSoftButtons);
		//TODO - add on click listener to this button.
	}

	private void updateProgressText(float progress){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(progress);
		strBuilder.append(secondsStr);
		tv_alert_toneDuration.setText(strBuilder.toString());
	}
	
	// dialog button listsners
	private final DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String textToSpeak = et_alert_textToSpeak.getText().toString();
			String line1   = et_alert_line1.getText().toString();
			String line2   = et_alert_line2.getText().toString();
			String line3   = et_alert_line3.getText().toString();
			int toneDurationInS = (int) progressCalculator.calculateValue(seek_alert_toneDuration.getProgress());
			int toneDurationInMs = MathUtils.convertSecsToMillisecs(toneDurationInS);
			boolean playTone = check_alert_playTone.isChecked();
			
			if(textToSpeak.equals("")){
				textToSpeak = null;
			}
			if(line1.equals("")){
				line1 = " ";
			}
			if(line2.equals("")){
				line2 = " ";
			}
			if(line3.equals("")){
				line3 = " ";
			}
			
			Alert result = (Alert) SdlRequestFactory.alert(textToSpeak, line1, line2, line3, playTone, toneDurationInMs);
			if(softButtons != null && softButtons.size() > 0){
			    result.setSoftButtons(new Vector<SoftButton>(softButtons));
			}
			notifyListener(result);
		}
	};

	/*
	 * On Seek Bar Changed Listener Methods
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		updateProgressText(progressCalculator.calculateValue(progress));
	}

	@Override public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override public void onStopTrackingTouch(SeekBar seekBar) {}
}
