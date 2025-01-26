package com.zonesoft.persons.general.utils;

import java.lang.StackWalker.StackFrame;
import java.util.Optional;

public class MethodInfoHelper {

	public static String methodName() {
		Optional<StackFrame> optional = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
	                      .walk(s -> s.skip(1).findFirst());
		return extractInfoFromFrame(optional);
	}

	public static String calledBy() {
		Optional<StackFrame> optional =  StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
	                      .walk(s -> s.skip(2).findFirst());
		return extractInfoFromFrame(optional);
	}
	
	private static String extractInfoFromFrame(Optional<StackFrame> optional) {
		if (optional.isPresent()) {
			StackFrame frame = optional.get();
			StringBuilder sb = new StringBuilder();
			sb.append(frame.getMethodName());
			sb.append(" ["); sb.append(frame.getLineNumber()); sb.append("]");
			return sb.toString();
		}else {
			return "<stack-data-unavailable>";
		}
	}	
}
