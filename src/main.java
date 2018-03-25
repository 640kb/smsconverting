import java.io.File;

import com.android.mms.QTIBackupSMS;

public class main {
	
	private static void usage()
	{
		System.out.println("./programm <smsFile> [<outFile>]\n");
	}

    public static void main(String[] args) {
        // TODO Auto-generated method stub        

    	String sourceFile = new String();
    	String outFile = new String();
    	
    	if(args.length == 1)
    	{
    		sourceFile = args[0];
    		outFile = sourceFile + ".sql";
    	}
    	else if(args.length == 2)
    	{
    		sourceFile = args[0];
    		outFile = args[1];
    	}
    	
    	if(sourceFile.isEmpty())
    	{
    		usage();
    		return;
    	}
    		
    	File smsFile = new File(sourceFile);
    	if(!smsFile.exists())
    	{
    		System.out.println("SMS File doesnt exists");
    	}
    	File sqlFile = new File(outFile);
    	
        QTIBackupSMS smsreader = new QTIBackupSMS(smsFile, sqlFile);
        smsreader.performRestore();
    }

}
