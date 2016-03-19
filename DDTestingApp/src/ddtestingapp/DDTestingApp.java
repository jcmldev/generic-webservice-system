
package ddtestingapp;

import dd.Timestamp;
import dd.monitor.BackupDataHelper;
import dd.monitor.InterDependency;
import dd.monitor.ServerMonitor;
import dd.monitor.dependencestorage.BitArrayBasedContainer;
import dd.monitor.dependencestorage.DependenceStorage;
import dd.monitor.dependencestorage.DependenceStorageFactory;
import dd.monitor.multihopgp.BackupDataSet;
import dd.monitor.multihopgp.MultihopGpBackupServer;
import dd.output.LoggingContext;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DDTestingApp {
    
    
    // test the backup timeslots
    /*
     * extract history when there is none
     * - when there is only part / later part
     * - when there is missing newer part
     * -- then add the newer part and extract again only changes
     * 
     * 
     * in order to test it - set the timestamp now !!
     * 
     * 
     * 
     */

    /*
    public static void main(String[] args)   
    {
        long now = Timestamp.now();
        long slotsize = 10000;
        long start;
        
        start = now - (now % slotsize);
        start += slotsize;
        
        start = start;
    }
    */
    
    ///* testing multihop gp backup
    public static void main(String[] args) throws InterruptedException  
    {
        // set configuration in ddconfig.xml to slot 10 max slots 10
        Timestamp.setnow(1001);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1002);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1011);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1050);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        
        
        String[] b = MultihopGpBackupServer.getInstance().getBackupData(new String[0], true);
        b[1] = "http://s3/GWS";
        BackupDataSet bd = BackupDataSet.loadRecordsFromLocalData(10, b);
        String bt[] = bd.getDataToTransfer(null);
        MultihopGpBackupServer.getInstance().storeBackupData(bt);        
        String[] b2 = MultihopGpBackupServer.getInstance().getBackupData(new String[0], true);
        
        //now b should == b2
        
        // test here that after 10s with the last slot on value we get no old slot data 
        Timestamp.setnow(1060);
        String[] b2x = MultihopGpBackupServer.getInstance().getBackupData(b2, true);
        //should be empty with last index same as in b2
        
        
        Timestamp.setnow(1100);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1101);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1110);
        ServerMonitor.getInstance().addInterDependencyOccurrence("s1/GWS", "s2/GWS");
        Timestamp.setnow(1120);
        
        String[] b3 = MultihopGpBackupServer.getInstance().getBackupData(b, true);
        b3[5] = "http://s3/GWS";
        String b3t[] = BackupDataSet.loadRecordsFromLocalData(10, b3).getDataToTransfer(bd);


        MultihopGpBackupServer.getInstance().storeBackupData(b3t);

        String[] b4 = MultihopGpBackupServer.getInstance().getBackupData(b, true);
       
        
        b4 = MultihopGpBackupServer.getInstance().getBackupData(new String[0], true);
        b4[9] = "http://s2/GWS";
        BackupDataSet s = BackupDataSet.loadRecordsFromLocalData(10, b4);
        String[] c = s.getDataToTransfer(BackupDataSet.loadRecordsFromLocalData(10, b3));
        BackupDataSet t1 = BackupDataSet.loadRecordsFromTransferedData(10, c);


        String[] f = MultihopGpBackupServer.getInstance().getBackupData(new String[0], true);
        MultihopGpBackupServer.getInstance().storeBackupData(c);
        String[] f1 = MultihopGpBackupServer.getInstance().getBackupData(new String[0], true);
        
        c = c;
    }
    //*/
    
    /* testing DependencyOccurrenceStorage with backup (for any type)
    public static void main(String[] args) throws FileNotFoundException 
    {
        boolean b;
         
        //DependencyOccurrenceStorage ds = new DependencyOccurrenceStorageTimestamp(10000);
        //DependencyOccurrenceStorage ds = new DependencyOccurrenceStorageBitArray(10000, 100, 10);
        DependenceStorage ds = DependenceStorageFactory.getDependenceStorageFromConfiguration(null);        
        LoggingContext  log = LoggingContext.getContext(new Integer(0), "");
        
        ds.addOccurrence(1);
        b = ds.hasOccuredWithinTimewindow(-1, 0);
        b = ds.hasOccuredWithinTimewindow(1, 2);
        b = ds.hasOccuredWithinTimewindow(2, 3);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);
        b = ds.hasOccuredWithinTimewindow(100000, 100001);
        
        ds.writeOut(log);
        
        ds.addOccurrence(1000);
        b = ds.hasOccuredWithinTimewindow(400, 500);
        b = ds.hasOccuredWithinTimewindow(660, 1001);

        ds.writeOut(log);
        
        ds.addOccurrence(9999);
        ds.addOccurrence(10000);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);
        b = ds.hasOccuredWithinTimewindow(10001, 100000);

        ds.writeOut(log);
        
        ds.addOccurrence(10001);
        b = ds.hasOccuredWithinTimewindow(0, 1);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);        
        b = ds.hasOccuredWithinTimewindow(10001, 100000);
        b = ds.hasOccuredWithinTimewindow(10100, 100000);
        b = ds.hasOccuredWithinTimewindow(10101, 100000);

        ds.writeOut(log);
        
        ds.addOccurrence(20001);
        
        ArrayList<String> s = BackupDataHelper.getBackupDataFromStorage("test", ds, 0, 20001, 100);
        BitArrayBasedContainer bc = BackupDataHelper.getBackupDataFromString(0, 20001, 100, (String)s.get(1));
        
        
        b = ds.hasOccuredWithinTimewindow(0, 1);
        b = bc.hasOccuredWithinTimewindow(0, 1);
        b = ds.hasOccuredWithinTimewindow(0, 10000);
        b = bc.hasOccuredWithinTimewindow(0, 10000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);
        b = bc.hasOccuredWithinTimewindow(10000, 100000);
        b = ds.hasOccuredWithinTimewindow(20002, 100000);
        b = bc.hasOccuredWithinTimewindow(20002, 100000);
        b = ds.hasOccuredWithinTimewindow(10100, 20000);
        b = bc.hasOccuredWithinTimewindow(10100, 20000);
        b = ds.hasOccuredWithinTimewindow(10101, 19000);
        b = bc.hasOccuredWithinTimewindow(10101, 19000);

        ds.writeOut(log);
    }
    */
    
    
    
    
    /* testing DependencyOccurrenceStorage (for any type)
    public static void main(String[] args) throws FileNotFoundException 
    {
        boolean b;
         
        //DependencyOccurrenceStorage ds = new DependencyOccurrenceStorageTimestamp(10000);
        //DependencyOccurrenceStorage ds = new DependencyOccurrenceStorageBitArray(10000, 100, 10);
        DependenceStorage ds = DependenceStorageFactory.getDependenceStorageFromConfiguration(null);        
        LoggingContext  log = LoggingContext.getContext(new Integer(0), "");
        
        ds.addOccurrence(1);
        b = ds.hasOccuredWithinTimewindow(-1, 0);
        b = ds.hasOccuredWithinTimewindow(1, 2);
        b = ds.hasOccuredWithinTimewindow(2, 3);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);
        b = ds.hasOccuredWithinTimewindow(100000, 100001);
        
        ds.writeOut(log);
        
        ds.addOccurrence(1000);
        b = ds.hasOccuredWithinTimewindow(400, 500);
        b = ds.hasOccuredWithinTimewindow(660, 1001);

        ds.writeOut(log);
        
        ds.addOccurrence(9999);
        ds.addOccurrence(10000);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);
        b = ds.hasOccuredWithinTimewindow(10001, 100000);

        ds.writeOut(log);
        
        ds.addOccurrence(10001);
        b = ds.hasOccuredWithinTimewindow(0, 1);
        b = ds.hasOccuredWithinTimewindow(0, 100000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);        
        b = ds.hasOccuredWithinTimewindow(10001, 100000);
        b = ds.hasOccuredWithinTimewindow(10100, 100000);
        b = ds.hasOccuredWithinTimewindow(10101, 100000);

        ds.writeOut(log);
        
        ds.addOccurrence(20001);
        b = ds.hasOccuredWithinTimewindow(0, 1);
        b = ds.hasOccuredWithinTimewindow(0, 10000);
        b = ds.hasOccuredWithinTimewindow(10000, 100000);        
        b = ds.hasOccuredWithinTimewindow(20002, 100000);
        b = ds.hasOccuredWithinTimewindow(10100, 20000);
        b = ds.hasOccuredWithinTimewindow(10101, 19000);

        ds.writeOut(log);
    }
    */
    
}
