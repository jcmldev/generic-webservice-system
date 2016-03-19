
package dd.monitor;

import dd.monitor.dependencestorage.BitArrayBasedContainer;
import dd.monitor.dependencestorage.DependenceStorage;
import dd.monitor.multihopgp.TimeSlotSet;
import java.util.ArrayList;
import java.util.BitSet;


public class BackupDataHelper 
{

    public static ArrayList<String> getBackupDataFromStorage(
            String target,
            DependenceStorage occurrenceStorage, 
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize)
    {
        ArrayList<String>           resultArray = new ArrayList<>();
        BitSet                      backupDataBitSet = createBackupDataBitSet(occurrenceStorage, fromTimestamp, toTimestamp, timeSlotSize);
        String                      backupDataString = bitSetToString(backupDataBitSet);
        
        
        resultArray.add(target);
        resultArray.add(backupDataString);
        
        return resultArray;
    }
    
    public static BitSet createBackupDataBitSet(
            DependenceStorage occurrenceStorage, 
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize)
    {
        int             numberOfSlots = ((int)((toTimestamp - fromTimestamp) / (long)timeSlotSize)) + 1;
        BitSet          backupData = new BitSet(numberOfSlots);
        long            stepFromTimestamp;
        long            stepToTimestamp;
        boolean         slotState;
        
        
        for(int i = 0; i < numberOfSlots; i++)
        {
            stepFromTimestamp = fromTimestamp + (i * timeSlotSize);
            stepToTimestamp = stepFromTimestamp + timeSlotSize - 1;
                    
            slotState = occurrenceStorage.hasOccuredWithinTimewindow(stepFromTimestamp, stepToTimestamp);
            
            //debug
            if (slotState)
            {
                //System.out.println("occurance - from / to" + stepFromTimestamp + "/" + stepToTimestamp);
            }
            
            backupData.set(i, slotState);
        }
        
        return backupData;
    }
    
    public static TimeSlotSet createBackupDataSlotSet(
            DependenceStorage occurrenceStorage, 
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize)
    {
        int             numberOfSlots = ((int)((toTimestamp - fromTimestamp) / (long)timeSlotSize)) + 1;
        TimeSlotSet     slotSet = null;
        long            stepFromTimestamp;
        long            stepToTimestamp;
        boolean         slotState;
        long            slotIndex;
                
        
        for(int i = 0; i < numberOfSlots; i++)
        {
            stepFromTimestamp = fromTimestamp + (i * timeSlotSize);
            stepToTimestamp = stepFromTimestamp + timeSlotSize - 1;
                    
            slotState = occurrenceStorage.hasOccuredWithinTimewindow(stepFromTimestamp, stepToTimestamp);
            
            if (slotState)
            {   
                slotIndex = TimeSlotSet.getSlotIndexOfTimestamp(timeSlotSize, stepFromTimestamp);

                if (slotSet == null)
                {
                    slotSet = new TimeSlotSet(slotIndex, timeSlotSize);
                }
                
                slotSet.setSlotValue(slotIndex, slotState);
                
                //debug
                //System.out.println("occurance - from / to" + stepFromTimestamp + "/" + stepToTimestamp);
            }
        }
        
        if (slotSet == null)
        {
            slotSet = new TimeSlotSet(0, timeSlotSize);
        }
        
        return slotSet;
    }
    
    protected static final String             LONG_ARRAY_DELIMETER = "L";
    
    public static String bitSetToString(BitSet bitSet)
    {
        long[]          longArray = bitSet.toLongArray();
        String          result = "";
        
        
        for(long item : longArray)
        {
            result += item + LONG_ARRAY_DELIMETER;
        }
        
        if (result.length() > 0)
        {
            result = result.substring(0, result.length() - 1);
        }
        
        return result;
    }    
    
    public static BitSet getBackupDataFromString( 
            //int numberOfSlots,
            String backupData)
    {
        assert backupData != null;
        
        //BitSet                              restoredBackupData = new BitSet(numberOfSlots);
        BitSet                              restoredData = new BitSet();
        String[]                            backupDataStringArray;
        long[]                              backupDataLongArray;

        
        if (!backupData.isEmpty())
        {
            backupDataStringArray = backupData.split(LONG_ARRAY_DELIMETER);
            backupDataLongArray = new long[backupDataStringArray.length];

            for(int i = 0; i <backupDataStringArray.length; i++)
            {
                backupDataLongArray[i] = Long.parseLong(backupDataStringArray[i]);
            }

            restoredData = BitSet.valueOf(backupDataLongArray);
            //restoredBackupData.or(restoredData);
        }

        return restoredData;
    }
    
    public static BitArrayBasedContainer getBackupDataFromString(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize, 
            String backupData)
    {
        int                                 numberOfSlots = (int)((toTimestamp - fromTimestamp) / (long)timeSlotSize);
        BitSet                              restoredBackupData = new BitSet(numberOfSlots);
        BitSet                              restoredData;
        String[]                            backupDataStringArray;
        long[]                              backupDataLongArray;
        BitArrayBasedContainer              container;

        
        if (!backupData.isEmpty())
        {
            backupDataStringArray = backupData.split(LONG_ARRAY_DELIMETER);
            backupDataLongArray = new long[backupDataStringArray.length];

            for(int i = 0; i <backupDataStringArray.length; i++)
            {
                backupDataLongArray[i] = Long.parseLong(backupDataStringArray[i]);
            }

            restoredData = BitSet.valueOf(backupDataLongArray);
            restoredBackupData.or(restoredData);
        }

        container  = new BitArrayBasedContainer(
                fromTimestamp, 
                toTimestamp - fromTimestamp, 
                timeSlotSize, 
                restoredBackupData);
        
        return container;
    }
}
