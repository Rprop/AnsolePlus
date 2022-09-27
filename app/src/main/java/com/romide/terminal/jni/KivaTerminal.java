package com.romide.terminal.jni;

public final class KivaTerminal {

    public static final int REPLACE_YES = 1;
//	public static final int REPLACE_NO = 0;

    static {
        System.loadLibrary("kiva");
    }

    public static void setEnv(String name, String value) {
        setenv(name, value, REPLACE_YES);
    }

    private static native void setenv(String name, String value, int replace);
}
