package com.example.asus.makanyab.db;

import com.example.asus.makanyab.models.Contact;
import com.example.asus.makanyab.models.Location;
import com.example.asus.makanyab.models.Setting;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;


/**
 @author saeed khorsand @ stuland
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{
            Contact.class,
            Location.class,
            Setting.class

    };

	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt",classes);
	}
}
