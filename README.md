# smsconverting

I had updated an Android smartphone from Cyanogenmod 12.1 to LineageOS 14.1. For this I used the export function of the standard app to save the SMS data. After the update I noticed that the new SMS app has no import/export function.

Using the source code of CM 12.1 I wrote this tool that converts the SMS data into SQL statements.

Apply the sql commands to the database mmssms.db, which can be copied via adb:
```
adb pull /data/user_de/0/com.android.providers.telephony/databases/mmssms.db
```
**Warning:** The created SQL file deletes existing data.


For a successful recovery of the SMS data, including successful recovery of the thread structure, it is necessary in my experience to stop the SMS app in the settings and to delete the data.


I have deleted additional data in the following folder.
```
cd /data/user_de/0/com.android.providers.telephony/databases/
rm *
```

Then I copied the modified mmssms.db into the folder and restarted the smartphone.
```
adb push mmssms.db /data/user_de/0/com.android.providers.telephony/databases/
```

The program has characteristics that refer to the German mobile phone market. The telephone area code.

Use the program at your own risk. I give no guarantee and completeness. So please remember to make backups of your data.

Useful informations I Used:

https://redmine.replicant.us/attachments/909/doc%20android%20MMS-SMS%20DB%20(english).txt

https://github.com/CyanogenMod/android_packages_apps_Mms-caf/blob/cm-12.1/src/com/android/mms/QTIBackupSMS.java
