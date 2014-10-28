package com.livio.sdl;

import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.rpc.AddCommandResponse;
import com.smartdevicelink.proxy.rpc.AddSubMenuResponse;
import com.smartdevicelink.proxy.rpc.AlertResponse;
import com.smartdevicelink.proxy.rpc.ChangeRegistrationResponse;
import com.smartdevicelink.proxy.rpc.CreateInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteCommandResponse;
import com.smartdevicelink.proxy.rpc.DeleteFileResponse;
import com.smartdevicelink.proxy.rpc.DeleteInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteSubMenuResponse;
import com.smartdevicelink.proxy.rpc.GetDTCsResponse;
import com.smartdevicelink.proxy.rpc.GetVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.ListFilesResponse;
import com.smartdevicelink.proxy.rpc.PerformInteractionResponse;
import com.smartdevicelink.proxy.rpc.PutFileResponse;
import com.smartdevicelink.proxy.rpc.ReadDIDResponse;
import com.smartdevicelink.proxy.rpc.ResetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.ScrollableMessageResponse;
import com.smartdevicelink.proxy.rpc.SetAppIconResponse;
import com.smartdevicelink.proxy.rpc.SetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.SetMediaClockTimerResponse;
import com.smartdevicelink.proxy.rpc.ShowResponse;
import com.smartdevicelink.proxy.rpc.SliderResponse;
import com.smartdevicelink.proxy.rpc.SpeakResponse;
import com.smartdevicelink.proxy.rpc.SubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.UnsubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.UnsubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.enums.Result;

/**
 * A factory class to generate responses based on the input requests.
 *
 * @author Mike Burke
 *
 */
public final class SdlResponseFactory {

	private SdlResponseFactory() {}
	
	/**
	 * Creates a generic "Success" response for the input request with no input checking.  The created
	 * response is sent back to the appropriate method of the input listener.
	 * 
	 * @param request The request to respond to
	 * @param listener The listener to inform when the response is complete
	 */
	public static void sendGenericResponseForRequest(RPCRequest request, IProxyListenerALM listener){
		if(listener == null){
			throw new NullPointerException();
		}

		final String reqName = request.getFunctionName();
		final int correlationId = request.getCorrelationID();
		
		// this is really bad
		if(reqName.equals(FunctionID.ALERT)){
			AlertResponse result = new AlertResponse();
			setSuccessParams(result, correlationId);
			listener.onAlertResponse(result);
		}
		else if(reqName.equals(FunctionID.SPEAK)){
			SpeakResponse result = new SpeakResponse();
			setSuccessParams(result, correlationId);
			listener.onSpeakResponse(result);
		}
		else if(reqName.equals(FunctionID.SHOW)){
			ShowResponse result = new ShowResponse();
			setSuccessParams(result, correlationId);
			listener.onShowResponse(result);
		}
		else if(reqName.equals(FunctionID.SUBSCRIBE_BUTTON)){
			SubscribeButtonResponse result = new SubscribeButtonResponse();
			setSuccessParams(result, correlationId);
			listener.onSubscribeButtonResponse(result);
			
		}
		else if(reqName.equals(FunctionID.UNSUBSCRIBE_BUTTON)){
			UnsubscribeButtonResponse result = new UnsubscribeButtonResponse();
			setSuccessParams(result, correlationId);
			listener.onUnsubscribeButtonResponse(result);
		}
		else if(reqName.equals(FunctionID.ADD_COMMAND)){
			AddCommandResponse result = new AddCommandResponse();
			setSuccessParams(result, correlationId);
			listener.onAddCommandResponse(result);
		}
		else if(reqName.equals(FunctionID.DELETE_COMMAND)){
			DeleteCommandResponse result = new DeleteCommandResponse();
			setSuccessParams(result, correlationId);
			listener.onDeleteCommandResponse(result);
		}
		else if(reqName.equals(FunctionID.ADD_SUB_MENU)){
			AddSubMenuResponse result = new AddSubMenuResponse();
			setSuccessParams(result, correlationId);
			listener.onAddSubMenuResponse(result);
		}
		else if(reqName.equals(FunctionID.DELETE_SUB_MENU)){
			DeleteSubMenuResponse result = new DeleteSubMenuResponse();
			setSuccessParams(result, correlationId);
			listener.onDeleteSubMenuResponse(result);
		}
		else if(reqName.equals(FunctionID.SET_GLOBAL_PROPERTIES)){
			SetGlobalPropertiesResponse result = new SetGlobalPropertiesResponse();
			setSuccessParams(result, correlationId);
			listener.onSetGlobalPropertiesResponse(result);
		}
		else if(reqName.equals(FunctionID.RESET_GLOBAL_PROPERTIES)){
			ResetGlobalPropertiesResponse result = new ResetGlobalPropertiesResponse();
			setSuccessParams(result, correlationId);
			listener.onResetGlobalPropertiesResponse(result);
		}
		else if(reqName.equals(FunctionID.SET_MEDIA_CLOCK_TIMER)){
			SetMediaClockTimerResponse result = new SetMediaClockTimerResponse();
			setSuccessParams(result, correlationId);
			listener.onSetMediaClockTimerResponse(result);
		}
		else if(reqName.equals(FunctionID.CREATE_INTERACTION_CHOICE_SET)){
			CreateInteractionChoiceSetResponse result = new CreateInteractionChoiceSetResponse();
			setSuccessParams(result, correlationId);
			listener.onCreateInteractionChoiceSetResponse(result);
		}
		else if(reqName.equals(FunctionID.DELETE_INTERACTION_CHOICE_SET)){
			DeleteInteractionChoiceSetResponse result = new DeleteInteractionChoiceSetResponse();
			setSuccessParams(result, correlationId);
			listener.onDeleteInteractionChoiceSetResponse(result);
		}
		else if(reqName.equals(FunctionID.PERFORM_INTERACTION)){
			PerformInteractionResponse result = new PerformInteractionResponse();
			setSuccessParams(result, correlationId);
			listener.onPerformInteractionResponse(result);
		}
		else if(reqName.equals(FunctionID.SLIDER)){
			SliderResponse result = new SliderResponse();
			setSuccessParams(result, correlationId);
			listener.onSliderResponse(result);
		}
		else if(reqName.equals(FunctionID.SCROLLABLE_MESSAGE)){
			ScrollableMessageResponse result = new ScrollableMessageResponse();
			setSuccessParams(result, correlationId);
			listener.onScrollableMessageResponse(result);
		}
		else if(reqName.equals(FunctionID.CHANGE_REGISTRATION)){
			ChangeRegistrationResponse result = new ChangeRegistrationResponse();
			setSuccessParams(result, correlationId);
			listener.onChangeRegistrationResponse(result);
		}
		else if(reqName.equals(FunctionID.PUT_FILE)){
			PutFileResponse result = new PutFileResponse();
			setSuccessParams(result, correlationId);
			listener.onPutFileResponse(result);
		}
		else if(reqName.equals(FunctionID.DELETE_FILE)){
			DeleteFileResponse result = new DeleteFileResponse();
			setSuccessParams(result, correlationId);
			listener.onDeleteFileResponse(result);
		}
		else if(reqName.equals(FunctionID.LIST_FILES)){
			ListFilesResponse result = new ListFilesResponse();
			setSuccessParams(result, correlationId);
			listener.onListFilesResponse(result);
		}
		else if(reqName.equals(FunctionID.SET_APP_ICON)){
			SetAppIconResponse result = new SetAppIconResponse();
			setSuccessParams(result, correlationId);
			listener.onSetAppIconResponse(result);
		}
		else if(reqName.equals(FunctionID.SUBSCRIBE_VEHICLE_DATA)){
			SubscribeVehicleDataResponse result = new SubscribeVehicleDataResponse();
			setSuccessParams(result, correlationId);
			listener.onSubscribeVehicleDataResponse(result);
		}
		else if(reqName.equals(FunctionID.UNSUBSCRIBE_VEHICLE_DATA)){
			UnsubscribeVehicleDataResponse result = new UnsubscribeVehicleDataResponse();
			setSuccessParams(result, correlationId);
			listener.onUnsubscribeVehicleDataResponse(result);
		}
		else if(reqName.equals(FunctionID.GET_VEHICLE_DATA)){
			GetVehicleDataResponse result = new GetVehicleDataResponse();
			setSuccessParams(result, correlationId);
			listener.onGetVehicleDataResponse(result);
		}
		else if(reqName.equals(FunctionID.READ_DID)){
			ReadDIDResponse result = new ReadDIDResponse();
			setSuccessParams(result, correlationId);
			listener.onReadDIDResponse(result);
		}
		else if(reqName.equals(FunctionID.GET_DTCS)){
			GetDTCsResponse result = new GetDTCsResponse();
			setSuccessParams(result, correlationId);
			listener.onGetDTCsResponse(result);
		}
	}
	
	private static void setSuccessParams(RPCResponse response, int correlationId){
		response.setSuccess(true);
		response.setCorrelationID(correlationId);
		response.setResultCode(Result.SUCCESS);
	}

}
