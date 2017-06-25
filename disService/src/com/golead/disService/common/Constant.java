package com.golead.disService.common;

import java.io.File;

public class Constant {
	
	public static final String DIR_DAT = System.getProperty("user.dir") + File.separator + "dat" + File.separator;
	
	public static final String SFX_CFG = ".cfg";
	
	public static final String STATUS_START = "start";
	public static final String STATUS_COLLECTED = "collected";
	public static final String STATUS_ZIP = "zip";
	public static final String STATUS_SENT = "sent";
	public static final String STATUS_UNZIP = "unzip";
	public static final String STATUS_FINISH = "finish";
	public static final String STATUS_FAIL = "fail";

}
