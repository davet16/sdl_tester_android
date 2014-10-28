package com.livio.sdl.enums;

import com.smartdevicelink.proxy.rpc.enums.SystemAction;


public enum SdlSystemAction{
    DEFAULT_ACTION("Default Action"),
    STEAL_FOCUS("Steal Focus"),
    KEEP_CONTEXT("Keep Context"),
    
    ;
    
    private final String friendlyName;
    private SdlSystemAction(String friendlyName){
        this.friendlyName = friendlyName;
    }
    
    @Override
    public String toString(){
        return this.friendlyName;
    }
    
    /**
     * Translates a legacy button (ButtonName) to this type of button (SdlButton).
     * 
     * @param legacyButton The legacy button to translate
     * @return The appropriate SdlButton for the input
     */
    public static SdlSystemAction translateFromLegacy(SystemAction legacyButton){
        switch(legacyButton){
        case DEFAULT_ACTION:
            return DEFAULT_ACTION;
        case STEAL_FOCUS:
            return STEAL_FOCUS;
        case KEEP_CONTEXT:
            return KEEP_CONTEXT;
        default:
            return null;
        }
    }
    
    /**
     * Translates this type of button (SdlButton) to a legacy button (ButtonName).
     * 
     * @param sdlButton The new button to translate
     * @return The appropriate legacy button for the input
     */
    public static SystemAction translateToLegacy(SdlSystemAction sdlButton){
        switch(sdlButton){
        case DEFAULT_ACTION:
            return SystemAction.DEFAULT_ACTION;
        case STEAL_FOCUS:
            return SystemAction.STEAL_FOCUS;
        case KEEP_CONTEXT:
            return SystemAction.KEEP_CONTEXT;
        default:
            return null;
        }
    }
}
