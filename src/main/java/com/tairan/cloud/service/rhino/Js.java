package com.tairan.cloud.service.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Js {

	public static void main(String[] args) {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();

		String jsStr = "100*20/10";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}

}
