package com.android.mms;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class QTIBackupSMS {

    File smsSourceFile;
    File sqlFile;
    private Map<String, Integer> mapNumber = null;
    private Map<Integer, Integer> mapCountOfMessages = null;
    FileOutputStream mFileOutputStream;
    FileInputStream mFileInputStream;
    BufferedOutputStream buf;

    public QTIBackupSMS(File smsFile, File outFile) {
        smsSourceFile = smsFile;
        sqlFile = outFile;
        mapNumber = new HashMap<String, Integer>();
        mapCountOfMessages = new HashMap<Integer, Integer>();
    }

    private enum MsgBox {
        SENT,
        INBOX,
        DRAFT;
    }

    public void performRestore() {
        SMSBackup smsbk;
        try {
            mFileOutputStream = new FileOutputStream(sqlFile);
            mFileInputStream = new FileInputStream(smsSourceFile);
            ObjectInputStream ois = new ObjectInputStream(mFileInputStream);

            String updateDB = new String();
            updateDB = "--- clear database\n";
            updateDB += "delete from sms;\n";
            updateDB += "delete from canonical_addresses;\n";
            updateDB += "delete from raw;\n";
            updateDB += "delete from threads;\n";
            updateDB += "\n";
            updateDB += "--- Delete indices\n";
            updateDB += "delete from canonical_addresses;\n";
            updateDB += "delete from sqlite_sequence where name='canonical_addresses';\n";
            updateDB += "delete from part;\n";
            updateDB += "delete from sqlite_sequence where name='part';\n";
            updateDB += "delete from pdu;\n";
            updateDB += "delete from sqlite_sequence where name='pdu';\n";
            updateDB += "delete from threads;\n";
            updateDB += "delete from sqlite_sequence where name='threads';\n";
            updateDB += "delete from words;\n";
            updateDB += "delete from words_content;\n";
            updateDB += "delete from words_segments;\n";
            updateDB += "\n";
            updateDB += "--- insert SMS data\n";
            mFileOutputStream.write(updateDB.getBytes());

            //FOR Inbox
            int numOfObjects = ois.readInt();
            if (numOfObjects < 1) {
                System.out.println("No messages to restore in Inbox");
            }
            System.out.println("numOfObjects: " + numOfObjects);
            smsbk = (SMSBackup) ois.readObject();
            writeSMS(smsbk, MsgBox.INBOX);

            //For Sent
            numOfObjects = ois.readInt();
            if (numOfObjects < 1) {
                System.out.println("No messages to restore in Sent");
            }
            System.out.println("numOfObjects: " + numOfObjects);
            smsbk = (SMSBackup) ois.readObject();
            writeSMS(smsbk, MsgBox.SENT);

            //For Draft
            numOfObjects = ois.readInt();
            if (numOfObjects < 1) {
                System.out.println("No messages to restore in Draft");
            }
            System.out.println("No messages to restore in Draft");
            System.out.println("numOfObjects: " + numOfObjects);
            smsbk = (SMSBackup) ois.readObject();
            writeSMS(smsbk, MsgBox.DRAFT);

            updateDB = "--- update metadata\n";
            mFileOutputStream.write(updateDB.getBytes());
            for (String key : mapNumber.keySet()) {
                String numbers = new String();
                numbers = "INSERT INTO canonical_addresses (_id, address)";
                numbers += "VALUES("+ mapNumber.get(key) +",\'"+ key +   "\');\n";
                mFileOutputStream.write(numbers.getBytes());
            }
            for (Integer it: mapCountOfMessages.keySet()) {
                String threads_msg = new String();
                threads_msg = "INSERT INTO threads ( date, message_count, recipient_ids, snippet, snippet_cs, read, archived, type, error, has_attachment, attachment_info, notification) values( 1521673329000,";
                threads_msg += mapCountOfMessages.get(it) + ", " +  it +", NULL, 0, 1, 0, 0, 0, 0, NULL, 0);\n";
                mFileOutputStream.write(threads_msg.getBytes());
            }

            updateDB = "UPDATE SMS SET thread_ID = (SELECT t._id from threads as t left join canonical_addresses ca where ca._id=t.recipient_ids and sms.address = ca.address);\n";
            mFileOutputStream.write(updateDB.getBytes());
            mFileOutputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (StreamCorruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void writeSMS(SMSBackup smsbk, MsgBox box) {

        try {
        switch (box) {
            case INBOX:                
                for (SMSObject smsObj : smsbk.SMSList) {
                    mFileOutputStream.write(convertSMS(smsObj, 1).getBytes());
                    mFileOutputStream.flush();
                }
                break;

            case SENT:
                for (SMSObject smsObj : smsbk.SMSList) {
                    mFileOutputStream.write(convertSMS(smsObj, 2).getBytes());
                    mFileOutputStream.flush();
                }
                break;

            case DRAFT:
                for (SMSObject smsObj : smsbk.SMSList) {
                    mFileOutputStream.write(convertSMS(smsObj, 3).getBytes());
                    mFileOutputStream.flush();
                }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertSMS(SMSObject smsObj, int smstype)
    {
        String sms = new String();
        String address = smsObj.getAddr();
        //format address string
        if(address.startsWith("+4") || address.startsWith("01"))
            address = address.replaceAll("\\s+","");
        else
            address = address.trim();
        //+49 -> german prefix for telephone numbers
        if(address.startsWith("01") && address.length() > 8)
        {
            address = "+49" + address.substring(1, address.length());
        }

        if(!mapNumber.containsKey(address))
        {
            mapNumber.put(address, mapNumber.size());
        }
        int adressId = mapNumber.get(address);

        if(!mapCountOfMessages.containsKey(adressId))
        {
            mapCountOfMessages.put(adressId, 1);
        }
        else
        {
            int oldValue = mapCountOfMessages.get(adressId);
            oldValue++;
            mapCountOfMessages.put(adressId, oldValue);            
        }

        sms = "INSERT INTO sms (_id ,thread_id ,address, person, date, date_sent,protocol,read ,status ,type ,reply_path_present,";
        sms += "subject,body,service_center,locked,sub_id, error_code,creator,seen,priority) VALUES(" +  smsObj.getId() + "," + mapNumber.get(address) + ",";
        sms += "\'" + address + "\', NULL, " +  smsObj.getDateTime() + "," + smsObj.getDateSentTime() + ",0, 1, -1," + smstype +", 0, NULL, ";
        sms += "\'" + smsObj.getMsg().replace("\'", "\'\'") +"\', NULL, 0, 1, 0, \'com.android.messaging\', 1, -1);\n";
        return sms;
    }
}