package com.Plugins.MobileNativeCode;//Use only this package, do not change it!!!

// !! Do not use korean annotation !! if you use korean this code not compile
// !! Do not add unuse library !! if you use korean this code not compile

import android.app.Activity;
import android.support.annotation.Keep;
//import android.support.v4.app.Fragment;

// get cache dir
import android.os.Environment;

//import File
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import com.epicgames.ue4.GameActivity;

//Android Context
import android.content.Context;

//Android Intent
import android.content.Intent;

//Android Gallery
import android.provider.MediaStore;
import android.net.Uri;
import android.content.ContentUris;
import android.view.View;
import android.widget.ImageView;


//Android Toast
import android.widget.Toast;
import com.Plugins.MobileNativeCode.NativeUI;

//Android Database 
import android.database.Cursor;

//Android image
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.ContentResolver;
import java.io.ByteArrayOutputStream;

//CompareFile
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
//import org.apache.commons.io.FileUtils;

//WriteFile
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.nio.charset.*;

//remove file
import java.util.ArrayList;

//generate secret key
import java.security.SecureRandom;
import com.Plugins.MobileNativeCode.ECDHCurve25519;


@Keep
public class asyncAndroidFunctionLibClass 
{

	static Activity GameActivity;
	static int Gallery_Picture_Size = 0;
	static boolean Is_Profile = false;
	static String FileName = "None";
	static String UE4_Android_Cache_Path = GameActivity.getApplicationContext().getExternalCacheDir().getAbsolutePath();

	//Calling *.cpp code
	@Keep
	public static native void CallBackAndroidForImage(String returnStr);

	// Calling Java code asynchronously and returning the value back to C++
	@Keep
	public static void CallBackFunction(final Activity activity) 
	{	
		GameActivity = activity;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				//showToast("Call_Back");
				CallBackAndroidForImage(FileName);
				
				//init
				Gallery_Picture_Size = 0;
				Is_Profile = false;
				FileName = "None";
			}
		});
	}

	// save bitmap file convert format png as ue4 cache folder
	@Keep
	public static void saveBitmapToPng(Activity activity, String AppName, String ImagePath, String ImageName, Bitmap bitmap)
	{	
		//showToast("Enter Create Png File From Bitmap");
		GameActivity = activity;
		File savefile;
		File GetModified = new File(ImagePath);
		
		if(Is_Profile)
		{
			Delete_Old_Profile_Image(activity);
			FileName = "profile_" + ImageName;
		}
		else
		{
			FileName = ImageName;
		}

		savefile = new File(activity.getApplicationContext().getExternalCacheDir().getAbsolutePath() +"/" + FileName);

		
		//showToast("Path: " + savefile.toPath().toAbsolutePath());
		//showToast("CallBitmapToPng, Gallery_Picture_Size: "+ String.valueOf(Gallery_Picture_Size) +"   Is_Profile: " +  String.valueOf(Is_Profile) + "FileName: " +  FileName);
		//showToast("Check Point");
		
		try
		{
			//showToast("Check Point 2");

			savefile.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(savefile);
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

			fos.close();

			Add_Line_To_List(ImageName, GetModified.lastModified());
		}
		catch(Exception e)
		{

		}

		CallBackFunction(activity);
	}
	

	//Call from GameActivity ref MobileNativeCode_UPL_Android.xml
	@Keep
	public static void sendPictureToApplicationPath(Activity activity, String AppName, Uri imgUri) 
	{
		GameActivity = activity;
		Check_Config();

		//showToast("CallCreatePath, Gallery_Picture_Size: "+ String.valueOf(Gallery_Picture_Size) +"   Is_Profile: " +  String.valueOf(Is_Profile));

		String imagePath =  getPath(imgUri);
		//showToast(imagePath);
		String ImageName = Extract_image_name(imagePath) + ".png";
		boolean Is_In_ImageList = false;

		try
		{
			Is_In_ImageList = Check_Image_List(imagePath, ImageName);
		}
		catch (Exception e)
		{
		}
		
		//if (Check_Same_Image(FirstFile, SecondFile) && !Is_Profile)
		if (Is_In_ImageList && !Is_Profile)
		{
			//branch: file in Cache + profile boolean is false
			//showToast("branch: file in Cache + profile boolean is false");
			File CheckFile =  new File(imagePath);
			if(CheckFile.isFile())
			{
				FileName = ImageName;
				CallBackFunction(activity);
			}
			else
			{
				File Image_List_in_Cache = new File(UE4_Android_Cache_Path + "/Orginal_Image_List.txt");
				ImageList_Delete(ImageName);
				//Image_List_in_Cache.delete();

				Bitmap imgBitmap =  BitmapFactory.decodeFile(imagePath);
				saveBitmapToPng(activity, AppName, imagePath, ImageName, imgBitmap);
			}
		}
		else
		{	
			try
			{
				if(Is_Profile)
				{

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(imagePath, options);

					options.inSampleSize  = calculateInSampleSize(options, Gallery_Picture_Size, Gallery_Picture_Size);
					options.inJustDecodeBounds = false;

					Bitmap imgBitmap =  BitmapFactory.decodeFile(imagePath, options);
					//showToast("Sampled Height: " + String.valueOf(imgBitmap.getHeight()) + "   Sampled Width: "+ String.valueOf(imgBitmap.getWidth()));

					int min = Math.min(imgBitmap.getWidth() , imgBitmap.getHeight());
					int absValue = Math.abs(imgBitmap.getHeight() - imgBitmap.getWidth())/2;
					Bitmap croppedBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
							
					//square cropping in image
					if(imgBitmap.getWidth() > imgBitmap.getHeight())
					{
						try
						{
							croppedBitmap = Bitmap.createBitmap(imgBitmap, absValue, 0, min, min);

						}
						catch (Exception e)
						{
							showToast("Exeption Of Height min: " + String.valueOf(min) + "   Height: " + String.valueOf(imgBitmap.getHeight()) + "   Width: " +String.valueOf(imgBitmap.getWidth()) + "   abs: " + String.valueOf(absValue));
						}
					}
					else if(imgBitmap.getWidth() < imgBitmap.getHeight())
					{
						try
						{
							croppedBitmap = Bitmap.createBitmap(imgBitmap, 0, absValue, min, min);
						}
						catch (Exception e)
						{
							showToast("Exeption Of Width min: " + String.valueOf(min) + "   Height: " + String.valueOf(imgBitmap.getHeight()) + "   Width: " +String.valueOf(imgBitmap.getWidth()) + "   abs: " + String.valueOf(absValue));

						}
					}
					else
					{
						croppedBitmap = imgBitmap;
					}

					try
					{
						Bitmap result = Bitmap.createScaledBitmap(croppedBitmap, Gallery_Picture_Size, Gallery_Picture_Size, false);
						saveBitmapToPng(activity, AppName, imagePath, ImageName, result);
					}
					catch(Exception e)
					{
						showToast("Failed Scailing");
					}
				}
				else
				{
					Bitmap imgBitmap =  BitmapFactory.decodeFile(imagePath);
					saveBitmapToPng(activity, AppName, imagePath, ImageName, imgBitmap);

					// Old Version
					// ContentResolver resolver = activity.getApplicationContext().getContentResolver();
					// InputStream instream = resolver.openInputStream(imgUri);
					// Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
					// instream.close();
					// saveBitmapToPng(activity, AppName, imagePath, ImageName, imgBitmap);
				}

			}	
			catch (Exception e)
			{
				//showToast("Fail to decode Bitmap");
			}
		}
	}

	@Keep
	public static void showToast(final String msg)
	{
		GameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(GameActivity.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Keep
	public static String getPath(Uri uri) 
	{
		//showToast("GetPath");
		String result;
		Cursor cursor = GameActivity.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
		
		if (cursor == null) 
		{ // Source is Dropbox or other similar local file path
			//showToast("CursorNULL");
			result = uri.getPath();
		} else 
		{
			//showToast("CursorNotNULL");
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		//showToast("REsult");
		return result;
	}

	@Keep
	public static boolean Delete_Old_Profile_Image(Activity activity)
	{

		String dirPath = activity.getApplicationContext().getExternalCacheDir().getAbsolutePath();
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		List<File> RemoveList = new ArrayList<File>();

		if(files.length <= 0)
		{
			//showToast("Profile Not Exist");
			return true;
		}

		for(File f : files) 
		{
			if(f.isFile() && f.getName().startsWith("profile_")) 
			{
				//showToast("CheckPoint_set remove file");	
				RemoveList.add(f);
			}
		}
		
		while(RemoveList.size() > 0)
		{
			File File_For_Remove = RemoveList.remove(0);

			if(File_For_Remove.isFile())
			{
				//showToast("CheckPoint_IsFile");	

				if(File_For_Remove.delete())
				{
					//showToast("Success Delete Old Profile");
				}
				else
				{
					//showToast("Fail Delete Old Profile");
				}
			}
			else
			{
				//showToast("Profile Not Exist");
			}
		}

		return true;
	}

	@Keep
	public static boolean Check_Same_Image(Path FirstFile, Path SecondFile)
	{

		return contentEquals(FirstFile, SecondFile);
	}
	
	@Keep
	public static String Extract_image_name(String ImagePath)
	{		
		String ImageName = null;
		int cut = ImagePath.lastIndexOf('/');
		if (cut != -1) 
		{
			ImageName = ImagePath.substring(cut + 1);

			int parse = ImageName.lastIndexOf('.');
			ImageName = ImageName.substring(0, parse);
			//showToast("ImageName is" + ImageName);
		}
		return ImageName;
	}

	@Keep
	private static boolean contentEquals(Path firstFile, Path secondFile)
    {
        try {
            if (Files.size(firstFile) != Files.size(secondFile)) {
				showToast("Files are Not Same Size");
                return false;
            }
 
            byte[] first = Files.readAllBytes(firstFile);
            byte[] second = Files.readAllBytes(secondFile);
			showToast("Array.Equals");
            return Arrays.equals(first, second);
        } catch (Exception e) {
            e.printStackTrace();
        }
		//showToast("ContentEquals Exception");
        return false;
    }

	@Keep
	private static boolean Check_Same_Name_File_In_Cache(String ImageName)
	{
		File CheckFile = new File(UE4_Android_Cache_Path +"/" + ImageName);
		return CheckFile.isFile();
	}

	@Keep
	private static void Check_Config()
	{
		// Call config File
		try {
			Path path = UE_Android_PathGetter("Config.txt");
			Charset charset = Charset.forName("UTF-8");
			List<String> lines = Files.readAllLines(path, charset);
			for(String line : lines) {
				Gallery_Picture_Size = Integer.parseInt(line);
			}
			if(Gallery_Picture_Size > 0)
			{
				Is_Profile = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Keep
	private static boolean Check_Image_List(String ImagePath, String ImageName) throws IOException
	{
		File Orginal_Image = new File(ImagePath);
		
		Path Orginal_Image_List = UE_Android_PathGetter("Orginal_Image_List.txt");
		Charset charset = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(Orginal_Image_List, charset);
		for(String line : lines)
		{
			String[] ParseArray  = ParseLine(line);
			if (ParseArray[0] == ImageName)
			{
				if(ParseArray[1] == String.valueOf(Orginal_Image.lastModified()))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}

		return false;
	}

	@Keep
	private static String[] ParseLine(String line)
	{
		return line.split(",");
	}

	@Keep
	private static void Add_Line_To_List(String Orginal_FileName, long date) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(UE4_Android_Cache_Path + "/Orginal_Image_List.txt", true));
		PrintWriter pw = new PrintWriter(bw,true);
		pw.write(Orginal_FileName + "," + String.valueOf(date));
		pw.flush();
		pw.close();
	}

	@Keep
	private static void Create_Config(int return_size) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(UE4_Android_Cache_Path + "/Config.txt", false));
		PrintWriter pw = new PrintWriter(bw,true);
		pw.write(String.valueOf(return_size));
		pw.flush();
		pw.close();
	}

	@Keep
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@Keep
	public static void ImageList_Delete(String ImageName)
	{
		String FileName =  UE4_Android_Cache_Path + "/Orginal_Image_List.txt";
		try
		{
			List<String> newLines = new ArrayList<>();
			for (String line : Files.readAllLines(Paths.get(FileName), StandardCharsets.UTF_8)) 
			{
				if (line.contains(ImageName)) 
				{
					newLines.add(line.replace(ImageName, ""));
				} 
				else 
				{
					newLines.add(line);
				}
			}
			Files.write(Paths.get(FileName), newLines, StandardCharsets.UTF_8);
		}
		catch(Exception e)
		{}
	}

	@Keep
    public static String binarytoHexString(byte[] binary)
    {
        StringBuilder sb = new StringBuilder(binary.length*2);

        // Go backwards (left to right in the string) since typically you print the low-order
        // bytes to the right.
        for (int i = binary.length-1; i >= 0; i--) {
            // High nibble first, i.e., to the left.
            // Note that bytes are signed in Java. However, "int x = abyte&0xff" will always
            // return an int value of x between 0 and 255.
            // "int v = binary[i]>>4" (without &0xff) does *not* work.
            int v = (binary[i]&0xff)>>4;
            char c;
            if (v < 10) {
                c = (char) ('0'+v);
            } else {
                c = (char) ('a'+v-10);
            }
            sb.append(c);
            // low nibble
            v = binary[i]&0x0f;
            if (v < 10) {
                c = (char) ('0'+v);
            } else {
                c = (char) ('a'+v-10);
            }
            sb.append(c);
        }

        return sb.toString();
    }


	@Keep
	private static Path UE_Android_PathGetter(String file_name)
	{
		if(file_name == "")
		{
			return Paths.get(UE4_Android_Cache_Path);
		}
		else
		{
			return  Paths.get(UE4_Android_Cache_Path + "/" + file_name);
		}
	}
	@Keep
	public static void CreateSecretKey(Activity activity)
	{
		GameActivity = activity;
		DeleteAllKeys(activity);

		try{
				String file_name = "yeobimillsoe";
				Path SecretKey = UE_Android_PathGetter(file_name);
				SecureRandom random = new SecureRandom();
				Files.write(SecretKey, ECDHCurve25519.generate_secret_key(random));
			}
				catch(Exception e)
			{
			}
	}

	@Keep
	public static void CreatePublicKey()
	{
		Path My_Key = UE_Android_PathGetter("yeobimillsoe");
		if(Files.exists(My_Key))
		{
			try{
					String file_name = "yegaeolsgongoe";
					Path PublicKey = UE_Android_PathGetter(file_name);
					byte[] SecretKey = Files.readAllBytes(My_Key);
					Files.write(PublicKey, ECDHCurve25519.generate_public_key(SecretKey));
				}catch(Exception e)
				{
				}
		}
	}

	@Keep
	public static void CreateSharedcKey()
	{
		Path My_Key = UE_Android_PathGetter("yeobimillsoe");
		Path OppenentKey = UE_Android_PathGetter("OpponentKey");
		if(Files.exists(My_Key) && Files.exists(OppenentKey))
		{
			try{
					String file_name = "ygyohuaneolsoe";
					Path SharedKey = UE_Android_PathGetter(file_name);
					byte[] my_secret_key = Files.readAllBytes(My_Key);
					byte[] other_public_key = Files.readAllBytes(OppenentKey);
					Files.write(SharedKey, ECDHCurve25519.generate_shared_secret(my_secret_key, other_public_key));
			}catch(Exception e)
			{
			}
		}
	}
	@Keep
	public static void DeleteAllKeys(Activity activity)
	{
		String dirPath = activity.getApplicationContext().getExternalCacheDir().getAbsolutePath();
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		List<File> RemoveList = new ArrayList<File>();

		if(files.length > 0)
		{
			for(File f : files) 
			{
				if(f.isFile() && (f.getName() == "yeobimillsoe" || f.getName() == "yegaeolsgongoe" || f.getName() == "ygyohuaneolsoe") )
				{
					RemoveList.add(f);
				}
			}
			
			while(RemoveList.size() > 0)
			{
				File File_For_Remove = RemoveList.remove(0);

				if(File_For_Remove.isFile())
				{

					if(File_For_Remove.delete())
					{
						// delete all keys
					}
					else
					{
					}
				}
				else
				{
				}
			}
		}
	}

	// !! notice !! Functions in this code page that are reentrant after the function below is executed do not own the same intent.
	// that's why you can't use intent.getExtra().

	//This function is called in Blueprint + Do not use korean
	
	@Keep
	public static void GalleryOpen(Activity activity)
	{
		GameActivity = activity;
		// Set Intent. Intent is a container used to exchange information between activities.
		Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		// Sending intent to game activity. This method is not recommended. You should create a Result function in each activity.
		// Code must be added to the result function with the xml command in MobileNativeCode_UPL_Android.
		try{Create_Config(-1);}catch(Exception e){e.printStackTrace();}
		
		activity.startActivityForResult(intent,1723828);

	}

	//This function is called in Blueprint

	@Keep
	public static void OpenGallery_Create_Profile(Activity activity, int return_size)
	{
		GameActivity = activity;
		//showToast("CallProfile, Gallery_Picture_Size: "+ String.valueOf(Gallery_Picture_Size) +"   Is_Profile: " +  String.valueOf(Is_Profile) + "FileName: " +  FileName);

		Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// intent.putExtra("Is_Profile", true);
		// intent.putExtra("Gallery_Picture_Size", return_size);

		NativeUI.showToast(activity, "InputSize: " + String.valueOf(intent.getIntExtra("Gallery_Picture_Size", -1)), 5);

		// List<String> lines = Arrays.asList("128");//, ,"The second line"
		// Path Config_File = Paths.get(activity.getApplicationContext().getExternalCacheDir().getAbsolutePath() + "/Config.txt");
		// Files.write(Config_File, lines, StandardCharsets.UTF_8);

		try{Create_Config(return_size);}catch(Exception e){e.printStackTrace();}
		activity.startActivityForResult(intent,1723828);
	}

}



