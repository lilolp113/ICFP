package com.icfp.frame.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.log4j.Logger;

public class ApplicationRuntimeException extends RuntimeException{
	
	private static final long serialVersionUID = 531598236529157982L;
	
	private static Logger logger = Logger.getLogger(ApplicationRuntimeException.class);
	
	private Throwable mRootCause = null;
	
	public ApplicationRuntimeException(){
		
	}
	
	public ApplicationRuntimeException(String message) {
		super(message);
	}
	
	public ApplicationRuntimeException(Throwable t){
		super(t);
		this.mRootCause = t;
	}
	public ApplicationRuntimeException(String mess, Throwable t){
		super(mess, t);
		this.mRootCause = t;
	}

	public Throwable getRootCause(){
		return this.mRootCause;
	}

	public String getRootCauseMessage(){
		String rcmessage = null;
		if(getRootCause() != null){
			if (getRootCause().getCause() != null){
				rcmessage = getRootCause().getCause().getMessage();
			}
			rcmessage = rcmessage == null ? getRootCause().getMessage() : rcmessage;
			rcmessage = rcmessage == null ? super.getMessage() : rcmessage;
			rcmessage = rcmessage == null ? "NONE" : rcmessage;
	    }
	    return rcmessage;
	}

	public void printStackTrace(){
	    super.printStackTrace();
	    if (this.mRootCause != null){
	    	logger.info("--- ROOT CAUSE ---");
	    	this.mRootCause.printStackTrace();
	    }
	}

	public void printStackTrace(PrintStream s){
	    super.printStackTrace(s);
	    if (this.mRootCause != null){
	      s.println("--- ROOT CAUSE ---");
	      this.mRootCause.printStackTrace(s);
	    }
	}

	public void printStackTrace(PrintWriter s){
	    super.printStackTrace(s);
	    if (null != this.mRootCause){
	      s.println("--- ROOT CAUSE ---");
	      this.mRootCause.printStackTrace(s);
	    }
	}
}
