package com.aimeizhuyi.users.analysis.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class GZip {
	
	private final static String	LOG_TAG	= GZip.class.getName();
	
	private static final String charset = "UTF-8";
	/**
	 * gzip压缩字符，压缩失败则返回原字符
	 * @param
	 * @param str
	 * @return
	 */
	public static String compress( String str) {
		if (str == null || str.length() == 0)
			return str;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(charset));
			gzip.close();
			return out.toString(charset);
		} catch (IOException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
        }
		return str;
	}
	/**
	 * 加压压缩过的字符
	 * @param
	 * @param str
	 * @return
	 */
	public static String uncompress( String str) {
		if (str == null || str.length() == 0)
			return str;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes(charset));
			GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gzipInputStream.read(buffer)) >= 0) {
				byteArrayOutputStream.write(buffer, 0, n);
			}
			return byteArrayOutputStream.toString();
		} catch (UnsupportedEncodingException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
		} catch (IOException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
		}
		return str;
	}
	
	/**
	 * 使用gzip压缩字符，返回压缩后的字节数组
	 * @param
	 * @param str
	 * @return
	 */
	public static byte[] compress_(String str) {
		if (str == null || str.length() == 0)
			return null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
			return out.toByteArray();
		} catch (IOException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
		}
		return str.getBytes();
	}
	
	/**
	 * 将压缩过的字符解压后返回其字节数组
	 * @param context
	 * @param str
	 * @return
	 */
	public static byte[] uncompress_(Context context, String str) {
		if (str == null || str.length() == 0)
			return str.getBytes();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gzipInputStream.read(buffer)) >= 0) {
				byteArrayOutputStream.write(buffer, 0, n);
			}
			return byteArrayOutputStream.toByteArray();
		} catch (UnsupportedEncodingException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
		} catch (IOException e) {
            Log.w(LOG_TAG,  e.getLocalizedMessage());
		}
		return str.getBytes();
	}
}