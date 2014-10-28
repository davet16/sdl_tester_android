package com.livio.sdl;

import com.livio.sdl.utils.UpCounter;


public class IdGenerator{

    private IdGenerator(){}

    private static UpCounter commandIdGenerator;
    
    public static int next(){
        if(commandIdGenerator == null){
            commandIdGenerator = new UpCounter(100);
        }
        
        return commandIdGenerator.next();
    }
    
    public static void reset(){
        if(commandIdGenerator != null){
            commandIdGenerator.reset();
        }
    }
}
