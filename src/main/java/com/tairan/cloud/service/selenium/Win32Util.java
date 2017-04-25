package com.tairan.cloud.service.selenium;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

/**
 * Window组件操作工具类
 * 
 * @author sunju
 * 
 */
public class Win32Util implements User32Ext {

	public static final int WM_SETTEXT = 0x000C; // 输入文本

	public static final int WM_CHAR = 0x0102; // 输入字符

	public static final int BM_CLICK = 0xF5; // 点击事件，即按下和抬起两个动作

	public static final int KEYEVENTF_KEYUP = 0x0002; // 键盘按键抬起

	public static final int KEYEVENTF_KEYDOWN = 0x0; // 键盘按键按下

	private static final int N_MAX_COUNT = 512;

	private Win32Util() {
	}

	/**
	 * 从桌面开始查找指定类名的组件，在超时的时间范围内，如果未找到任何匹配的组件则反复查找
	 * 
	 * @param className
	 *            组件的类名
	 * @param timeout
	 *            超时时间
	 * @param unit
	 *            超时时间的单位
	 * @return 返回匹配的组件的句柄，如果匹配的组件大于一个，返回第一个查找的到的；如果未找到或超时则返回<code>null</code>
	 */
	public static HWND findHandleByClassName(String className, long timeout, TimeUnit unit) {
		return findHandleByClassName(USER32EXT.GetDesktopWindow(), className, timeout, unit);
	}

	/**
	 * 从桌面开始查找指定类名的组件
	 * 
	 * @param className
	 *            组件的类名
	 * @return 返回匹配的组件的句柄，如果匹配的组件大于一个，返回第一个查找的到的；如果未找到任何匹配则返回<code>null</code>
	 */
	public static HWND findHandleByClassName(String className) {
		return findHandleByClassName(USER32EXT.GetDesktopWindow(), className);
	}

	/**
	 * 从指定位置开始查找指定类名的组件
	 * 
	 * @param root
	 *            查找组件的起始位置的组件的句柄，如果为<code>null</code>则从桌面开始查找
	 * @param className
	 *            组件的类名
	 * @param timeout
	 *            超时时间
	 * @param unit
	 *            超时时间的单位
	 * @return 返回匹配的组件的句柄，如果匹配的组件大于一个，返回第一个查找的到的；如果未找到或超时则返回<code>null</code>
	 */
	public static HWND findHandleByClassName(HWND root, String className, long timeout, TimeUnit unit) {
		if (null == className || className.length() <= 0) {
			return null;
		}
		long start = System.currentTimeMillis();
		HWND hwnd = findHandleByClassName(root, className);
		while (null == hwnd && (System.currentTimeMillis() - start < unit.toMillis(timeout))) {
			hwnd = findHandleByClassName(root, className);
		}
		return hwnd;
	}

	/**
	 * 从指定位置开始查找指定类名的组件
	 * 
	 * @param root
	 *            查找组件的起始位置的组件的句柄，如果为<code>null</code>则从桌面开始查找
	 * @param className
	 *            组件的类名
	 * @return 返回匹配的组件的句柄，如果匹配的组件大于一个，返回第一个查找的到的；如果未找到任何匹配则返回<code>null</code>
	 */
	public static HWND findHandleByClassName(HWND root, String className) {
		if (null == className || className.length() <= 0) {
			return null;
		}
		HWND[] result = new HWND[1];
		findHandle(result, root, className);
		return result[0];
	}

	private static boolean findHandle(final HWND[] target, HWND root, final String className) {
		if (null == root) {
			root = USER32EXT.GetDesktopWindow();
		}
		return USER32EXT.EnumChildWindows(root, new WNDENUMPROC() {

			@Override
			public boolean callback(HWND hwnd, Pointer pointer) {
				char[] winClass = new char[N_MAX_COUNT];
				USER32EXT.GetClassName(hwnd, winClass, N_MAX_COUNT);
				if (USER32EXT.IsWindowVisible(hwnd) && className.equals(Native.toString(winClass))) {
					target[0] = hwnd;
					return false;
				} else {
					return target[0] == null || findHandle(target, hwnd, className);
				}
			}

		}, Pointer.NULL);
	}

	/**
	 * 模拟键盘按键事件，异步事件。使用win32
	 * keybd_event，每次发送KEYEVENTF_KEYDOWN、KEYEVENTF_KEYUP两个事件。默认10秒超时
	 * 
	 * @param hwnd
	 *            被键盘操作的组件句柄
	 * @param keyCombination
	 *            键盘的虚拟按键码（<a href=
	 *            "http://msdn.microsoft.com/ZH-CN/library/windows/desktop/dd375731.aspx">
	 *            Virtual-Key Code</a>），或者使用{@link java.awt.event.KeyEvent}</br>
	 *            二维数组第一维中的一个元素为一次按键操作，包含组合操作，第二维中的一个元素为一个按键事件，即一个虚拟按键码
	 * @return 键盘按键事件放入windows消息队列成功返回<code>true</code>
	 *         ，键盘按键事件放入windows消息队列失败或超时返回<code>false</code>
	 */
	public static boolean simulateKeyboardEvent(HWND hwnd, int[][] keyCombination) {
		if (null == hwnd) {
			return false;
		}
		USER32EXT.SwitchToThisWindow(hwnd, true);
		USER32EXT.SetFocus(hwnd);
		for (int[] keys : keyCombination) {
			for (int i = 0; i < keys.length; i++) {
				USER32EXT.keybd_event((byte) keys[i], (byte) 0, KEYEVENTF_KEYDOWN, 0); // key
																						// down
			}
			for (int i = keys.length - 1; i >= 0; i--) {
				USER32EXT.keybd_event((byte) keys[i], (byte) 0, KEYEVENTF_KEYUP, 0); // key
																						// up
			}
		}
		return true;
	}

	/**
	 * 模拟字符输入，同步事件。使用win32 SendMessage API发送WM_CHAR事件。默认10秒超时
	 * 
	 * @param hwnd
	 *            被输入字符的组件的句柄
	 * @param content
	 *            输入的内容。字符串会被转换成<code>char[]</code>后逐个字符输入
	 * @return 字符输入事件发送成功返回<code>true</code>，字符输入事件发送失败或超时返回<code>false</code>
	 */
	public static boolean simulateCharInput(final HWND hwnd, final String content) {
		if (null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					USER32EXT.SwitchToThisWindow(hwnd, true);
					USER32EXT.SetFocus(hwnd);
					for (char c : content.toCharArray()) {
						Thread.sleep(5);
						USER32EXT.SendMessage(hwnd, WM_CHAR, (byte) c, 0);
					}
					return true;
				}

			});
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean simulateCharInput(final HWND hwnd, final String content, final long sleepMillisPreCharInput) {
		if (null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					USER32EXT.SwitchToThisWindow(hwnd, true);
					USER32EXT.SetFocus(hwnd);
					for (char c : content.toCharArray()) {
						Thread.sleep(sleepMillisPreCharInput);
						USER32EXT.SendMessage(hwnd, WM_CHAR, (byte) c, 0);
					}
					return true;
				}

			});
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 模拟文本输入，同步事件。使用win32 SendMessage API发送WM_SETTEXT事件。默认10秒超时
	 * 
	 * @param hwnd
	 *            被输入文本的组件的句柄
	 * @param content
	 *            输入的文本内容
	 * @return 文本输入事件发送成功返回<code>true</code>，文本输入事件发送失败或超时返回<code>false</code>
	 */
	public static boolean simulateTextInput(final HWND hwnd, final String content) {
		if (null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					USER32EXT.SwitchToThisWindow(hwnd, true);
					USER32EXT.SetFocus(hwnd);
					USER32EXT.SendMessage(hwnd, WM_SETTEXT, 0, content);
					return true;
				}

			});
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 模拟鼠标点击，同步事件。使用win32 SendMessage API发送BM_CLICK事件。默认10秒超时
	 * 
	 * @param hwnd
	 *            被点击的组件的句柄
	 * @return 点击事件发送成功返回<code>true</code>，点击事件发送失败或超时返回<code>false</code>
	 */
	public static boolean simulateClick(final HWND hwnd) {
		if (null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					USER32EXT.SwitchToThisWindow(hwnd, true);
					USER32EXT.SendMessage(hwnd, BM_CLICK, 0, null);
					return true;
				}

			});
		} catch (Exception e) {
			return false;
		}
	}

	private static <T> T execute(Callable<T> callable) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<T> task = executor.submit(callable);
			return task.get(10, TimeUnit.SECONDS);
		} finally {
			executor.shutdown();
		}
	}

	@Override
	public boolean AttachThreadInput(DWORD arg0, DWORD arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LRESULT CallNextHookEx(HHOOK arg0, int arg1, WPARAM arg2, LPARAM arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LRESULT CallNextHookEx(HHOOK arg0, int arg1, WPARAM arg2, Pointer arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean CloseWindow(HWND arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HWND CreateWindowEx(int arg0, WString arg1, String arg2, int arg3, int arg4, int arg5, int arg6, int arg7,
			HWND arg8, HMENU arg9, HINSTANCE arg10, LPVOID arg11) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LRESULT DefWindowProc(HWND arg0, int arg1, WPARAM arg2, LPARAM arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean DestroyIcon(HICON arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DestroyWindow(HWND arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LRESULT DispatchMessage(MSG arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean EnumChildWindows(HWND arg0, WNDENUMPROC arg1, Pointer arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean EnumThreadWindows(int arg0, WNDENUMPROC arg1, Pointer arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean EnumWindows(WNDENUMPROC arg0, Pointer arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HWND FindWindow(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean FlashWindowEx(FLASHWINFO arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public short GetAsyncKeyState(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean GetClassInfoEx(HINSTANCE arg0, WString arg1, WNDCLASSEX arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetClassName(HWND arg0, char[] arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HDC GetDC(HWND arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HWND GetForegroundWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean GetGUIThreadInfo(int arg0, GUITHREADINFO arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean GetKeyboardState(byte[] arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean GetLastInputInfo(LASTINPUTINFO arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean GetLayeredWindowAttributes(HWND arg0, IntByReference arg1, ByteByReference arg2,
			IntByReference arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetMessage(MSG arg0, HWND arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int GetSystemMetrics(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HWND GetWindow(HWND arg0, DWORD arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean GetWindowInfo(HWND arg0, WINDOWINFO arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetWindowLong(HWND arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LONG_PTR GetWindowLongPtr(HWND arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int GetWindowModuleFileName(HWND arg0, char[] arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean GetWindowRect(HWND arg0, RECT arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetWindowText(HWND arg0, char[] arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int GetWindowTextLength(HWND arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int GetWindowThreadProcessId(HWND arg0, IntByReference arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean InvalidateRect(HWND arg0, RECT arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsWindowVisible(HWND arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HICON LoadIcon(HINSTANCE arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HANDLE LoadImage(HINSTANCE arg0, String arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean MoveWindow(HWND arg0, int arg1, int arg2, int arg3, int arg4, boolean arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean PeekMessage(MSG arg0, HWND arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void PostMessage(HWND arg0, int arg1, WPARAM arg2, LPARAM arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void PostQuitMessage(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean RedrawWindow(HWND arg0, RECT arg1, HRGN arg2, DWORD arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ATOM RegisterClassEx(WNDCLASSEX arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HDEVNOTIFY RegisterDeviceNotification(HANDLE arg0, Structure arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean RegisterHotKey(HWND arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int RegisterWindowMessage(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ReleaseDC(HWND arg0, HDC arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DWORD SendInput(DWORD arg0, INPUT[] arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HWND SetFocus(HWND arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SetForegroundWindow(HWND arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean SetLayeredWindowAttributes(HWND arg0, int arg1, byte arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HWND SetParent(HWND arg0, HWND arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int SetWindowLong(HWND arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pointer SetWindowLong(HWND arg0, int arg1, Pointer arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LONG_PTR SetWindowLongPtr(HWND arg0, int arg1, LONG_PTR arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pointer SetWindowLongPtr(HWND arg0, int arg1, Pointer arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SetWindowPos(HWND arg0, HWND arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int SetWindowRgn(HWND arg0, HRGN arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HHOOK SetWindowsHookEx(int arg0, HOOKPROC arg1, HINSTANCE arg2, int arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ShowWindow(HWND arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean TranslateMessage(MSG arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UnhookWindowsHookEx(HHOOK arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UnregisterClass(WString arg0, HINSTANCE arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UnregisterDeviceNotification(HDEVNOTIFY arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UnregisterHotKey(Pointer arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UpdateLayeredWindow(HWND arg0, HDC arg1, POINT arg2, SIZE arg3, HDC arg4, POINT arg5, int arg6,
			BLENDFUNCTION arg7, int arg8) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean UpdateWindow(HWND arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DWORD WaitForInputIdle(HANDLE arg0, DWORD arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HWND FindWindowEx(HWND lpParent, HWND lpChild, String lpClassName, String lpWindowName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HWND GetDesktopWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int SendMessage(HWND hWnd, int dwFlags, byte bVk, int dwExtraInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int SendMessage(HWND hWnd, int Msg, int wParam, String lParam) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SwitchToThisWindow(HWND hWnd, boolean fAltTab) {
		// TODO Auto-generated method stub

	}
}