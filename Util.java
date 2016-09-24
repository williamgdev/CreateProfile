package com.mac.fireflies.wgt.createprofile;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 9/22/2016.
 */

public class Util {
    public static File createNamePicture() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "photo_" + time + "_";
        //File dir = getAlbumDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(name, ".jpg");

        return image;
    }
    public static File getAlbumDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory().toString()
        );
    }
}
