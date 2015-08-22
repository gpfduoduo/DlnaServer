package com.guo.duoduo.dlnaserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;


public class FileUtils {

	public final static int NAME_ASC =    0;      
	public final static int NAME_DESC =   1;
	public final static int SIZE_ASC =    2;
	public final static int SIZE_DESC =   3;
	public final static int SUFFIX_ASC =  4;
	public final static int SUFFIX_DESC = 5;

	// http://www.fileinfo.com/filetypes/video , "dat" , "bin" , "rms"
	public static final String[] VIDEO_EXTENSIONS = { "264", "3g2", "3gp",
		"3gp2", "3gpp", "3gpp2", "3mm", "3p2", "60d", "aep", "ajp", "amv",
		"amx", "arf", "asf", "asx", "avb", "avd", "avi", "avs", "avs",
		"axm", "bdm", "bdmv", "bik", "bix", "bmk", "box", "bs4", "bsf",
		"byu", "camre", "clpi", "cpi", "cvc", "d2v", "d3v", "dav", "dce",
		"dck", "ddat", "dif", "dir", "divx", "dlx", "dmb", "dmsm", "dmss",
		"dnc", "dpg", "dream", "dsy", "dv", "dv-avi", "dv4", "dvdmedia",
		"dvr-ms", "dvx", "dxr", "dzm", "dzp", "dzt", "evo", "eye", "f4p",
		"f4v", "fbr", "fbr", "fbz", "fcp", "flc", "flh", "fli", "flv",
		"flx", "gl", "grasp", "gts", "gvi", "gvp", "hdmov", "hkm", "ifo",
		"imovi", "imovi", "iva", "ivf", "ivr", "ivs", "izz", "izzy", "jts",
		"lsf", "lsx", "m15", "m1pg", "m1v", "m21", "m21", "m2a", "m2p",
		"m2t", "m2ts", "m2v", "m4e", "m4u", "m4v", "m75", "meta", "mgv",
		"mj2", "mjp", "mjpg", "mkv", "mmv", "mnv", "mod", "modd", "moff",
		"moi", "moov", "mov", "movie", "mp21", "mp21", "mp2v", "mp4",
		"mp4v", "mpe", "mpeg", "mpeg4", "mpf", "mpg", "mpg2", "mpgin",
		"mpl", "mpls", "mpv", "mpv2", "mqv", "msdvd", "msh", "mswmm",
		"mts", "mtv", "mvb", "mvc", "mvd", "mve", "mvp", "mxf", "mys",
		"ncor", "nsv", "nvc", "ogm", "ogv", "ogx", "osp", "par", "pds",
		"pgi", "piv", "playlist", "pmf", "prel", "pro", "prproj", "psh",
		"pva", "pvr", "pxv", "qt", "qtch", "qtl", "qtm", "qtz",
		"rcproject", "rdb", "rec", "rm", "rmd", "rmp", "rmvb", "roq", "rp",
		"rts", "rts", "rum", "rv", "sbk", "sbt", "scm", "scm", "scn",
		"sec", "seq", "sfvidcap", "smil", "smk", "sml", "smv", "spl",
		"ssm", "str", "stx", "svi", "swf", "swi", "swt", "tda3mt", "tivo",
		"tix", "tod", "tp", "tp0", "tpd", "tpr", "trp", "ts", "tvs", "vc1",
		"vcr", "vcv", "vdo", "vdr", "veg", "vem", "vf", "vfw", "vfz",
		"vgz", "vid", "viewlet", "viv", "vivo", "vlab", "vob", "vp3",
		"vp6", "vp7", "vpj", "vro", "vsp", "w32", "wcp", "webm", "wm",
		"wmd", "wmmp", "wmv", "wmx", "wp3", "wpl", "wtv", "wvx", "xfl",
		"xvid", "yuv", "zm1", "zm2", "zm3", "zmv" };
	// http://www.fileinfo.com/filetypes/audio , "spx" , "mid" , "sf"
	public static final String[] AUDIO_EXTENSIONS = { "4mp", "669", "6cm",
		"8cm", "8med", "8svx", "a2m", "aa", "aa3", "aac", "aax", "abc",
		"abm", "ac3", "acd", "acd-bak", "acd-zip", "acm", "act", "adg",
		"afc", "agm", "ahx", "aif", "aifc", "aiff", "ais", "akp", "al",
		"alaw", "all", "amf", "amr", "ams", "ams", "aob", "ape", "apf",
		"apl", "ase", "at3", "atrac", "au", "aud", "aup", "avr", "awb",
		"band", "bap", "bdd", "box", "bun", "bwf", "c01", "caf", "cda",
		"cdda", "cdr", "cel", "cfa", "cidb", "cmf", "copy", "cpr", "cpt",
		"csh", "cwp", "d00", "d01", "dcf", "dcm", "dct", "ddt", "dewf",
		"df2", "dfc", "dig", "dig", "dls", "dm", "dmf", "dmsa", "dmse",
		"drg", "dsf", "dsm", "dsp", "dss", "dtm", "dts", "dtshd", "dvf",
		"dwd", "ear", "efa", "efe", "efk", "efq", "efs", "efv", "emd",
		"emp", "emx", "esps", "f2r", "f32", "f3r", "f4a", "f64", "far",
		"fff", "flac", "flp", "fls", "frg", "fsm", "fzb", "fzf", "fzv",
		"g721", "g723", "g726", "gig", "gp5", "gpk", "gsm", "gsm", "h0",
		"hdp", "hma", "hsb", "ics", "iff", "imf", "imp", "ins", "ins",
		"it", "iti", "its", "jam", "k25", "k26", "kar", "kin", "kit",
		"kmp", "koz", "koz", "kpl", "krz", "ksc", "ksf", "kt2", "kt3",
		"ktp", "l", "la", "lqt", "lso", "lvp", "lwv", "m1a", "m3u", "m4a",
		"m4b", "m4p", "m4r", "ma1", "mdl", "med", "mgv", "midi", "miniusf",
		"mka", "mlp", "mmf", "mmm", "mmp", "mo3", "mod", "mp1", "mp2",
		"mp3", "mpa", "mpc", "mpga", "mpu", "mp_", "mscx", "mscz", "msv",
		"mt2", "mt9", "mte", "mti", "mtm", "mtp", "mts", "mus", "mws",
		"mxl", "mzp", "nap", "nki", "nra", "nrt", "nsa", "nsf", "nst",
		"ntn", "nvf", "nwc", "odm", "oga", "ogg", "okt", "oma", "omf",
		"omg", "omx", "ots", "ove", "ovw", "pac", "pat", "pbf", "pca",
		"pcast", "pcg", "pcm", "peak", "phy", "pk", "pla", "pls", "pna",
		"ppc", "ppcx", "prg", "prg", "psf", "psm", "ptf", "ptm", "pts",
		"pvc", "qcp", "r", "r1m", "ra", "ram", "raw", "rax", "rbs", "rcy",
		"rex", "rfl", "rmf", "rmi", "rmj", "rmm", "rmx", "rng", "rns",
		"rol", "rsn", "rso", "rti", "rtm", "rts", "rvx", "rx2", "s3i",
		"s3m", "s3z", "saf", "sam", "sb", "sbg", "sbi", "sbk", "sc2", "sd",
		"sd", "sd2", "sd2f", "sdat", "sdii", "sds", "sdt", "sdx", "seg",
		"seq", "ses", "sf2", "sfk", "sfl", "shn", "sib", "sid", "sid",
		"smf", "smp", "snd", "snd", "snd", "sng", "sng", "sou", "sppack",
		"sprg", "sseq", "sseq", "ssnd", "stm", "stx", "sty", "svx", "sw",
		"swa", "syh", "syw", "syx", "td0", "tfmx", "thx", "toc", "tsp",
		"txw", "u", "ub", "ulaw", "ult", "ulw", "uni", "usf", "usflib",
		"uw", "uwf", "vag", "val", "vc3", "vmd", "vmf", "vmf", "voc",
		"voi", "vox", "vpm", "vqf", "vrf", "vyf", "w01", "wav", "wav",
		"wave", "wax", "wfb", "wfd", "wfp", "wma", "wow", "wpk", "wproj",
		"wrk", "wus", "wut", "wv", "wvc", "wve", "wwu", "xa", "xa", "xfs",
		"xi", "xm", "xmf", "xmi", "xmz", "xp", "xrns", "xsb", "xspf", "xt",
		"xwb", "ym", "zvd", "zvr" };

	public static final String[] PICTURE_EXTENSIONS = {
		"jpg", "bmp", "png", "gif", "jpeg"
	};

	public static final String[] APP_EXTENSIONS = {
		".apk"
	};

	private static final HashSet<String> mHashVideo;
	private static final HashSet<String> mHashAudio;
	private static final HashSet<String> mHashPicture;
	private static final HashSet<String> mHashApp;

	private static final double KB = 1024.0;
	private static final double MB = KB * KB;
	private static final double GB = KB * KB * KB;

	static {
		mHashVideo = new HashSet<String>(Arrays.asList(VIDEO_EXTENSIONS));
		mHashAudio = new HashSet<String>(Arrays.asList(AUDIO_EXTENSIONS));
		mHashPicture = new HashSet<String>(Arrays.asList(PICTURE_EXTENSIONS));
		mHashApp = new HashSet<String>(Arrays.asList(APP_EXTENSIONS));
	}

	/** 是否是音频或者视频 */
	public static boolean isVideoOrAudio(File f) {
		final String ext = getFileExtension(f);
		return mHashVideo.contains(ext) || mHashAudio.contains(ext);
	}

	public static boolean isVideoOrAudio(String f) {
		final String ext = getUrlExtension(f);
		return mHashVideo.contains(ext) || mHashAudio.contains(ext);
	}

	public static boolean isVideo(File f) {
		final String ext = getFileExtension(f);
		return mHashVideo.contains(ext);
	}

	public static boolean isVideo(String url) {
		final String ext = getUrlExtension(url);
		return mHashVideo.contains(ext);
	}

	public static boolean isAudio(File f) {
		final String ext = getFileExtension(f);
		return mHashAudio.contains(ext);
	}

	public static boolean isAudio(String url) {
		final String ext = getUrlExtension(url);
		return mHashAudio.contains(ext);
	}

	public static boolean isPicture(File f) {
		final String ext = getFileExtension(f);
		return mHashPicture.contains(ext);
	}

	public static boolean isPicture(String url) {
		final String ext = getUrlExtension(url);
		return mHashPicture.contains(ext);
	}

	public static boolean isAPP(String url) {
		final String ext = getUrlExtension(url);
		return mHashApp.contains(ext);
	}

	public static String getMime(String url) {
		String type ="*/*";
		if(isVideo(url)) {
			type = "video/*";
		}
		if(isAudio(url)) {
			type = "audio/*";
		}
		if(isPicture(url)) {
			type = "image/*";
		}
		if (isAPP(url)) {
			type = "application/*";
		}
		return type;
	}

	/**获取文件后缀 */
	public static String getFileExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase(Locale.US);
			}
		}
		return null;
	}

	public static String getUrlFileName(String url) {
		int slashIndex = url.lastIndexOf('/');
		int dotIndex = url.lastIndexOf('.');
		String filenameWithoutExtension;
		if (dotIndex == -1) {
			filenameWithoutExtension = url.substring(slashIndex + 1);
		} else {
			filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
		}
		return filenameWithoutExtension;
	}

	public static String getUrlExtension(String url) {
		if (!TextUtils.isEmpty(url)) {
			int i = url.lastIndexOf('.');
			if (i > 0 && i < url.length() - 1) {
				return url.substring(i + 1).toLowerCase(Locale.US);
			}
		}
		return "";
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static String showFileSize(long size) {
		String fileSize;
		if (size < KB)
			fileSize = size + "B";
		else if (size < MB)
			fileSize = String.format("%.1f", size / KB) + "KB";
		else if (size < GB)
			fileSize = String.format("%.1f", size / MB) + "MB";
		else
			fileSize = String.format("%.1f", size / GB) + "GB";

		return fileSize;
	}

	/** 显示SD卡剩余空间 */
	public static String showFileAvailable() {
		String result = "";
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			return showFileSize(availCount * blockSize) + " / "
			+ showFileSize(blockSize * blockCount);
		}
		return result;
	}

	/** 如果不存在就创建 */
	public static boolean createIfNoExists(String path) {
		File file = new File(path);
		boolean mk = false;
		if (!file.exists()) {
			mk = file.mkdirs();
		}
		return mk;
	}

	private static HashMap<String, String> mMimeType = new HashMap<String, String>();
	static {
		mMimeType.put("M1V", "video/mpeg");
		mMimeType.put("MP2", "video/mpeg");
		mMimeType.put("MPE", "video/mpeg");
		mMimeType.put("MPG", "video/mpeg");
		mMimeType.put("MPEG", "video/mpeg");
		mMimeType.put("MP4", "video/mp4");
		mMimeType.put("M4V", "video/mp4");
		mMimeType.put("3GP", "video/3gpp");
		mMimeType.put("3GPP", "video/3gpp");
		mMimeType.put("3G2", "video/3gpp2");
		mMimeType.put("3GPP2", "video/3gpp2");
		mMimeType.put("MKV", "video/x-matroska");
		mMimeType.put("WEBM", "video/x-matroska");
		mMimeType.put("MTS", "video/mp2ts");
		mMimeType.put("TS", "video/mp2ts");
		mMimeType.put("TP", "video/mp2ts");
		mMimeType.put("WMV", "video/x-ms-wmv");
		mMimeType.put("ASF", "video/x-ms-asf");
		mMimeType.put("ASX", "video/x-ms-asf");
		mMimeType.put("FLV", "video/x-flv");
		mMimeType.put("MOV", "video/quicktime");
		mMimeType.put("QT", "video/quicktime");
		mMimeType.put("RM", "video/x-pn-realvideo");
		mMimeType.put("RMVB", "video/x-pn-realvideo");
		mMimeType.put("VOB", "video/dvd");
		mMimeType.put("DAT", "video/dvd");
		mMimeType.put("AVI", "video/x-divx");
		mMimeType.put("OGV", "video/ogg");
		mMimeType.put("OGG", "video/ogg");
		mMimeType.put("VIV", "video/vnd.vivo");
		mMimeType.put("VIVO", "video/vnd.vivo");
		mMimeType.put("WTV", "video/wtv");
		mMimeType.put("AVS", "video/avs-video");
		mMimeType.put("SWF", "video/x-shockwave-flash");
		mMimeType.put("YUV", "video/x-raw-yuv");
	}

	/** 获取Video MIME */
	public static String getVideoMimeType(String path) {
		int lastDot = path.lastIndexOf(".");
		if (lastDot < 0)
			return null;

		return mMimeType.get(path.substring(lastDot + 1).toUpperCase(Locale.US));
	}

	/** 多个SD卡时 取外置SD卡 */
	public static String getExternalStorageDirectory() {
		// 参考文章
		// http://blog.csdn.net/bbmiku/article/details/7937745
		Map<String, String> map = System.getenv();
		String[] values = new String[map.values().size()];
		map.values().toArray(values);
		String path = values[values.length - 1];
		Log.e("nmbb", "FileUtils.getExternalStorageDirectory : " + path);
		if (path.startsWith("/mnt/")
				&& !Environment.getExternalStorageDirectory().getAbsolutePath()
				.equals(path))
			return path;
		else
			return null;
	}

	public static String getCanonical(File f) {
		if (f == null)
			return null;

		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			return f.getAbsolutePath();
		}
	}

	public static boolean sdAvailable() {
		return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment
				.getExternalStorageState())
				|| Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState());
	}

	/**remove /\:*"?<>| */
	public static String removeSpecial(String before) {
		String after = null;
		after = before
				.replace(" ","")
				.replace("\u003f", "")
				.replace("\u002F", "")
				.replace("\u003A", "")
				.replace("\u002A", "")
				.replace("\u007C", "")
				.replace("\u003E", "")
				.replace("\u003C", "")
				.replace("\\", "")
				.replace("\"", "");
		return after;
	}

	public static List<File> fileList;
	/**
	 * @param context      上下文对象
	 * @param path         目录路径
	 * @param fs1                                   文件夹排序方式
	 * @param fs2                                   文件排序方式
	 * @param firstSort    文件和文件夹谁排先
	 * @return ArrayList
	 */
	public static List<File> fileList(
			Context context, File path, int fs1, int fs2, boolean firstSort) {

		//file filter, not show hidden files (.*)
		File[] files = path.listFiles(new FileFilter() { 
			@Override
			public boolean accept(File pathname) {
				return ! pathname.isHidden();
			}
		});

		//File[] files = path.listFiles();

		if (files == null) {
			return null;
		}

		fileList = new ArrayList<File>();
		List<File> dir = null;
		List<File> file = null;
		if(fs1 != -1) {
			dir = new ArrayList<File>();
			file = new ArrayList<File>();
			for(File f : files){
				if(f.isDirectory()) {
					dir.add(f);
				}else {
					file.add(f);
				}
			}
			if(firstSort) {
				dirSort(dir, fs1);
				for(File fd : dir){
					fileList.add(fd);
				}
				fileSort(file, fs2);
				for(File ff : file){
					fileList.add(ff);
				}
			}else {
				fileSort(file, fs2);
				for(File ff : file){
					fileList.add(ff);
				}
				dirSort(dir, fs1);
				for(File fd : dir){
					fileList.add(fd);
				}
			}

		}else{
			for(File f : files){
				fileList.add(f);
			}
			fileSort(fileList, fs2);
		}
		return fileList;
	}

	public static List<List<File>> fileCategory(File[] files) {
		List<List<File>> fileList = new ArrayList<List<File>>();

		List<File> dir=new ArrayList<File>();
		List<File> file=new ArrayList<File>();

		for(File f:files){
			if(f.isDirectory()){
				dir.add(f);
			}else {
				file.add(f);
			}
		}
		Collections.sort(dir);
		Collections.sort(file);
		fileList.add(dir);
		fileList.add(file);
		return fileList;
	}

	public static void fileSort(List<File> list,int fs){
		switch (fs) {
		case NAME_ASC:
			Collections.sort(list);
			break;
		case NAME_DESC:
			Collections.sort(list);
			Collections.reverse(list);
			break;
		case SIZE_ASC:
			Collections.sort(list, new Comparator<File>() {

				public int compare(File f1, File f2) {
					int i=f1.length()>f2.length()?1:f1.length()<f2.length()?-1:0;
					return i;
				}

			});
			break;
		case SIZE_DESC:
			Collections.sort(list, new Comparator<File>(){

				public int compare(File f1, File f2) {
					int i=f1.length()>f2.length()?-1:f1.length()<f2.length()?1:0;
					return i;
				}

			});
			break;
		case SUFFIX_ASC:
			Collections.sort(list, new Comparator<File>(){

				public int compare(File f1, File f2) {
					String suffix1=getSuffix(f1);
					String suffix2=getSuffix(f2);
					return suffix1.compareToIgnoreCase(suffix2);
				}

			});
			break;
		case SUFFIX_DESC:
			Collections.sort(list, new Comparator<File>() {

				public int compare(File f1, File f2) {
					String suffix1=getSuffix(f1);
					String suffix2=getSuffix(f2);
					return suffix2.compareToIgnoreCase(suffix1);
				}
			});
			break;
		default:
			break;
		}
	}

	public static String getSuffix(File file) {
		return file.getName().substring(file.getName().lastIndexOf(".")+1);
	}

	public static void dirSort(List<File> list,int fs) {
		switch(fs){
		case NAME_ASC:
			Collections.sort(list);
			break;
		case NAME_DESC:
			Collections.sort(list);
			Collections.reverse(list);
			break;
		case SIZE_ASC:
			Collections.sort(list, new Comparator<File>() {

				public int compare(File d1, File d2) {
					int i = -1;
					if(d1.canRead() && d2.canRead()) {
						int d1_size = d1.listFiles().length;
						int d2_size = d2.listFiles().length;
						i = d1_size > d2_size ? 1 : d1_size < d2_size ? -1 : 0;
					}

					return i;
				}
			});
			break;
		case SIZE_DESC:
			Collections.sort(list, new Comparator<File>() {

				public int compare(File d1, File d2) {
					int i = 1;
					if(d1.canRead() && d2.canRead()) {
						int d1_size = d1.listFiles().length;
						int d2_size = d2.listFiles().length;
						i = d1_size > d2_size ? -1 : d1_size < d2_size ? 1 : 0;
					}

					return i;
				}
			});
			break;
		}
	}

	/**
	 * @param duration "0:01:22.554" or "00:02:33.44" or "123:22:44.55"
	 * 
	 * */
	public static int getTime(String duration){
		int time = 0;
		int ms_index = duration.lastIndexOf('.');
		if(ms_index >0){
			String ms = duration.substring(ms_index+1, duration.length());
			time += Integer.parseInt(ms);
			duration = duration.substring(0, ms_index);
		}
		int s_index = duration.lastIndexOf(':');
		if(s_index>0){
			String s = duration.substring(s_index+1, duration.length());
			time += Integer.parseInt(s) * 1000;
			duration = duration.substring(0, s_index);
		}
		int m_index = duration.lastIndexOf(':');
		if(m_index >0){
			String m = duration.substring(m_index+1, duration.length());
			time += Integer.parseInt(m) * 1000 * 60;
			duration = duration.substring(0, m_index);
		}
		if(m_index > 0){
			time += Integer.parseInt(duration) * 1000 * 60 * 60;
		}		
		return time;
	}

	public static String parseTime(int time) {
		if (time == 0) { return "00:00:00";}

		StringBuffer sb = new StringBuffer();

		int second = time % 60;
		time = time / 60;
		int min = time % 60;
		time = time / 60;
		int hour = time;

		if(hour>0) {
			if(hour < 10) {sb.append("0");}
			sb.append("" + hour + ":");
		}else {sb.append( "00:");}

		if(min > 0) {
			if(min < 10) {sb.append("0");}
			sb.append("" + min + ":");
		}else {sb.append( "00:");}

		if(second > 0) {
			if(second < 10) {sb.append("0");}
			sb.append("" + second);
		} else {sb.append("00");}

		return sb.toString();
	}

	@SuppressLint("DefaultLocale")
	public static String byte2HexString( byte[] b) {
		String string = "";
		for (int i = 0; i < b.length; i++) {   
			String hex = Integer.toHexString(b[i] & 0xFF);   
			if (hex.length() == 1) {   
				hex = '0' + hex;   
			}   
			string += hex.toUpperCase(Locale.US);   
		}   
		return string;
	}

	/***get file name with extension*/
	public static String getUrlFileNameWithExtension(String url) {
		int slashIndex = url.lastIndexOf('/');
		return url.substring(slashIndex + 1);
	}  

	/**
	 * get file name from path, include suffix
	 * 
	 * <pre>
	 *      getFileName(null)               =   null
	 *      getFileName("")                 =   ""
	 *      getFileName("   ")              =   "   "
	 *      getFileName("a.mp3")            =   "a.mp3"
	 *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	 *      getFileName("abc")              =   "abc"
	 *      getFileName("c:\\")              =   ""
	 *      getFileName("c:\\a")             =   "a"
	 *      getFileName("c:\\a.b")           =   "a.b"
	 *      getFileName("c:a.txt\\a")        =   "a"
	 *      getFileName("/home/admin")      =   "admin"
	 *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	 * </pre>
	 * 
	 * @param filePath
	 * @return file name from path, include suffix
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	}

	/**
	 * get folder name from path
	 * 
	 * <pre>
	 *      getFolderName(null)               =   null
	 *      getFolderName("")                 =   ""
	 *      getFolderName("   ")              =   ""
	 *      getFolderName("a.mp3")            =   ""
	 *      getFolderName("a.b.rmvb")         =   ""
	 *      getFolderName("abc")              =   ""
	 *      getFolderName("c:\\")              =   "c:"
	 *      getFolderName("c:\\a")             =   "c:"
	 *      getFolderName("c:\\a.b")           =   "c:"
	 *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	 *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	 *      getFolderName("/home/admin")      =   "/home"
	 *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	 * </pre>
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFolderName(String filePath) {

		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * delete file or directory
	 * <ul>
	 * <li>if path is null or empty, return true</li>
	 * <li>if path not exist, return true</li>
	 * <li>if path exist, delete recursion. return true</li>
	 * <ul>
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return true;
		}

		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}

	/**
	 * copy file
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @return
	 * @throws RuntimeException if an error occurs while operator FileOutputStream
	 */
	public static boolean copyFile(String sourceFilePath, String destFilePath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(sourceFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		}
		return writeFile(destFilePath, inputStream);
	}

	/**
	 * read file
	 * 
	 * @param filePath
	 * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	 * @return if file not exist, return null, else return content of file
	 * @throws RuntimeException if an error occurs while operator BufferedReader
	 */
	public static StringBuilder readFile(String filePath, String charsetName) {
		File file = new File(filePath);
		StringBuilder fileContent = new StringBuilder("");
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
			reader = new BufferedReader(is);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!fileContent.toString().equals("")) {
					fileContent.append("\r\n");
				}
				fileContent.append(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * write file
	 * 
	 * @param filePath
	 * @param content
	 * @param append is append, if true, write to the end of file, else clear content of file and write into it
	 * @return return false if content is empty, true otherwise
	 * @throws RuntimeException if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, String content, boolean append) {
		if (TextUtils.isEmpty(content)) {
			return false;
		}

		FileWriter fileWriter = null;
		try {
			makeDirs(filePath);
			fileWriter = new FileWriter(filePath, append);
			fileWriter.write(content);
			fileWriter.close();
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * write file, the string will be written to the begin of the file
	 * 
	 * @param filePath
	 * @param content
	 * @return
	 */
	public static boolean writeFile(String filePath, String content) {
		return writeFile(filePath, content, false);
	}

	/**
	 * write file, the bytes will be written to the begin of the file
	 * 
	 * @param filePath
	 * @param stream
	 * @return
	 * @see {@link #writeFile(String, InputStream, boolean)}
	 */
	public static boolean writeFile(String filePath, InputStream stream) {
		return writeFile(filePath, stream, false);
	}

	/**
	 * write file
	 * 
	 * @param filePath the file to be opened for writing.
	 * @param stream the input stream
	 * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	 * @return return true
	 * @throws RuntimeException if an error occurs while operator FileOutputStream
	 */
	public static boolean writeFile(String filePath, InputStream stream, boolean append) {
		return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	}

	/**
	 * write file, the bytes will be written to the begin of the file
	 * 
	 * @param file
	 * @param stream
	 * @return
	 * @see {@link #writeFile(File, InputStream, boolean)}
	 */
	public static boolean writeFile(File file, InputStream stream) {
		return writeFile(file, stream, false);
	}

	/**
	 * write file
	 * 
	 * @param file the file to be opened for writing.
	 * @param stream the input stream
	 * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	 * @return return true
	 * @throws RuntimeException if an error occurs while operator FileOutputStream
	 */
	public static boolean writeFile(File file, InputStream stream, boolean append) {
		OutputStream o = null;
		try {
			makeDirs(file.getAbsolutePath());
			o = new FileOutputStream(file, append);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				o.write(data, 0, length);
			}
			o.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (o != null) {
				try {
					o.close();
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * Creates the directory named by the trailing filename of this file, including the complete directory path required
	 * to create this directory. <br/>
	 * <br/>
	 * <ul>
	 * <strong>Attentions:</strong>
	 * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
	 * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
	 * </ul>
	 * 
	 * @param filePath
	 * @return true if the necessary directories have been created or the target directory already exists, false one of
	 * the directories can not be created.
	 * <ul>
	 * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
	 * <li>if target directory already exists, return true</li>
	 * <li>return {@link File#makeFolder}</li>
	 * </ul>
	 */
	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);
		if (TextUtils.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}

}
